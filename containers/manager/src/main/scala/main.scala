package org.grid_search.manager
import com.typesafe.config.ConfigFactory
import org.grid_search.common.config.FileConfigReader
import org.grid_search.common.marshalling.{WorkParser, unParseResult}
import org.grid_search.common.middleware
import org.grid_search.common.stats.{StatsDLogger, getLogger}
import org.grid_search.common.work_split.{Aggregator, Result, Work, aggregateResults}

import scala.concurrent.Promise
import upickle.default

def getConfigReader: FileConfigReader = {
    if (System.getenv("LOCAL") == "true") {
        println("-------------- Using local config --------------")
        FileConfigReader("manager_local.conf")
    } else {
        val config = ConfigFactory
            .parseString(s"metrics.prefix: ${System.getenv("NODE_ID")}")
            .withFallback(ConfigFactory.load("manager.conf"))
        val reader = FileConfigReader(config)
        StatsDLogger.init(reader.getMetricsConfig)
        reader
    }
}

def produceWork(workParser: WorkParser, rabbitMq: middleware.Rabbit, workQueue: String): Option[Int] = {
    try {
        val subWorks = workParser.work.split(workParser.maxItemsPerBatch, Some(5))
        var subWorksAmount = 0
        implicit val writer: default.Writer[Work] = org.grid_search.common.marshalling.workRW

        for (subWork <- subWorks) {
            val parsed = WorkParser.parse(subWork)
            println("Sending work: " + parsed)
            rabbitMq.produce(workQueue, parsed.getBytes)
            subWorksAmount += 1
        }
        Some(subWorksAmount)
    } catch case _: Exception => None
}

def consumeResults(rabbitMq: middleware.Rabbit, resultsQueue: String, endEvent: String, aggregator: Aggregator, responsesToWait: Int): Unit = {
    var results: List[Result] = List()
    val startTime = System.currentTimeMillis()
    val allResultsReceived = Promise[Unit]()
    println(s"Waiting for $responsesToWait results")

    rabbitMq.setConsumer(resultsQueue, message => {
        val messageStr = new String(message, "UTF-8")
        val newResult = unParseResult(aggregator, messageStr)
        results = newResult :: results
        if (results.length == responsesToWait) {
            val aggregatedResults = aggregateResults(results)
            val endTime = System.currentTimeMillis()
            println(s"Got all results - $aggregatedResults - in ${endTime - startTime} ms")
            getLogger.gauge("completion_time", endTime - startTime)
            allResultsReceived.success(())
        }
        true
    })

    rabbitMq.startConsuming(Some(allResultsReceived.future))

    rabbitMq.publish(endEvent, "end".getBytes("UTF-8"))
    rabbitMq.close()
}

@main
def main(): Unit = {
    val config = getConfigReader

    val rabbitMq = middleware.Rabbit(config.getMiddlewareConfig)

    val queues = config.getQueuesConfig

    val workPath = config.getWorkConfig.path
    val workParser = WorkParser.fromJsonFile(workPath)
    val subWorksAmount = produceWork(workParser, rabbitMq, queues.work)

    subWorksAmount match {
      case None => rabbitMq.close()
      case Some(amount) => consumeResults(rabbitMq, queues.results, queues.endEvent, workParser.work.aggregator, amount)
    }
}
package org.grid_search.manager
import com.typesafe.config.ConfigFactory
import org.grid_search.common.config.FileConfigReader
import org.grid_search.common.marshalling.{WorkParser, unParseResult}
import org.grid_search.common.middleware
import org.grid_search.common.stats.StatsDLogger
import org.grid_search.common.work_split.Aggregator

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

def produceWork(workParser: WorkParser, rabbitMq: middleware.Rabbit, workQueue: String): Unit = {
    val subWorks = workParser.work.split(workParser.maxItemsPerBatch)

    for (subWork <- subWorks) {
        val parsed = WorkParser.parse(subWork)
        println("Sending work: " + parsed)
        rabbitMq.produce(workQueue, parsed.getBytes)
    }
}

def consumeResults(rabbitMq: middleware.Rabbit, resultsQueue: String, aggregator: Aggregator): Unit = {
    rabbitMq.setConsumer(resultsQueue, message => {
        val result = unParseResult(aggregator, new String(message, "UTF-8"))
        println("Received result: " + result)
        true
    })
    rabbitMq.startConsuming()
}

@main
def main(): Unit = {
    val config = getConfigReader

    val rabbitMq = middleware.Rabbit(config.getMiddlewareConfig)
    val queues = config.getQueuesConfig
    val workPath = config.getWorkConfig.path
    val workParser = WorkParser.fromJsonFile(workPath)

    produceWork(workParser, rabbitMq, queues.work)

    consumeResults(rabbitMq, queues.results, workParser.work.aggregator)
}
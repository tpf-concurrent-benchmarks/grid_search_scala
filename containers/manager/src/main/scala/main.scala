package org.grid_search.manager
import org.grid_search.common.config.{FileConfigReader, WorkConfig}
import org.grid_search.common.work_split.{CircularIterator, Interval, Work, Aggregator}
import org.grid_search.common.marshalling.{WorkParser, unParseResult}

import com.typesafe.config.ConfigFactory
import org.grid_search.common.middleware
import org.grid_search.common.config.FileConfigReader


def getConfigReader: FileConfigReader = {
    if (System.getenv("LOCAL") == "true") {
        println("-------------- Using local config --------------")
        FileConfigReader("manager_local.conf")
    } else {
        FileConfigReader("manager.conf")
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
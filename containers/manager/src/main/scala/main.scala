package org.grid_search.manager
import config.{FileConfigReader, QueuesConfig, WorkConfig}
import work_split.{CircularIterator, Interval, Work, Aggregator}
import marshalling.{WorkParser, unParseResult}

import com.newmotion.akka.rabbitmq
import com.typesafe.config.ConfigFactory


def getConfigReader: FileConfigReader = {
    if (System.getenv("LOCAL") == "true") {
        println("-------------- Using local config --------------")
        FileConfigReader("manager_local.conf")
    } else {
        FileConfigReader()
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
    rabbitMq.setConsumer(resultsQueue, (message) => {
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
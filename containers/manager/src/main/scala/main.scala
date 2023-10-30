package org.grid_search.manager
import config.{FileConfigReader, QueuesConfig, WorkConfig}
import work_split.{CircularIterator, Interval, Work, Aggregator}
import marshalling.{parseWork, workFromJson, unParseResult}

import com.newmotion.akka.rabbitmq
import com.typesafe.config.ConfigFactory


def getConfigReader(): FileConfigReader = {
    if (System.getenv("LOCAL") == "true") {
        println("-------------- Using local config --------------")
        FileConfigReader("manager_local.conf")
    } else {
        FileConfigReader()
    }
}

def produceWork( config: WorkConfig, rabbitMq: middleware.Rabbit, workQueue: String ): Unit = {
    val path = config.path
    val subWorks = workFromJson(path)

    for (subWork <- subWorks) {
        val parsed = parseWork(subWork)
        println("Sending work: " + parsed)
        rabbitMq.produce(workQueue, parsed.getBytes("UTF-8"))
    }
}

def getResults( rabbitMq: middleware.Rabbit, resultsQueue: String ): Unit = {
    rabbitMq.setConsumer(resultsQueue, (message) => {
        val result = unParseResult(Aggregator.Mean, new String(message, "UTF-8"))
        println("Received result: " + result)
        true
    })
    rabbitMq.startConsuming()
}

@main
def main(): Unit = {
    val config = getConfigReader()

    val rabbitMq = middleware.Rabbit(config.getMiddlewareConfig)
    val queues = config.getQueuesConfig
    
    produceWork(config.getWorkConfig, rabbitMq, queues.work)

    getResults(rabbitMq, queues.results)
}
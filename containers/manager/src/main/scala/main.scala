package org.grid_search.manager
import config.{FileConfigReader, QueuesConfig, WorkConfig}
import work_split.{CircularIterator, Interval, Work, workFromJson}

import com.newmotion.akka.rabbitmq
import com.typesafe.config.ConfigFactory



case class Message(data: List[List[Int]]) derives upickle.default.ReadWriter

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
        val parsed = upickle.default.write(subWork)
        println("Sending work: " + parsed)
    }
}

def getResults( rabbitMq: middleware.Rabbit, resultsQueue: String ): Unit = {
    rabbitMq.setConsumer(resultsQueue, (message) => {
        val data = upickle.default.read[Message](new String(message, "UTF-8"))
        println("Received message: " + data)
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
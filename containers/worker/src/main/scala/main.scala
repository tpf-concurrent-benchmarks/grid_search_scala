package org.grid_search.worker
import config.{FileConfigReader, QueuesConfig}
import work_split.{CircularIterator, Interval, Work}
import marshalling.{unParseWork, workFromJson}

import com.newmotion.akka.rabbitmq
import com.typesafe.config.ConfigFactory


def getConfigReader(): FileConfigReader = {
    if (System.getenv("LOCAL") == "true") {
        println("-------------- Using local config --------------")
        FileConfigReader("worker_local.conf")
    } else {
        FileConfigReader()
    }
}

def work( rabbitMq: middleware.Rabbit, workQueue: String, resultsQueue: String ): Unit = {
    rabbitMq.setConsumer(workQueue, (workString) => {
        val work = unParseWork(new String(workString, "UTF-8"))
        println("Received work: " + work)
        
        true
    })
    rabbitMq.startConsuming()
}

@main
def main(): Unit = {
    val config = getConfigReader()

    val rabbitMq = middleware.Rabbit(config.getMiddlewareConfig)
    val queues = config.getQueuesConfig
    
    work( rabbitMq, queues.work, queues.results )
}
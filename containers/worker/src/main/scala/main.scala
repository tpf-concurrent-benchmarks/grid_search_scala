package org.grid_search.worker
import org.grid_search.common.config.{FileConfigReader, QueuesConfig}
import org.grid_search.common.work_split.{CircularIterator, Interval, Work, Result}
import org.grid_search.common.marshalling.{WorkParser, parseResult}
import org.grid_search.common.middleware.Rabbit

import com.typesafe.config.ConfigFactory


def getConfigReader: FileConfigReader = {
    if (System.getenv("LOCAL") == "true") {
        println("-------------- Using local config --------------")
        FileConfigReader("worker_local.conf")
    } else {
        FileConfigReader("worker.conf")
    }
}

def receiveWork(rabbitMq: Rabbit, workQueue: String, resultsQueue: String): Unit = {
    rabbitMq.setConsumer(workQueue, workString => {
        val work = WorkParser.unParse(new String(workString, "UTF-8"))

        val result: Result = work.calculateFor(mainFunc)

        val resultString = parseResult(result)
        println("Sending result: " + resultString)
        rabbitMq.produce(resultsQueue, resultString.getBytes("UTF-8"))

        true
    })
    rabbitMq.startConsuming()
}

@main
def main(): Unit = {
    val config = getConfigReader

    val rabbitMq = Rabbit(config.getMiddlewareConfig)
    val queues = config.getQueuesConfig
    
    receiveWork(rabbitMq, queues.work, queues.results)
}
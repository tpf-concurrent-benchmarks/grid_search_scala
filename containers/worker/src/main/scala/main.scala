package org.grid_search.worker
import com.typesafe.config.ConfigFactory
import org.grid_search.common.config.FileConfigReader
import org.grid_search.common.marshalling.{WorkParser, parseResult}
import org.grid_search.common.middleware.Rabbit
import org.grid_search.common.stats.{StatsDLogger, getLogger}
import org.grid_search.common.work_split.Result




def getConfigReader: FileConfigReader = {
    if (System.getenv("LOCAL") == "true") {
        println("-------------- Using local config --------------")
        FileConfigReader("worker_local.conf")
    } else {
        val config = ConfigFactory
            .parseString(s"metrics.prefix: ${System.getenv("NODE_ID")}")
            .withFallback(ConfigFactory.load("worker.conf"))
        val reader = FileConfigReader(config)
        StatsDLogger.init(reader.getMetricsConfig)
        reader

    }
}

def receiveWork(rabbitMq: Rabbit, workQueue: String, resultsQueue: String): Unit = {
    var resultsCount = 0
    rabbitMq.setConsumer(workQueue, workString => {
        val work = WorkParser.unParse(new String(workString, "UTF-8"))

        val result: Result = work.calculateFor(griewankFunc)

        val resultString = parseResult(result)
        rabbitMq.produce(resultsQueue, resultString.getBytes("UTF-8"))
        resultsCount += 1
        getLogger.increment("results_produced")
        if (resultsCount == 1000) {
            println("Produced 1000 results")
            resultsCount = 0
        }

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
package org.grid_search.worker
import com.typesafe.config.ConfigFactory
import org.grid_search.common.config.FileConfigReader
import org.grid_search.common.marshalling.{resultW, workRW}
import org.grid_search.common.middleware.Rabbit
import org.grid_search.common.stats.StatsDLogger
import org.grid_search.common.work_split.{Result, Work}
import upickle.default

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

@main
def main(): Unit = {
    val config = getConfigReader

    val rabbitMq = Rabbit(config.getMiddlewareConfig)

    Worker(config.getQueuesConfig).start(rabbitMq)
}
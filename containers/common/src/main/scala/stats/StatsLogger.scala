package org.grid_search.common
package stats

import com.timgroup.statsd.{StatsDClient, NonBlockingStatsDClient}
import config.MetricsConfig

object StatsLogger {
    var instance: Option[StatsLogger] = None

    def apply(config: MetricsConfig): StatsLogger = {
        val host = config.host
        val port = config.port

        val logger = new StatsLogger(new NonBlockingStatsDClient("grid_search", host, port))
        instance = Some(logger)
        logger
    }

    @throws(classOf[Exception])
    def getInstance: StatsLogger = {
        instance match {
            case Some(logger) => logger
            case None => throw new Exception("StatsLogger is not initialized")
        }
    }
}

case class StatsLogger(client: StatsDClient) {

    def runAndMeasure[T](metricName: String, f: => T): T = {
        val startTime = System.currentTimeMillis()
        val result = f
        val endTime = System.currentTimeMillis()

        val duration = endTime - startTime
        client.recordExecutionTime(metricName, duration)

        result
    }
}


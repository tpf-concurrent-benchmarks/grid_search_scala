package org.grid_search.common
package stats

import com.timgroup.statsd.{StatsDClient, NonBlockingStatsDClient}
import config.MetricsConfig

object StatsLogger {

    var client: Option[StatsDClient] = None

    def init(config: MetricsConfig): Unit = {
        client = Some(new NonBlockingStatsDClient(config.prefix, config.host, config.port))
    }

    private def withClient(f: StatsDClient => Unit): Unit = {
        client match {
            case Some(c) => f(c)
            case None => handleNotInitializedError()
        }
    }

    private def handleNotInitializedError(): Unit = {
        println("StatsLogger not initialized")
    }

    def increment(metric: String): Unit = {
        withClient(_.increment(metric))
    }

    def decrement(metric: String): Unit = {
        withClient(_.decrement(metric))
    }

    def gauge(metric: String, value: Long): Unit = {
        withClient(_.gauge(metric, value))
    }

    def runAndMeasure[T](metric: String, f: => T): T = {
        val startTime = System.currentTimeMillis()
        val result = f
        val endTime = System.currentTimeMillis()

        val duration = endTime - startTime
        client.recordExecutionTime(metric, duration)

        result
    }
}
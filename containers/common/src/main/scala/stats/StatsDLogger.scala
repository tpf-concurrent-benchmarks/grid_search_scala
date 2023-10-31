package org.grid_search.common
package stats

import com.timgroup.statsd.{StatsDClient, NonBlockingStatsDClient}
import config.MetricsConfig

object StatsDLogger extends MetricsLogger {

    var client: Option[StatsDClient] = None

    def init(config: MetricsConfig): Unit = {
        println(s"Initializing StatsDLogger with prefix: ${config.prefix} in ${config.host}:${config.port}")
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

    override def increment(metric: String): Unit = {
        withClient(_.increment(metric))
    }

    override def decrement(metric: String): Unit = {
        withClient(_.decrement(metric))
    }

    override def gauge(metric: String, value: Long): Unit = {
        withClient(_.gauge(metric, value))
    }

    override def runAndMeasure[T](metric: String, f: => T): T = {
        val startTime = System.currentTimeMillis()
        val result = f
        val endTime = System.currentTimeMillis()

        val duration = endTime - startTime
        withClient(_.recordExecutionTime(metric, duration.longValue()))

        result
    }
}
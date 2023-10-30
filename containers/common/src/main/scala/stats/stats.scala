package org.grid_search.common
package metrics

import com.timgroup.statsd.{StatsDClient, NonBlockingStatsDClient}
import config.MetricsConfig

object StatsLogger {
  
  var client: Option[StatsDClient] = None

  def init(config: MetricsConfig) = {
    client = Some(new NonBlockingStatsDClient(config.prefix, config.host, config.port))
  }

  private def withClient(f: StatsDClient => Unit): Unit = {
    client match {
      case Some(c) => f(c)
      case None => notInitializedHandler()
    }
  }

  private def notInitializedHandler() = {
    println("StatsLogger not initialized")
  }

  def increment(metric: String) = {
    withClient(_.increment(metric))
  }

  def decrement(metric: String) = {
    withClient(_.decrement(metric))
  }

  def gauge(metric: String, value: Long) = {
    withClient(_.gauge(metric, value))
  }
}
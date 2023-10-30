package org.grid_search.common
package metrics

import com.timgroup.statsd.{StatsDClient, NonBlockingStatsDClient}
import config.MetricsConfig

object StatsLogger {

  def apply( config: MetricsConfig ) = {
    val host = config.host
    val port = config.port
  }
}


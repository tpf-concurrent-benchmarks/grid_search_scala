package org.grid_search.common
package config

import config.{MiddlewareConfig, QueuesConfig}

trait ConfigReader {
  def getMiddlewareConfig: MiddlewareConfig

  def getQueuesConfig: QueuesConfig

  def getMetricsConfig: MetricsConfig

  def getWorkConfig: WorkConfig
}

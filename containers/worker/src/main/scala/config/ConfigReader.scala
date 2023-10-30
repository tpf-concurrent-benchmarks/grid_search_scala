package org.grid_search.worker
package config


trait ConfigReader {
  def getMiddlewareConfig: MiddlewareConfig

  def getQueuesConfig: QueuesConfig

  def getMetricsConfig: MetricsConfig
}

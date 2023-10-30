package org.grid_search.manager
package config


trait ConfigReader {
  def getMiddlewareConfig: MiddlewareConfig

  def getQueuesConfig: QueuesConfig

  def getMetricsConfig: MetricsConfig

  def getWorkConfig: WorkConfig
}

package org.grid_search.common
package config

import config.{MiddlewareConfig, QueuesConfig}

import com.typesafe.config.{Config, ConfigFactory}

case class FileConfigReader(filePath: String) extends ConfigReader {
    val config: Config = ConfigFactory.load(filePath)

    override def getMiddlewareConfig: MiddlewareConfig = {
        val middlewareHost = config.getString("middleware.host")
        val middlewarePort = config.getInt("middleware.port")
        val middlewareUser = config.getString("middleware.user")
        val middlewarePassword = config.getString("middleware.password")

        MiddlewareConfig(
            middlewareHost,
            middlewarePort,
            middlewareUser,
            middlewarePassword)
    }

    override def getQueuesConfig: QueuesConfig = {
        val workQueue = config.getString("middleware.queues.work")
        val resultsQueue = config.getString("middleware.queues.results")

        QueuesConfig(
            workQueue,
            resultsQueue)
    }

    override def getMetricsConfig: MetricsConfig = {
        val metricsAddress = config.getString("metrics.address")
        val metricsPort = config.getInt("metrics.port")
        
        val metricsPrefix = if (config.hasPath("metrics.prefix")) {
            config.getString("metrics.prefix")
        } else {
            "grid_search"
        }

        MetricsConfig(
            metricsAddress,
            metricsPort,
            metricsPrefix)
    }

    override def getWorkConfig: WorkConfig = {
        val workPath = config.getString("data.path")

        WorkConfig(
            workPath)
    }
}

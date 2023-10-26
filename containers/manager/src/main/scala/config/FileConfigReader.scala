package org.grid_search.manager
package config

import com.typesafe.config.{Config, ConfigFactory}

case class FileConfigReader(filePath: String = "manager.conf") extends ConfigReader {
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
        val resultQueue = config.getString("middleware.queues.result")

        QueuesConfig(
            workQueue,
            resultQueue)
    }

    override def getMetricsConfig: MetricsConfig = {
        val metricsAddress = config.getString("metrics.address")
        val metricsPort = config.getInt("metrics.port")

        MetricsConfig(
            metricsAddress,
            metricsPort)
    }
}

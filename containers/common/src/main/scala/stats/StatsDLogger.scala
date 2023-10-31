package org.grid_search.common
package stats

import config.MetricsConfig
import java.net.{DatagramSocket, DatagramPacket, InetAddress}

object StatsDLogger extends MetricsLogger {

    // udp socket
    var socket: Option[DatagramSocket] = None
    var cfg = MetricsConfig("", 0, "")

    def init(config: MetricsConfig): Unit = {
        println(s"Initializing StatsDLogger with prefix: ${config.prefix} in ${config.host}:${config.port}")
        socket = Some(new DatagramSocket())
        cfg = config
    }

    private def withSocket(f: DatagramSocket => Unit): Unit = {
        socket match {
            case Some(s) => f(s)
            case None => handleNotInitializedError()
        }
    }

    private def send(message: String): Unit = {
        val _message = s"${cfg.prefix}.${message}"
        val addr = InetAddress.getByName(cfg.host)
        val packet = new DatagramPacket(_message.getBytes, _message.length, addr, cfg.port)
        withSocket(_.send(packet))
    }

    private def handleNotInitializedError(): Unit = {
        println("StatsLogger not initialized")
    }

    override def increment(metric: String): Unit = {
        send(s"${metric}:1|c")
    }

    override def decrement(metric: String): Unit = {
        send(s"${metric}:-1|c")
    }

    override def gauge(metric: String, value: Long): Unit = {
        send(s"${metric}:${value}|g")
    }

    def timer(metric: String, value: Long): Unit = {
        send(s"${metric}:${value}|ms")
    }

    override def runAndMeasure[T](metric: String, f: => T): T = {
        val startTime = System.currentTimeMillis()
        val result = f
        val endTime = System.currentTimeMillis()

        val duration = endTime - startTime
        timer(metric, duration)

        result
    }
}
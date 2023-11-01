package org.grid_search.common
package middleware

import com.newmotion.akka.rabbitmq
import com.rabbitmq.client.{Channel, Connection}
import config.MiddlewareConfig

object Rabbit {
    private val maxConnectionAttempts = 10

    def apply(config: MiddlewareConfig): Rabbit = {
        val factory: rabbitmq.ConnectionFactory = new rabbitmq.ConnectionFactory()

        factory.setHost(config.host)
        factory.setPort(config.port)
        factory.setUsername(config.user)
        factory.setPassword(config.password)

        var rabbit: Option[Rabbit] = None
        Range.inclusive(1, maxConnectionAttempts).takeWhile(attempt => {
            try {
                val connection: rabbitmq.Connection = factory.newConnection()
                rabbit = Some(new Rabbit(connection))
                false
            } catch {
                case _: Exception =>
                    println(s"Failed to connect to RabbitMQ. Attempt $attempt/$maxConnectionAttempts")
                    Thread.sleep(5000)
                    true
            }
        })
        rabbit match {
            case Some(r) => r
            case None => throw new Exception("Failed to connect to RabbitMQ")
        }
    }
}

class Rabbit(connection: rabbitmq.Connection) extends MessageQueue {
    private val channel: rabbitmq.Channel = connection.createChannel()
    private var declaredQueues: Set[String] = Set[String]()

    private def declareQueue(queue: String): Unit = {
        if (!declaredQueues.contains(queue)) {
            channel.queueDeclare(queue, false, false, false, null)
            declaredQueues += queue
        }
    }

    override def produce(queue: String, message: Array[Byte]): Unit = {
        declareQueue(queue)
        channel.basicPublish("", queue, null, message)
    }

    override def setConsumer(queue: String, callback: Callback): Unit = {
        declareQueue(queue)

        val consumer = new rabbitmq.DefaultConsumer(channel) {
            override def handleDelivery(
                                           consumerTag: String,
                                           envelope: rabbitmq.Envelope,
                                           properties: rabbitmq.BasicProperties,
                                           body: Array[Byte]
                                       ): Unit = {
                if (callback(body)) {
                    channel.basicAck(envelope.getDeliveryTag, false)
                } else {
                    channel.basicNack(envelope.getDeliveryTag, false, true)
                }
            }
        }
        channel.basicConsume(queue, false, consumer)
    }

    override def publish(eventName: String, message: String): Unit = {
        throw new NotImplementedError()
    }

    override def subscribe(eventName: String, callback: Callback): Unit = {
        throw new NotImplementedError()
    }

    override def close(): Unit = {
        channel.close()
        connection.close()
    }
}

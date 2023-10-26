package org.grid_search.manager
import com.typesafe.config.ConfigFactory
import com.newmotion.akka.rabbitmq
import config.{FileConfigReader, QueuesConfig}



case class Message(data: List[List[Int]]) derives upickle.default.ReadWriter

def loadConfig(): Unit = {
    val config = ConfigFactory.load("manager.conf")
    val rabbitmqAddress = config.getString("rabbitmq.address")
    val rabbitmqPort = config.getInt("rabbitmq.port")
    val rabbitmqUser = config.getString("rabbitmq.user")
    val rabbitmqPassword = config.getString("rabbitmq.password")

    println("rabbitmq.address: " + rabbitmqAddress)
    println("rabbitmq.port: " + rabbitmqPort)
    println("rabbitmq.user: " + rabbitmqUser)
    println("rabbitmq.password: " + rabbitmqPassword)

}

@main
def main(): Unit = {
    val config = FileConfigReader()
    val rabbitMq = middleware.Rabbit(config.getMiddlewareConfig)
    val queues = config.getQueuesConfig

    rabbitMq.produce(queues.work, "Hello World!".getBytes("UTF-8"))
    rabbitMq.setConsumer(queues.work, (message) => {
        println("Received message: " + new String(message, "UTF-8"))
        true
    })

    rabbitMq.startConsuming()
}
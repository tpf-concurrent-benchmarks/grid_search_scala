package org.grid_search.manager
import com.typesafe.config.ConfigFactory
import com.newmotion.akka.rabbitmq
import config.{FileConfigReader, QueuesConfig}



case class Message(data: List[List[Int]]) derives upickle.default.ReadWriter

def getConfigReader(): FileConfigReader = {
    if (System.getenv("LOCAL") == "true") {
        FileConfigReader("manager_local.conf")
    } else {
        FileConfigReader()
    }
}

@main
def main(): Unit = {
    val config = getConfigReader()
    val rabbitMq = middleware.Rabbit(config.getMiddlewareConfig)
    val queues = config.getQueuesConfig

    rabbitMq.produce(queues.work, "Hello World!".getBytes("UTF-8"))
    rabbitMq.setConsumer(queues.work, (message) => {
        println("Received message: " + new String(message, "UTF-8"))
        true
    })

    rabbitMq.startConsuming()
}
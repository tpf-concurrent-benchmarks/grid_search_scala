package org.grid_search.manager
import com.typesafe.config.ConfigFactory
import com.newmotion.akka.rabbitmq._



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

  loadConfig()

  val factory = new ConnectionFactory()

  factory.setHost("gs_scala_rabbitmq")
  factory.setPort(5672)
  factory.setUsername("guest")
  factory.setPassword("guest")

  println("Connecting to RabbitMQ "+factory.getHost+":"+factory.getPort)

  val connection: Connection = factory.newConnection()

  val channel: Channel = connection.createChannel()

  channel.queueDeclare("work", false, false, false, null)
  channel.basicPublish("", "work", null, "{msg: \"Hello world!\"}".getBytes())

  println(" [x] Sent 'Hello World!'")

  Thread.sleep(30000)
}
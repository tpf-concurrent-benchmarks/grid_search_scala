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
  println("Hello world!")

  loadConfig()

  val factory = new ConnectionFactory()
  // connect to address "template_rabbitmq"
  factory.setHost("template_rabbitmq")
  factory.setPort(5672)
  factory.setUsername("guest")
  factory.setPassword("guest")

  val connection: Connection = factory.newConnection()

  val channel: Channel = connection.createChannel()

  // create a queue "work"
  channel.queueDeclare("work", false, false, false, null)
  // send "{msg: "Hello world!"}" to queue "work"
  channel.basicPublish("", "work", null, "{msg: \"Hello world!\"}".getBytes())

  Thread.sleep(30000)
}
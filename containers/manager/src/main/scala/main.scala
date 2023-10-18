package org.grid_search.manager
import com.typesafe.config.ConfigFactory
import com.newmotion.akka.rabbitmq



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

  loadConfig()

  val factory = new rabbitmq.ConnectionFactory()

  factory.setHost("gs_scala_rabbitmq")
  factory.setPort(5672)
  factory.setUsername("guest")
  factory.setPassword("guest")

  println("Connecting to RabbitMQ "+factory.getHost+":"+factory.getPort)

  val connection = factory.newConnection()

  val channel= connection.createChannel()

  channel.queueDeclare("work", false, false, false, null)

  val message = Message(List(List(4, 2, 4), List(1, 9, 3), List(5, 6, 7)))

  channel.basicPublish("", "work", null, upickle.default.write(message).getBytes("UTF-8"))

  println(" [x] Sent 'Hello World!'")

  val consumer = new rabbitmq.DefaultConsumer(channel) {
    override def handleDelivery(
      consumerTag: String,
      envelope: rabbitmq.Envelope,
      properties: rabbitmq.BasicProperties,
      body: Array[Byte]
    ): Unit = {
      val message = new String(body, "UTF-8")

      val msg = upickle.default.read[Message](message)
      println("Received message: ")
      for (row <- msg.data) {
        println("\tStart: " + row(0) + " End: " + row(1) + " Step: " + row(2))
      }


    }
  }
  channel.basicConsume("work", true, consumer)

  println("Waiting for messages. To exit press ENTER")

  try {
    scala.io.StdIn.readLine()
  } catch {
    case _: InterruptedException =>
      println("Shutting down...")

      channel.close()
      connection.close()
  }

}
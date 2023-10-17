package org.grid_search.manager
import com.typesafe.config.ConfigFactory

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
}
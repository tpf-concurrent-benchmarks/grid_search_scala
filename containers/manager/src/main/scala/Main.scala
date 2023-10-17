
import com.rabbitmq.client.{Connection, ConnectionFactory, Channel}

def getConnection: Try[Connection] = Try {

  val connectionFactory: ConnectionFactory = new ConnectionFactory()

  connectionFactory.setUsername("admin")

  connectionFactory.setPassword("password")

  connectionFactory.newConnection()
}

def initializeConnection(): Try[Channel] = {

  // Get the Connection

  val rabbitMQConnection: Try[Connection] = RabbitMQConnection.getConnection  

  // Create Channel if connection is successfully established

  val connectionChannel: Try[Channel] = rabbitMQConnection match {

    case Failure(exception) =>

      logger.error("Could not establish connection with RabbitMQ")

      throw new Exception(exception.getMessage)

    case Success(connection) => Try(connection.createChannel())

  }

  connectionChannel
}

@main def hello: Unit =

  println( "hello" )



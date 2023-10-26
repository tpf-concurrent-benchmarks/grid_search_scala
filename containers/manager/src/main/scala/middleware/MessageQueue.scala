package org.grid_search.manager
package middleware

// Anonymous function that takes a message and returns a boolean
// indicating whether the message should be acked or not
type Callback = (Array[Byte]) => Boolean

trait MessageQueue {
    // @throws(classOf[IOException])
    def produce(queue: String, message: Array[Byte]): Unit

    // @throws(classOf[IOException])
    def setConsumer(queue: String, callback: Callback): Unit

    // @throws(classOf[IOException])
    def publish(eventName: String, message: String): Unit

    // @throws(classOf[IOException])
    def subscribe(eventName: String, callback: Callback): Unit

    // @throws(classOf[IOException], classOf[TimeoutException])
    def close(): Unit

    // @throws(classOf[IOException], classOf[TimeoutException])
    def startConsuming(): Unit = {
        println("Waiting for messages. To exit press ENTER")

        try {
            scala.io.StdIn.readLine()
        } catch {
            case _: InterruptedException =>
                println("Shutting down...")
                close()
        }
    }
}

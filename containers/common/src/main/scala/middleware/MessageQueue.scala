package org.grid_search.common
package middleware

import scala.concurrent.Promise

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
        try {
            val p = Promise[Unit]()
            scala.concurrent.Await.result(p.future, scala.concurrent.duration.Duration.Inf)
        } catch {
            case _: InterruptedException =>
                println("Shutting down...")
                close()
        }
    }
}

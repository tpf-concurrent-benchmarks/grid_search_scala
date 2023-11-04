package org.grid_search.common
package transformer

import middleware.MessageQueue

import scala.concurrent.Promise

trait BasicTransformer {
    val inputQueue: String
    val outputQueue: String
    val endEvent: String

    def transform(input: Array[Byte]): Array[Byte]

    def start(middleware: MessageQueue): Unit = {
        middleware.setConsumer(inputQueue, input => {
            val output = transform(input)
            middleware.produce(outputQueue, output)
            true
        })
        val stopPromise = Promise[Unit]()

        middleware.subscribe(endEvent, end => {
            println(s"Received end: ${new String(end, "UTF-8")}")
            stopPromise.success(())
            true
        })

        middleware.startConsuming(Some(stopPromise.future))
        middleware.close()
    }
}

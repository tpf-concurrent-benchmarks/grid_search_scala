package org.grid_search.worker

import org.grid_search.common.config.QueuesConfig
import org.grid_search.common.marshalling.{WorkParser, parseResult}
import org.grid_search.common.stats.getLogger
import org.grid_search.common.transformer.BasicTransformer
import org.grid_search.common.work_split.Result

object Worker {
    def apply(queuesConfig: QueuesConfig): Worker = {
        Worker(queuesConfig.work, queuesConfig.results, queuesConfig.endEvent)
    }
}

case class Worker(inputQueue: String, outputQueue: String, endEvent: String) extends BasicTransformer {
    private var resultsCount = 0

    override def transform(input: Array[Byte]): Array[Byte] = {
        val work = WorkParser.unParse(new String(input, "UTF-8"))

        val result: Result = getLogger.runAndMeasure("work_time", work.calculateFor(griewankFunc))
        val resultString = parseResult(result)

        resultsCount += 1
        getLogger.increment("results_produced")
        if (resultsCount == 1000) {
            println("Produced 1000 results")
            resultsCount = 0
        }
        resultString.getBytes("UTF-8")
    }
}

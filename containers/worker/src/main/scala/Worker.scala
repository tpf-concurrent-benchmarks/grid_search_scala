package org.grid_search.worker

import org.grid_search.common.config.QueuesConfig
import org.grid_search.common.marshalling.{parseResult, resultW, workRW}
import org.grid_search.common.stats.getLogger
import org.grid_search.common.transformer.BasicTransformer
import org.grid_search.common.work_split.{Result, Work}
import upickle.default

object Worker {
    def apply(queuesConfig: QueuesConfig): Worker = {
        Worker(queuesConfig.work, queuesConfig.results, queuesConfig.endEvent)
    }
}

case class Worker(inputQueue: String, outputQueue: String, endEvent: String) extends BasicTransformer {
    override type InputType = Work
    override type OutputType = Result
    override implicit val reader: default.ReadWriter[Work] = workRW
    override implicit val writer: default.Writer[Result] = resultW

    private var resultsCount = 0

    override def transform(input: Work): Result = {
        val result: Result = getLogger.runAndMeasure("work_time", input.calculateFor(griewankFunc))

        resultsCount += 1
        getLogger.increment("results_produced")
        if (resultsCount == 1000) {
            println("Produced 1000 results")
            resultsCount = 0
        }
        result
    }
}

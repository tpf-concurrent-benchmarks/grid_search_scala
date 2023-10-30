package org.grid_search.common
package work_split

import scala.collection.IndexedSeqView
import scala.util.control.Breaks._

object WorkPlan {
    def apply(intervals: List[CircularIterator[Interval]], aggregator: Aggregator): WorkPlan = {
        new WorkPlan(intervals, aggregator)
    }
}

case class WorkPlan(intervals: List[CircularIterator[Interval]], aggregator: Aggregator) extends Iterator[Work] {
    private val intervalsPerIterator = intervals.map(interval => BigInt(interval.size()))
    private val size: BigInt = {
        intervalsPerIterator.foldLeft(BigInt(1))((a, b) => {
            a * b
        })
    }
    private var currentValues = intervals.map(_.next)
    private var positions = List.fill(intervals.length)(0)
    private var generatedAmount = 0

    override def next(): Work = {
        val result = Work(currentValues, aggregator)
        update()
        result
    }

    override def hasNext: Boolean = generatedAmount < size

    def update(): Unit = {
        generatedAmount += 1
        intervals.indices.takeWhile(i => {
            positions = positions.updated(i, positions(i) + 1)
            currentValues = currentValues.updated(i, intervals(i).next)

            if (positions(i) < intervalsPerIterator(i)) {
                false
            } else {
                positions = positions.updated(i, 0)
                true
            }
        })
    }
}

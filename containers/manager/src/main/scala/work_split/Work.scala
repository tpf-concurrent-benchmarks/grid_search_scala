package org.grid_search.manager
package work_split

import scala.collection.IndexedSeqView

object Work {
    def apply(intervals: List[Interval]): Work = new Work(intervals)
}

case class Work(intervals: List[Interval]) {
    val size: Int = calcSize()
    var current: List[Double] = intervals.map(i => i.start)

    private def calcSize(): Int = {
        intervals.map(_.size).product
    }

    def next(precision: Option[Int] = None): List[Double] = {
        val result = current
        for (i <- current.indices.reverse) {
            val start = intervals(i).start
            val end = intervals(i).end
            val step = intervals(i).step

            if (current(i) + step < end) {
                current = current.updated(i, roundNumber(current(i) + step, precision))
                return result
            } else {
                current = current.updated(i, start)
            }
        }
        result
    }

    def unfold(precision: Option[Int] = None): IndexedSeqView[List[Double]] = {
        for (_ <- (0 until size).view) yield {
            next(precision)
        }
    }

    private def calcAmountOfMissingPartitions(minBatches: Int, partitionsPerInterval: List[Int]): Int = {
        math.ceil(minBatches / partitionsPerInterval.product).toInt
    }

    private def calcPartitionsPerInterval(minBatches: Int): List[Int] = {
        var currPartitionsPerInterval = List.fill(intervals.length)(1)

        for (intervalPos <- intervals.indices) {
            val missingPartitions = calcAmountOfMissingPartitions(minBatches, currPartitionsPerInterval)
            val elements = intervals(intervalPos).size

            if (elements > missingPartitions) {
                currPartitionsPerInterval = currPartitionsPerInterval.updated(intervalPos, missingPartitions)
                return currPartitionsPerInterval
            } else {
                currPartitionsPerInterval = currPartitionsPerInterval.updated(intervalPos, elements)
            }
        }
        currPartitionsPerInterval
    }

    def split(maxChunkSize: Int, precision: Option[Int] = None): IndexedSeqView[Work] = {
        val minBatches = Math.floor(size / maxChunkSize) + 1
        val partitionsPerInterval = calcPartitionsPerInterval(minBatches.toInt)

        val listOfIterator = for (intervalPos <- intervals.indices) yield {
            val iterator = intervals(intervalPos).split(partitionsPerInterval(intervalPos), precision)
            CircularIterator(iterator, partitionsPerInterval(intervalPos), precision)
        }

        WorkPlan(listOfIterator.toList, partitionsPerInterval).calcCartesianProduct()
    }
}

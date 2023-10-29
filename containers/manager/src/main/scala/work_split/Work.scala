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

    def unfold(precision: Option[Int] = None): WorkIterator = {
        WorkIterator(this, precision)
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

    def split(maxChunkSize: Int, precision: Option[Int] = None): Iterator[Work] = {
        val minBatches = Math.floor(size / maxChunkSize) + 1
        val partitionsPerInterval = calcPartitionsPerInterval(minBatches.toInt)


        val listOfIterator = for (intervalPos <- intervals.indices) yield {
            val iterator = intervals(intervalPos).split(partitionsPerInterval(intervalPos), precision)
            CircularIterator(iterator, partitionsPerInterval(intervalPos), precision)
        }

        WorkPlan(listOfIterator.toList).iterator
    }
}
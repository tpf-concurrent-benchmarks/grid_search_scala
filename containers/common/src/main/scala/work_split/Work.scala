package org.grid_search.common
package work_split


type Params = Seq[Double]


object Work {
    def apply(intervals: List[Interval], aggregator: Aggregator = Aggregator.Mean): Work = new Work(intervals, aggregator)
}

case class Work(intervals: List[Interval], aggregator: Aggregator) {
    val size: BigInt = calcSize()

    private def calcSize(): BigInt = {
        intervals.map(i => BigInt(i.size)).product
    }

    def unfold(precision: Option[Int] = None): WorkIterator = {
        WorkIterator(this, precision)
    }

    private def calcAmountOfMissingPartitions(minBatches: Int, partitionsPerInterval: List[Int]): Int = {
        math.ceil(minBatches.toDouble / partitionsPerInterval.product).toInt
    }

    private def calcPartitionsPerInterval(minBatches: Int): List[Int] = {
        var currPartitionsPerInterval = List.fill(intervals.length)(1)

        intervals.indices.takeWhile( intervalPos => {
            val missingPartitions = calcAmountOfMissingPartitions(minBatches, currPartitionsPerInterval)
            val elements = intervals(intervalPos).size

            if (elements > missingPartitions) {
                currPartitionsPerInterval = currPartitionsPerInterval.updated(intervalPos, missingPartitions)
                false
            } else {
                currPartitionsPerInterval = currPartitionsPerInterval.updated(intervalPos, elements)
                true
            }
        })
        currPartitionsPerInterval
    }

    def split(maxChunkSize: Int, precision: Option[Int] = None): Iterator[Work] = {
        val minBatches = Math.ceil(size.toDouble / maxChunkSize)
        val partitionsPerInterval = calcPartitionsPerInterval(minBatches.toInt)


        val listOfIterator = for (intervalPos <- intervals.indices) yield {
            val iterator = intervals(intervalPos).split(partitionsPerInterval(intervalPos), precision)
            CircularIterator(iterator, partitionsPerInterval(intervalPos), precision)
        }

        WorkPlan(listOfIterator.toList,aggregator).iterator
    }

    def calculateFor(func: Params => Double): Result = {
        val results: Iterator[(Params, Double)] = unfold().map(params => {
            (params, func(params))
        })
        aggregate(aggregator, results)
    }
}

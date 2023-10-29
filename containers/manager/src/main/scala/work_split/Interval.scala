package org.grid_search.manager
package work_split

object Interval {
    def apply(start: Double, end: Double, step: Double): Interval = {
        assertIntervalIsCorrect(start, end, step)
        new Interval(start, end, step)
    }
}

case class Interval(start: Double, end: Double, step: Double) {

    val size: Int = Math.ceil((end - start) / step).toInt

    def unfold(precision: Option[Int] = None): IntervalIterator = {
        IntervalIterator(start, end, step, precision)
    }

    def splitEvenly(amountOfSubIntervals: Int, precision: Option[Int] = None): IndexedSeq[Interval] = {
        Range.inclusive(0, amountOfSubIntervals - 1).map(pos => {
            val subStart = roundNumber(start + pos * Math.floor(size / amountOfSubIntervals) * step, precision)
            val subEnd = roundNumber(start + (pos + 1) * Math.floor(size / amountOfSubIntervals) * step, precision)
            Interval(subStart, subEnd, step)
        })
    }

    def split(amountOfSubIntervals: Int, precision: Option[Int] = None): IndexedSeq[Interval] = {
        if (size % amountOfSubIntervals == 0) {
            return splitEvenly(amountOfSubIntervals, precision)
        } else if (size < amountOfSubIntervals) {
            throw new IllegalArgumentException("Cannot split interval of size " + size + " into " + amountOfSubIntervals + " subintervals")
        }

        val maxElemsPerInterval = Math.ceil(size / amountOfSubIntervals.toDouble).toInt
        val amountOfSubIntervalsOfFullSize = Math.floor((size - amountOfSubIntervals) / (maxElemsPerInterval - 1).toDouble).toInt

        var subEnd = 0.0
        Range.inclusive(0, amountOfSubIntervalsOfFullSize - 1).map(pos => {
            val subStart = roundNumber(start + pos * maxElemsPerInterval * step, precision)
            subEnd = roundNumber(Math.min(end, subStart + maxElemsPerInterval * step), precision)
            Interval(subStart, subEnd, step)
        }) ++ {
            val remainingInterval = Interval(subEnd, end, step)
            val remainingAmountOfSubIntervals = amountOfSubIntervals - amountOfSubIntervalsOfFullSize
            remainingInterval.split(remainingAmountOfSubIntervals, precision)
        }

    }
}

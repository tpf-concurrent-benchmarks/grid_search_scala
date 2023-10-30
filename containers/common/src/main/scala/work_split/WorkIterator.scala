package org.grid_search.common
package work_split

object WorkIterator {
    def apply(work: Work, precision: Option[Int]): WorkIterator = {
        new WorkIterator(work.intervals, precision)
    }
}

case class WorkIterator(intervals: Seq[Interval], precision: Option[Int] = None) extends Iterator[List[Double]] {
    private var current = intervals.map(int => roundNumber(int.start, precision)).toList
    private val size = intervals.map(_.size).product
    private var generatedAmount = 0

    def hasNext: Boolean = generatedAmount < size

    def next(): List[Double] = {
        val result = current
        generatedAmount += 1
        current.zipWithIndex.reverse.takeWhile({
            case (x, i) =>
                val start = roundNumber(intervals(i).start, precision)
                val end = roundNumber(intervals(i).end, precision)
                val step = roundNumber(intervals(i).step, precision)

                if (x + step < end) {
                    current = current.updated(i, roundNumber(x + step, precision))
                    false
                } else {
                    current = current.updated(i, start)
                    true
                }
        })
        result
    }
}





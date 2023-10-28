package org.grid_search.manager
package work_split

object IntervalIterator {
    def apply(start: Double, end: Double, step: Double, precision: Option[Int] = None): IntervalIterator = {
        assertIntervalIsCorrect(start, end, step)
        new IntervalIterator(start, end, step, precision)
    }
}

case class IntervalIterator(start: Double, end: Double, step: Double, precision: Option[Int] = None) extends Iterator[Double] {
    private var current = start
    def hasNext: Boolean = current < end
    def next: Double = {
        val result = current
        println(s"Returning $result from $start to $end with step $step")
        current = roundNumber(current + step, precision)
        result
    }
}

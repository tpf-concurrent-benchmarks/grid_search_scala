package org.grid_search.manager
package work_split

object CircularIterator {
    def apply(interval: Interval, precision: Option[Int] = None): CircularIterator = {
        if (interval.size <= 0) {
            throw new IllegalArgumentException("Size must be positive")
        }
        new CircularIterator(interval, precision)
    }
}

case class CircularIterator(interval: Interval, precision: Option[Int] = None) extends Iterator[Double] {
    private var position = 0
    private val it = interval.unfold(precision)
    private var calculatedValues = List[Double]()

    override def toList: List[Double] = {
        val res = List[Double]()
        while (hasNext) {
            res.appended(next)
        }
        res
    }

    def hasNext = true
    def next: Double = {
        if (position == interval.size) {
            position = 0
        }
        if (position == calculatedValues.size) {
            calculatedValues = calculatedValues.appended(it.next)
        }
        val value = calculatedValues(position)
        position += 1
        value
    }
}

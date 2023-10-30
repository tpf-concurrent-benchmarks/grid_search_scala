package org.grid_search.worker
package work_split

object CircularIterator {
    def apply[T](it: Iterator[T], len: Int): CircularIterator[T] = {
        if (len <= 0) {
            throw new IllegalArgumentException("Size must be positive")
        }
        new CircularIterator(it, len, None)
    }

    def apply[T](it: Iterator[T], len: Int, precision: Int): CircularIterator[T] = {
        if (len <= 0) {
            throw new IllegalArgumentException("Size must be positive")
        }
        new CircularIterator(it, len, Some(precision))
    }
}

case class CircularIterator[T](it: Iterator[T], len: Int, precision: Option[Int] = None) extends Iterator[T] {
    private var position = 0
    private var calculatedValues = List[T]()

    override def size(): Int = len

    override def toList: List[T] = {
        val res = List[T]()
        while (hasNext) {
            res.appended(next)
        }
        res
    }

    def hasNext = true
    def next: T = {
        if (position == len) {
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

package org.grid_search.manager
package work_split

object CircularIterator {
    def apply(intervals: IndexedSeq[Interval], len: Int, precision: Option[Int] = None): CircularIterator = {
        if (intervals.size <= 0) {
            throw new IllegalArgumentException("Size must be positive")
        }
        new CircularIterator(intervals, len, precision)
    }
}

case class CircularIterator(intervals: IndexedSeq[Interval], len: Int, precision: Option[Int] = None) extends Iterator[Interval] {
    private var position = 0

    override def size(): Int = len

    override def toList: List[Interval] = {
        val res = List[Interval]()
        while (hasNext) {
            res.appended(next)
        }
        res
    }

    def hasNext = true
    def next: Interval = {
        if (position == len) {
            position = 0
        }
        val value = intervals(position)
        position += 1
        value
    }
}

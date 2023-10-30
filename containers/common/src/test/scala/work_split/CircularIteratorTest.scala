package org.grid_search.common
package work_split

import org.scalatest.funsuite.AnyFunSuite

class CircularIteratorTest extends AnyFunSuite {
    test("CircularIterator.iterator of empty interval throws exception") {
        assertThrows[IllegalArgumentException] {
            CircularIterator(Interval(0, 0, 1).unfold(), 0)
        }
    }

    test("CircularIterator.iterator of single element returns single element once") {
        val it = CircularIterator(Interval(0, 1, 1).unfold(), 1)
        assert(it.hasNext)
        assert(it.next == 0)
        assert(it.hasNext)
    }


    test("CircularIterator.iterator of single element returns single element twice") {
        val it = CircularIterator(Interval(0, 1, 1).unfold(), 1)
        assert(it.hasNext)
        assert(it.next == 0)
        assert(it.hasNext)
        assert(it.next == 0)
    }

    test("CircularIterator.iterator of multiple elements returns elements in order") {
        val it = CircularIterator(Interval(0, 2, 1).unfold(), 2)
        assert(it.hasNext)
        assert(it.next == 0)
        assert(it.hasNext)
        assert(it.next == 1)
        assert(it.hasNext)
        assert(it.next == 0)
        assert(it.hasNext)
        assert(it.next == 1)
    }

    test("CirculerIterator.iterator of Doubles works") {
        val it = CircularIterator(Interval(0.0, 1.3, 0.5).unfold(), 3)
        assert(it.hasNext)
        assert(it.next == 0.0)
        assert(it.hasNext)
        assert(it.next == 0.5)
        assert(it.hasNext)
        assert(it.next == 1.0)
        assert(it.hasNext)
        assert(it.next == 0.0)
        assert(it.hasNext)
        assert(it.next == 0.5)
        assert(it.hasNext)
        assert(it.next == 1.0)
    }

    test("CircularIterator.iterator works with Interval splitting") {
        val it = CircularIterator(Interval(0, 10, 1).split(2), 2, 2)
        assert(it.hasNext)
        assert(it.next == Interval(0, 5, 1))
        assert(it.hasNext)
        assert(it.next == Interval(5, 10, 1))
        assert(it.hasNext)
        assert(it.next == Interval(0, 5, 1))
    }

}

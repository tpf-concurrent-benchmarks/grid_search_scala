package org.grid_search.common
package work_split

import org.scalatest.funsuite.AnyFunSuite

class IntervalIteratorTest extends AnyFunSuite {
    test("IntervalIterator.iterator of 1 element works") {
        val it = IntervalIterator(0, 1, 1)
        assert(it.hasNext)
        assert(it.next() == 0)
        assert(!it.hasNext)
    }

    test("IntervalIterator.iterator of many elements works") {
        val it = IntervalIterator(0, 5, 1)
        val expectedValues = List(0, 1, 2, 3, 4)
        assert(it.toList == expectedValues)
    }
    
    test("IntervalIterator.iterator of negative elements works") {
        val it = IntervalIterator(-5, 0, 1)
        val expectedValues = List(-5, -4, -3, -2, -1)
        assert(it.toList == expectedValues)
    }
    
    test("Interval.iterator with step > 1 works") {
        val it = IntervalIterator(0, 5, 2)
        val expectedValues = List(0, 2, 4)
        assert(it.toList == expectedValues)
    }
    
    test("Interval.iterator with 0 < step < 1 works") {
        val it = IntervalIterator(0, 5, 0.5, Some(2))
        val expectedValues = List(0, 0.5, 1, 1.5, 2, 2.5, 3, 3.5,
                                  4, 4.5)
        assert(it.toList == expectedValues)
    }

    test("Interval.iterator with 0 < step < 1 and a lot of elements works if precision is fixed") {
        val it = IntervalIterator(0.35, 0.65, 0.01, Some(2))
        val expectedValues = List(0.35, 0.36, 0.37, 0.38, 0.39, 0.4,
                                  0.41, 0.42, 0.43, 0.44, 0.45, 0.46,
                                  0.47, 0.48, 0.49, 0.5, 0.51, 0.52,
                                  0.53, 0.54, 0.55, 0.56, 0.57, 0.58,
                                  0.59, 0.6, 0.61, 0.62, 0.63, 0.64)
        assert(it.toList == expectedValues)
    }
}

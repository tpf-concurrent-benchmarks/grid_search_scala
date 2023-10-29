package org.grid_search.manager
package work_split

import org.scalatest.funsuite.AnyFunSuite

class IntervalTest extends AnyFunSuite {
    test("Interval.size of interval of ints of single element works") {
        assert(Interval(0, 1, 1).size == 1)
    }

    test("Interval.size of interval of ints of multiple elements works") {
        assert(Interval(0, 2, 1).size == 2)
    }

    test("Interval.size of interval of ints with non-unit step works") {
        assert(Interval(0, 2, 2).size == 1)
    }

    test("Interval.size of interval with non-unit step and non-zero start works") {
        assert(Interval(1, 3, 2).size == 1)
    }

    test("Interval.size of interval with decimal values works") {
        assert(Interval(0.0, 1.0, 0.1).size == 10)
    }

    test("Interval.size of interval with negative values works") {
        assert(Interval(-1, 1, 1).size == 2)
    }

    test("Interval.unfold returns iterator of the elements of the interval") {
        assert(Interval(-3.1, 5, 1.1).unfold(Some(2)).toList == List(-3.1, -2.0, -0.9, 0.2, 1.3, 2.4, 3.5, 4.6))
    }

    test("Interval.split evenly of interval of two ints works") {
        assert(Interval(0, 2, 1).split(2).toList == List(Interval(0, 1, 1), Interval(1, 2, 1)))
    }

    test("Interval.split evenly of interval of multiple ints works") {
        val interval = Interval(0, 10, 1)
        val split = interval.split(5).toList
        val expected = List(
            Interval(0, 2, 1),
            Interval(2, 4, 1),
            Interval(4, 6, 1),
            Interval(6, 8, 1),
            Interval(8, 10, 1))
        assert(split == expected)
    }

    test("Interval.split evenly of interval of two doubles works") {
        val interval = Interval(0.1, 0.3, 0.1)
        val split = interval.split(2, Some(2)).toList
        val expected = List(
            Interval(0.1, 0.2, 0.1, 2),
            Interval(0.2, 0.3, 0.1, 2))

        assert(split == expected)
    }

    test("Interval.split evenly of interval of multiple doubles works") {
        val interval = Interval(0.1, 0.5, 0.1)
        val split = interval.split(2, Some(2)).toList
        val expected = List(
            Interval(0.1, 0.3, 0.1, 2),
            Interval(0.3, 0.5, 0.1, 2))
        assert(split == expected)
    }

    test("Interval.split evenly of interval of multiple doubles and multiple partitions works") {
        val interval = Interval(0.1, 0.5, 0.1)
        val split = interval.split(4, Some(2)).toList
        val expected = List(
            Interval(0.1, 0.2, 0.1, 2),
            Interval(0.2, 0.3, 0.1, 2),
            Interval(0.3, 0.4, 0.1, 2),
            Interval(0.4, 0.5, 0.1, 2))
        assert(split == expected)
    }

    test("Interval.split not evenly of interval of three ints works") {
        val interval = Interval(0, 3, 1)
        val split = interval.split(2).toList
        val expected = List(
            Interval(0, 2, 1),
            Interval(2, 3, 1))
        assert(split == expected)
    }

    test("Interval.split not evenly of interval of multiple ints works") {
        val interval = Interval(0, 33, 1)
        val split = interval.split(7)
        val expected = List(
            Interval(0, 5, 1),
            Interval(5, 10, 1),
            Interval(10, 15, 1),
            Interval(15, 20, 1),
            Interval(20, 25, 1),
            Interval(25, 30, 1),
            Interval(30, 33, 1))

        assert(split == expected)
    }

    test("Interval.split not evenly of interval of two doubles works") {
        val interval = Interval(0.1, 0.3, 0.1)
        val split = interval.split(2, Some(2))
        val expected = List(
            Interval(0.1, 0.2, 0.1, 2),
            Interval(0.2, 0.3, 0.1, 2))

        assert(split == expected)
    }

    test("Interval.split not evenly of interval of multiple doubles works") {
        val interval = Interval(0.1, 0.16, 0.01)
        val split = interval.split(3, Some(3))
        val expected = List(
            Interval(0.1, 0.12, 0.01, 3),
            Interval(0.12, 0.14, 0.01, 3),
            Interval(0.14, 0.16, 0.01, 3))
        assert(split == expected)
    }

    test("Interval.integration test works") {
        val interval = Interval(-13.4, 49.53, 0.3)
        val split = interval.split(7, Some(3))
        val unfoldedInterval = interval.unfold(Some(3)).toList
        val unfoldedSplit = split.flatMap(_.unfold(Some(3)).toList)
        assert(unfoldedInterval == unfoldedSplit)
    }
}
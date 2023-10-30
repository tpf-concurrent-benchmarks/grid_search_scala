package org.grid_search.common
package work_split

import org.scalatest.funsuite.AnyFunSuite

class WorkTest extends AnyFunSuite {
    test("Work.size works") {
        val w = Work(List(Interval(0, 2, 1), Interval(0, 2, 1)))
        assert(w.size == 4)
    }

    test("Work.unfold works for 1D") {
        val w = Work(List(Interval(0, 2, 1)))
        val l = w.unfold().toList
        assert(l == List(List(0), List(1)))
    }

    test("Work.unfold works for 2D") {
        val w = Work(List(Interval(0, 2, 1), Interval(0, 2, 1)))
        val l = w.unfold().toList
        assert(l == List(List(0, 0), List(0, 1), List(1, 0), List(1, 1)))
    }


    test("Work.unfold is lazy") {
        val w = Work(List.fill(15)(Interval(0, 2, 1)))
        val l = w.unfold().take(2).toList
        assert(l.head == List.fill(15)(0))
        assert(l(1) == List.fill(14)(0) ::: List(1))
        assert(w.size == Math.pow(2, 15))
    }


    test("Work.split works") {
        val w = Work(List(Interval(0, 2, 1), Interval(5, 7, 1)))
        val subWorks = w.split(2).toList
        assert(subWorks.size == 2)
        assert(subWorks.head.size == 2)
        assert(subWorks.head == Work(List(Interval(0, 1, 1), Interval(5, 7, 1))))
        assert(subWorks(1).size == 2)
        assert(subWorks(1) == Work(List(Interval(1, 2, 1), Interval(5, 7, 1))))
    }

    test("Work.split works with big intervals") {
        val w = Work(List(Interval(-5.3, 5.8, 0.4, 3)))
        val unfoldedWork = w.unfold(Some(3)).toList
        val subWorks = w.split(5, Some(3)).toList
        val unfoldedSubWorks = subWorks.flatMap(_.unfold(Some(3)))

        assert(unfoldedSubWorks.size == unfoldedWork.size)
        assert(unfoldedSubWorks.sorted == unfoldedWork.sorted)
    }

    test("Work.split is lazy") {
        val w = Work(List.fill(100)(Interval(0, 15, 1)))
        val subWorks = w.split(50).take(3).toList
        assert(subWorks.size == 3)
    }
}

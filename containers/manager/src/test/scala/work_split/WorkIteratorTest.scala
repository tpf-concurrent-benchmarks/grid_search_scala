package org.grid_search.manager
package work_split

import org.scalatest.funsuite.AnyFunSuite

class WorkIteratorTest extends AnyFunSuite {
    test("WorkIterator.works") {
        val wi = new WorkIterator(List(Interval(4.7, 5.9, 0.4, 3)), Some(3)).toList
        assert(wi.length == 3)
        assert(wi == List(List(4.7), List(5.1), List(5.5)))


    }

}

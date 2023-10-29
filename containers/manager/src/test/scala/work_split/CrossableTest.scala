package org.grid_search.manager
package work_split

import org.scalatest.funsuite.AnyFunSuite

import scala.collection.immutable.List

class CrossableTest extends AnyFunSuite {
    test("Crossable.cross of empty lists returns empty list") {
        val result = List() cross List()
        assert(result.toList == List())
    }

    test("Crossable.cross of empty list and non-empty list returns empty list") {
        val result = List() cross List(1, 2, 3)
        assert(result.toList == List())
    }

    test("Crossable.cross of non-empty list and empty list returns empty list") {
        val result = List(1, 2, 3) cross List()
        assert(result.toList == List())
    }

    test("Crossable.cross of single element lists returns single element list") {
        val result = List(1) cross List(2)
        assert(result.toList == List((1, 2)))
    }

    test("Crossable.cross of multiple element lists returns correct list") {
        val result = List(1, 2) cross List(3, 4)
        assert(result.toList == List((1, 3), (1, 4), (2, 3), (2, 4)))
    }

    test("Crossable.cross works with ranges") {
        val result = Range.inclusive(1, 2) cross Range.inclusive(3, 4)
        assert(result.toList == List((1, 3), (1, 4), (2, 3), (2, 4)))
    }

    test("Crossable.cross operation is lazy") {
        val result = LazyList.continually(1) cross List(1, 2, 3)
            assert(result.take(3).toList == List((1, 1), (1, 2), (1, 3)))
    }
}

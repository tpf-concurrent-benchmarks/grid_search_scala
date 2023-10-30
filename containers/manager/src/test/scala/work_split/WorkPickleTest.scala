package org.grid_search.manager
package work_split

import org.scalatest.funsuite.AnyFunSuite

class WorkPickleTest extends AnyFunSuite {
    
    test("Can pickle and un-pickle intervals") {

      val baseInterval = Interval(0, 10, 1)

      val parsed = upickle.default.write(baseInterval)
      val unparsed = upickle.default.read[Interval](parsed)

      assert(unparsed == baseInterval)
    }

    test("Can pickle and un-pickle work") {

      val baseWork = Work(List(Interval(0, 10, 1), Interval(10, 20, 1)))

      val parsed = upickle.default.write(baseWork)
      val unparsed = upickle.default.read[Work](parsed)

      assert(unparsed == baseWork)
    }
}

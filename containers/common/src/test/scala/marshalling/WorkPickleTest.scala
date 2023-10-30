package org.grid_search.common
package marshalling

import work_split.{Interval, Work, Aggregator}
import org.scalatest.funsuite.AnyFunSuite

class WorkPickleTest extends AnyFunSuite {
    
    test("Interval pickling works") {

      val baseInterval = Interval(0, 10, 1)

      val parsed = upickle.default.write(baseInterval)
      val unparsed = upickle.default.read[Interval](parsed)

      assert(unparsed == baseInterval)
    }

    test("Work pickling works") {

      val baseWork = Work(List(Interval(0, 10, 1), Interval(10, 20, 1)))

      val parsed = upickle.default.write(baseWork)
      val unparsed = upickle.default.read[Work](parsed)

      assert(unparsed == baseWork)
    }

    test("Work pickling works, with precision") {

      val baseWork = Work(List(Interval(0, 10, 1, 3), Interval(10, 20, 1, 3)))

      val parsed = upickle.default.write(baseWork)
      val unparsed = upickle.default.read[Work](parsed)

      assert(unparsed == baseWork)
    }

    test("Work pickling works, with Aggregator") {

      val baseWork = Work(List(Interval(0, 10, 1), Interval(10, 20, 1)), Aggregator.Min)

      val parsed = upickle.default.write(baseWork)
      val unparsed = upickle.default.read[Work](parsed)

      assert(unparsed == baseWork)
    }
}

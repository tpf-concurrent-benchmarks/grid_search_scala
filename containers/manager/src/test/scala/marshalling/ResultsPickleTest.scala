package org.grid_search.manager
package marshalling

import work_split.{Interval, Work, Aggregator}
import org.scalatest.funsuite.AnyFunSuite

class ResultsPickleTest extends  AnyFunSuite {

  test("MeanResult pickling works") {

    val baseMeanResult = work_split.MeanResult(1.0, 1)

    val parsed = upickle.default.write(baseMeanResult)
    val unparsed = upickle.default.read[work_split.MeanResult](parsed)

    assert(unparsed == baseMeanResult)
  }
}
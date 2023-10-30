package org.grid_search.manager
package marshalling

import work_split.{Interval, Work, Aggregator}
import org.scalatest.funsuite.AnyFunSuite

class ResultsPickleTest extends  AnyFunSuite {

  test("MeanResult pickling works") {

    val baseMeanResult = work_split.MeanResult(1.0, 1)

    val parsed = parseResult(baseMeanResult)
    val unparsed = unParseResult(Aggregator.Mean, parsed)

    assert(unparsed == baseMeanResult)
  }
}
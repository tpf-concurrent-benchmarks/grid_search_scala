package org.grid_search.common
package work_split

import org.scalatest.funsuite.AnyFunSuite

class ResultTest extends AnyFunSuite {
    val epsilon = 0.0001

    test("Result.aggregateWith works with MeanResult and empty others") {
        val result = MeanResult(5.4, 3)
        val others = List()
        val diff = result.aggregateWith(others).value - result.value
        assert(diff.abs < epsilon)
        assert(result.aggregateWith(others).paramsAmount == result.paramsAmount)
    }

    test("Result.aggregateWith works with MeanResult and non-empty others") {
        val result = MeanResult(5.4, 3)
        val others = List(MeanResult(3.5, 2), MeanResult(5, 3), MeanResult(8, 4))
        val expected = MeanResult(5.85, 12)
        val diff = result.aggregateWith(others).value - expected.value
        assert(diff.abs < epsilon)
        assert(result.aggregateWith(others).paramsAmount == expected.paramsAmount)
    }

    test("Result.aggregateWith MaxResult and empty others") {
        val result = MaxResult(5.4, List(1))
        val others = List()
        val diff = result.aggregateWith(others).value - result.value
        assert(diff.abs < epsilon)
        assert(result.aggregateWith(others).parameters == result.parameters)
    }

    test("Result.aggregateWith MaxResult and non-empty others") {
        val result = MaxResult(5.4, List(1))
        val others = List(MaxResult(3.5, List(2)), MaxResult(5, List(3)), MaxResult(8, List(4)))
        val expected = MaxResult(8, List(4))
        val diff = result.aggregateWith(others).value - expected.value
        assert(diff.abs < epsilon)
        assert(result.aggregateWith(others).parameters == expected.parameters)
    }

    test("Result.aggregateWith MinResult and empty others") {
        val result = MinResult(5.4, List(1))
        val others = List()
        val diff = result.aggregateWith(others).value - result.value
        assert(diff.abs < epsilon)
        assert(result.aggregateWith(others).parameters == result.parameters)
    }

    test("Result.aggregateWith MinResult and non-empty others") {
        val result = MinResult(-4, List(1))
        val others = List(MinResult(3.5, List(2)), MinResult(5, List(3)), MinResult(8, List(4)))
        val expected = MinResult(-4, List(1))
        val diff = result.aggregateWith(others).value - expected.value
        assert(diff.abs < epsilon)
        assert(result.aggregateWith(others).parameters == expected.parameters)
    }
}

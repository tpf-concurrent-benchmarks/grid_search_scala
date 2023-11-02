package org.grid_search.common
package work_split

sealed trait Result {
    type SubResult <: Result
    def aggregateWith(others: Seq[SubResult]): SubResult
}

case class MeanResult(value: Double, paramsAmount: Int) extends Result {
    type SubResult = MeanResult
    override def aggregateWith(others: Seq[MeanResult]): MeanResult = {
        others.foldLeft(this) { (acc, res) =>
            val newAmount = acc.paramsAmount + res.paramsAmount
            val a = (acc.value / newAmount) * acc.paramsAmount
            val b = (res.value / newAmount) * res.paramsAmount
            MeanResult(a + b, newAmount)
        }
    }

}

case class MinResult(value: Double, parameters: Params) extends Result {
    type SubResult = MinResult
    override def aggregateWith(others: Seq[MinResult]): MinResult = {
        if (others.isEmpty) this
        else {
            val min = others.minBy(_.value)
            if (min.value < value) min else this
        }
    }
}

case class MaxResult(value: Double, parameters: Params) extends Result {
    type SubResult = MaxResult
    override def aggregateWith(others: Seq[MaxResult]): MaxResult = {
        if (others.isEmpty) this
        else {
            val max = others.maxBy(_.value)
            if (max.value > value) max else this
        }
    }
}

def aggregateResults(results: Seq[Result]): Result = {
    results.headOption.map { head =>
        val others = results.tail.map(_.asInstanceOf[head.SubResult])
        head.aggregateWith(others)
    }.getOrElse(throw new IllegalArgumentException("Empty results list"))
}


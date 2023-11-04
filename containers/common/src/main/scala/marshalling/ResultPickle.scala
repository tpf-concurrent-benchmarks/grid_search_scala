package org.grid_search.common
package marshalling

import work_split.{Aggregator, MaxResult, MeanResult, MinResult, Params, Result}

import upickle.default
import upickle.default.{ReadWriter, readwriter, writer}


implicit val MeanResultRW: ReadWriter[MeanResult] =
  readwriter [(Double, Int)].bimap[MeanResult](
    (meanResult: MeanResult) => (meanResult.value, meanResult.paramsAmount),
    (tuple: (Double, Int)) => MeanResult(tuple._1, tuple._2)
  )

implicit val MinResultRW: ReadWriter[MinResult] =
  readwriter [(Double, Params)].bimap[MinResult](
    (minResult: MinResult) => (minResult.value, minResult.parameters),
    (tuple: (Double, Params)) => MinResult(tuple._1, tuple._2)
  )

implicit val MaxResultRW: ReadWriter[MaxResult] =
  readwriter [(Double, Params)].bimap[MaxResult](
    (maxResult: MaxResult) => (maxResult.value, maxResult.parameters),
    (tuple: (Double, Params)) => MaxResult(tuple._1, tuple._2)
  )

def parseResult(result: Result): ujson.Value = {
  result match {
    case meanResult: MeanResult => upickle.default.writeJs(meanResult)
    case minResult: MinResult => upickle.default.writeJs(minResult)
    case maxResult: MaxResult => upickle.default.writeJs(maxResult)
  }
}

def unParseResult(aggregator:Aggregator, data: String): Result = {
  aggregator match {
    case Aggregator.Mean => upickle.default.read[MeanResult](data)
    case Aggregator.Min => upickle.default.read[MinResult](data)
    case Aggregator.Max => upickle.default.read[MaxResult](data)
  }
}

val resultW: default.Writer[Result] =
  default.writer[ujson.Value].comap[Result](
    (result: Result) => parseResult(result)
  )

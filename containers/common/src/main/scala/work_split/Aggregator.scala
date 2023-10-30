package org.grid_search.common
package work_split

enum Aggregator {
    case Mean
    case Max
    case Min
}

def aggregate( aggregator: Aggregator, values: Iterator[ (Params, Double) ] ): Result = {
    aggregator match {
        case Aggregator.Mean => aggregateMean(values)
        case Aggregator.Max => aggregateMax(values)
        case Aggregator.Min => aggregateMin(values)
    }
}

def aggregateMean( values: Iterator[ (Params, Double) ] ): MeanResult = {
    // NOTE: Values can only be iterated once, so we can't use values.sum or values.length
    val (mean, count) = values.map(_._2).foldLeft((0.0, 0)) ({
        case ((accCurrAvg, accCount), value) =>
            val accNextAvg = accCurrAvg + (value - accCurrAvg) / (accCount + 1)
            (accNextAvg, accCount + 1)
    })
    MeanResult(mean, count)
}

def aggregateMax( values: Iterator[ (Params, Double) ] ): MaxResult = {
    val (maxParams, maxValue) = values.maxBy(_._2)
    MaxResult(maxValue, maxParams)
}

def aggregateMin( values: Iterator[ (Params, Double) ] ): MinResult = {
    val (minParams, minValue) = values.minBy(_._2)
    MinResult(minValue, minParams)
}
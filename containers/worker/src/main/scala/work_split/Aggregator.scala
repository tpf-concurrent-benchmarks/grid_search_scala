package org.grid_search.worker
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
    val (sum, count) = values.foldLeft( (0.0, 0) )( (acc, value) => (acc._1 + value._2, acc._2 + 1) )
    val mean = sum / count
    MeanResult(mean, count)
}

def aggregateMax( values: Iterator[ (Params, Double) ] ): MaxResult = {
    val max = values.maxBy( _._2 )
    MaxResult(max._2, max._1)
}

def aggregateMin( values: Iterator[ (Params, Double) ] ): MinResult = {
    val min = values.minBy( _._2 )
    MinResult(min._2, min._1)
}
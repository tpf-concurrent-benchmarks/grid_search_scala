package org.grid_search.common
package marshalling

import work_split.Aggregator
import work_split.{Interval, Work}

import upickle.default

object WorkParser {
    def fromJsonFile(jsonPath: String): WorkParser = {
        // open file from path, from resources
        val lines = scala.io.Source.fromResource(jsonPath).getLines.mkString

        val workConfig = upickle.default.read[WorkConfig](lines)
        val intervals = workConfig.data.map({
            case List(start, end, step, precision) => Interval(start, end, step, precision.toInt)
            case List(start, end, step) => Interval(start, end, step)
            case _ => throw new Exception("Wrong data format")
        })
        val maxItemsPerBatch = workConfig.maxItemsPerBatch
        val aggregator = {
            workConfig.agg match {
                case "AVG" => Aggregator.Mean
                case "MIN" => Aggregator.Min
                case "MAX" => Aggregator.Max
            }
        }

        val work = Work(intervals, aggregator)
        WorkParser(work, maxItemsPerBatch)
    }

    def parse(work: Work)(implicit writer: default.Writer[Work]): String = upickle.default.write(work)

    def parseBytes(work: Work)(implicit writer: default.Writer[Work]): Array[Byte] = upickle.default.writeBinary(work)

    def unParse(work: String)(implicit reader: default.Reader[Work]): Work = upickle.default.read[Work](work)

    def unParseBytes(work: Array[Byte])(implicit reader: default.Reader[Work]): Work = upickle.default.readBinary[Work](work)
}

case class WorkParser(work: Work, maxItemsPerBatch: Int)

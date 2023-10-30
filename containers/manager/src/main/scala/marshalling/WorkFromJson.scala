package org.grid_search.manager
package marshalling

import work_split.{Interval, Work}


case class WorkConfig(data: List[List[Double]], maxItemsPerBatch: Int) derives upickle.default.ReadWriter

def workFromJson(jsonPath: String): Iterator[Work] = {
    // open file from path, from resources
    val lines = scala.io.Source.fromResource(jsonPath).getLines.mkString
    
    val workConfig = upickle.default.read[WorkConfig](lines)

    val intervals = workConfig.data.map(i => Interval(i(0), i(1), i(2)))
    val maxItemsPerBatch = workConfig.maxItemsPerBatch

    val work = Work(intervals)
    
    work.split(maxItemsPerBatch)
}

def parseWork(work: Work):String = upickle.default.write(work)

def unParseWork(work: String):Work = upickle.default.read[Work](work)
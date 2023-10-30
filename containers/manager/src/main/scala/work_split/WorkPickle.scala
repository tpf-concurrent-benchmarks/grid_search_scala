package org.grid_search.manager
package work_split

import upickle.default._


implicit val intervalRW: ReadWriter[Interval] =
  readwriter[Array[Double]].bimap[Interval](
    interval => Array(interval.start, interval.end, interval.step, interval.precision.getOrElse(-1)),
    arr => {
      if arr(3)>0 then Interval(arr(0), arr(1), arr(2), arr(3).toInt)
      else Interval(arr(0), arr(1), arr(2))
    }
  )

implicit val workRW: ReadWriter[Work] =
  readwriter[Array[Interval]].bimap[Work](
    work => work.intervals.toArray,
    arr => Work(arr.toList)
  )
package org.grid_search.manager
package work_split

import scala.collection.IndexedSeqView
import scala.util.control.Breaks._

object WorkPlan {
    def apply(intervals: List[CircularIterator], intervalsPerIterator: List[Int]): WorkPlan = {
        new WorkPlan(intervals, intervalsPerIterator)
    }
}

case class WorkPlan(intervals: List[CircularIterator], intervalsPerIterator: List[Int]) {
    def calcCartesianProduct(): IndexedSeqView[Work] = {
        var positions = List.fill(intervals.length)(0)
        val size = intervals.map(_.size).product
        var currentValues = intervals.map(_.next)
        var update = false

        for (_ <- (0 until size).view) yield {
            println(s"positions: $positions, currentValues: $currentValues")
            if (update) {
                breakable {
                    for (i <- intervals.indices.view) {
                        println(s"i: $i")
                        positions = positions.updated(i, positions(i) + 1)
                        currentValues = currentValues.updated(i, intervals(i).next)

                        if (positions(i) < intervalsPerIterator(i)) {
                            break
                        } else {
                            positions = positions.updated(i, 0)
                        }
                    }
                }
            } else {
                update = true
            }
            Work(currentValues)
        }
    }
}

package org.grid_search.worker

import scala.math.cos
import org.grid_search.common.work_split.Params


def griewankFunc(params: Params ): Double = {
    val a = params.head
    val b = params(1)
    val c = params(2)
    1/4000.toDouble * (a*a + b*b + c*c) - cos(a) * cos(b / math.sqrt(2)) * cos(c / math.sqrt(3)) + 1
}
package org.grid_search.worker

import work_split.{Params}


def mainFunc( params: Params ): Double = {
  val x = params(0)
  val y = params(1)
  val z = params(2)
  val result = x * x + y * y + z * z
  result
}
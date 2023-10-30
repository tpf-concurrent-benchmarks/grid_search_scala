package org.grid_search.worker

import work_split.{Params}


def mainFunc( params: Params ): Double = {
  params.map( x => x * x ).sum
}
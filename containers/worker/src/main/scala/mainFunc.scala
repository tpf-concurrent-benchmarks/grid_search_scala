package org.grid_search.worker

import org.grid_search.common.work_split.Params


def mainFunc( params: Params ): Double = {
  params.map( x => x * x ).sum
}
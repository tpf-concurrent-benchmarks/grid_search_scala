package org.grid_search.worker
package work_split

sealed trait Result

case class AvgResult(value: Double, paramsAmount: Int) extends Result

case class MinResult(value: Double, parameters: List[Double]) extends Result

case class MaxResult(value: Double, parameters: List[Double]) extends Result
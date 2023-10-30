package org.grid_search.common
package work_split

sealed trait Result

case class MeanResult(value: Double, paramsAmount: Int) extends Result

case class MinResult(value: Double, parameters: Params) extends Result

case class MaxResult(value: Double, parameters: Params) extends Result

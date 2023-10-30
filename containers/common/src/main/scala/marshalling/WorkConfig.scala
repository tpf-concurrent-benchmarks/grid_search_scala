package org.grid_search.common
package marshalling

case class WorkConfig(data: List[List[Double]], maxItemsPerBatch: Int, agg: String) derives upickle.default.ReadWriter
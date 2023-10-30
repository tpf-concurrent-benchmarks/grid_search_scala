package org.grid_search.worker
package work_split

implicit class Crossable[X](xs: Iterable[X]) {
  def cross[Y](ys: Iterable[Y]): Iterable[(X, Y)] = for {x <- xs.view; y <- ys.view} yield (x, y)

}

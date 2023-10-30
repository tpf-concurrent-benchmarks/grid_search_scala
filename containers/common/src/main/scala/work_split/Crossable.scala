package org.grid_search.common
package work_split

implicit class Crossable[X](xs: Iterable[X]) {
  def cross[Y](ys: Iterable[Y]): Iterable[(X, Y)] = for {x <- xs.view; y <- ys.view} yield (x, y)

}

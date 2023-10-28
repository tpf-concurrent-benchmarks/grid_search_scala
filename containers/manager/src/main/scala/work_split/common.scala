package org.grid_search.manager
package work_split

def roundNumber(number: Double, precision: Option[Int] = None): Double = {
    precision match {
        case None => number
        case Some(p) => {
            val scale = Math.pow(10, p)
            Math.round(number * scale) / scale
        }
    }
}

@throws[IllegalArgumentException]
def assertIntervalIsCorrect(start: Double, end: Double, step: Double): Unit = {
    if (start > end) {
        throw new IllegalArgumentException("Start must be less than end")
    }
    if (step <= 0) {
        throw new IllegalArgumentException("Step must be positive")
    }
}
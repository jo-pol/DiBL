package dibl.math

/**
 *
 * @param matrix call with a clone for an immutable instance
 * @param flipper object that flips a tuple
 */
class Matrix(matrix: Array[Array[String]], flipper: Flipper[String]) {


  private val rows = matrix.length
  private val cols = matrix(0).length
  private val result = Array.ofDim[String](2, 2)

  override def toString: String = matrix.deep.toString()

  def flipLeftRight: Array[Array[String]] = {
    for {r <- 0 until rows
         c <- 0 until cols
    } {
      result(r)(cols - 1 - c) = flipper.flipLeftRight(matrix(r)(c))
    }
    result
  }

  def flipBottomUp: Array[Array[String]] = {
    for {r <- 0 until rows
         c <- 0 until cols
    } {
      result(rows - 1 - r)(c) = flipper.flipBottomUp(matrix(r)(c))
    }
    result
  }

  def flipNE2SW: Array[Array[String]] = {
    for {r <- 0 until rows
         c <- 0 until cols
    } {
      result(c)(r) = flipper.flipNE2SW(matrix(r)(c))
    }
    result
  }

  def flipNW2SE: Array[Array[String]] = {
    for {r <- 0 until rows
         c <- 0 until cols
    } {
      result(cols - 1 - c)(rows - 1 - r) = flipper.flipNW2SE(matrix(r)(c))
    }
    result
  }

  def shift(row: Int, col: Int): Array[Array[String]] = {
    for {r <- 0 until rows
         c <- 0 until cols
    } {
      result(r)(c) = matrix((r + row) % rows)((c + col) % cols)
    }
    result
  }

  def isShifted(shiftedMatrix: Array[Array[String]]): Boolean = {
    val shifted = shiftedMatrix.deep.toString()
    for {r <- 0 until rows
         c <- 0 until cols
    } {
      if (shift(r, c).deep.toString() == shifted) return true
    }
    false
  }

  def skewDown: Array[Array[String]] = {
    for {r <- 0 until rows
         c <- 0 until cols
    } {
      result(r)(c) = matrix((r + c) % rows)(c)
    }
    result
  }

  def skewUp: Array[Array[String]] = {
    for {r <- 0 until rows
         c <- 0 until cols
    } {
      result(r)(c) = matrix((r + rows - c) % rows)(c)
    }
    result
  }

  def fromBrickToCheckerboard: Array[Array[String]] = {
    for {r <- 0 until rows
         c <- 0 until cols
    } {
      result(r)(c) = matrix(r)(c)
      result(r + rows)(((1.5 * cols) + c).toInt % cols) = matrix(r)(c)
    }
    result
  }
}

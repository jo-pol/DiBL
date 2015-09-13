/*
 Copyright 2015 Jo Pol
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html dibl
*/

package dibl

/** @param cellFlipper object that flips matrix elements */
class MatrixFlipper(cellFlipper: Flipper[String]) extends Flipper[M] {

  def flipLeftRight(matrix: M): M = {
    val rows = matrix.length
    val cols = matrix(0).length
    val result = Array.ofDim[String](rows, cols)
    for {r <- 0 until rows
         c <- 0 until cols
    } {
      result(r)(cols - 1 - c) = cellFlipper.flipLeftRight(matrix(r)(c))
    }
    result
  }

  def flipBottomUp(matrix: M): M = {
    val rows = matrix.length
    val cols = matrix(0).length
    val result = Array.ofDim[String](rows, cols)
    for {r <- 0 until rows
         c <- 0 until cols
    } {
      result(rows - 1 - r)(c) = cellFlipper.flipBottomUp(matrix(r)(c))
    }
    result
  }

  def flipNE2SW(matrix: M): M = {
    val rows = matrix.length
    val cols = matrix(0).length
    val result = Array.ofDim[String](rows, cols)
    for {r <- 0 until rows
         c <- 0 until cols
    } {
      result(c)(r) = cellFlipper.flipNE2SW(matrix(r)(c))
    }
    result
  }

  def flipNW2SE(matrix: M): M = {
    val rows = matrix.length
    val cols = matrix(0).length
    val result = Array.ofDim[String](rows, cols)
    for {r <- 0 until rows
         c <- 0 until cols
    } {
      result(cols - 1 - c)(rows - 1 - r) = cellFlipper.flipNW2SE(matrix(r)(c))
    }
    result
  }
}

object MatrixFlipper {

  def flipDiamond(instruction: String, matrix: M) = {
    instruction match {
      case "x" => diamonds.flipNW2SE(matrix)
      case "y" => diamonds.flipNE2SW(matrix)
      case "r" => diamonds.flipNE2SW(diamonds.flipNW2SE(matrix))
    }
  }
  def flipInterleaved(instruction: String, brickMatrix: M) = {
    val matrix = fromBrickToCheckerboard(brickMatrix)
    instruction match {
      case "y" => interleaved.flipLeftRight(matrix)
      case "x" => interleaved.flipBottomUp(matrix)
      case "r" => interleaved.flipBottomUp(interleaved.flipLeftRight(matrix))
    }
  }
  def flipBrick(instruction: String, brickMatrix: M) = {
    val matrix = fromBrickToCheckerboard(brickMatrix)
    instruction match {
      case "y" => bricks.flipLeftRight(matrix)
      case "x" => bricks.flipBottomUp(matrix)
      case "r" => bricks.flipBottomUp(bricks.flipLeftRight(matrix))
    }
  }
  val diamonds = new MatrixFlipper(ShortTupleFlipper)
  val interleaved = new MatrixFlipper(LongTupleFlipper)
  val bricks = new BrickFlipper(LongTupleFlipper)
  
  class BrickFlipper(cellFlipper: Flipper[String]) extends 
       MatrixFlipper(cellFlipper: Flipper[String]) {
  
    // input | super | output
    // ========================
    // 1-2-3- | 3-2-1- | 3-2-1-
    // -4-5-6 | -6-5-4 | -5-4-6
    // 7-8-9- | 9-8-7- | 9-8-7-
    // ==============================
    // A-B-C-D- | D-C-B-A- | D-C-B-A-
    // -E-F-G-H | -H-G-F-E | -G-F-E-H
    // I-J-K-L- | L-K-J-I- | L-K-J-I-
    // -M-N-O-P | -P-O-N-M | -O-N-M-P
    // ==============================
    override def flipLeftRight(matrix: M) = shiftOddRows(super.flipLeftRight(matrix))

    // input | super | output
    // ========================
    // 1-2-3- | 7-8-9-
    // -4-5-6 | -4-5-6
    // 7-8-9- | 1-2-3-
    // ==============================
    // A-B-C-D- | M-N-O-P- | M-N-O-P-
    // -E-F-G-H | -I-J-K-L | -J-K-L-I
    // I-J-K-L- | E-F-G-H- | E-F-G-H-
    // -M-N-O-P | -A-B-C-D | -B-C-D-A
    // ==============================
    override def flipBottomUp(matrix: M) = {

      shiftOddRows(super.flipBottomUp(matrix))
      if (matrix(0).length % 2 == 0)
        shiftOddRows(matrix)
      else matrix
    }
  }

  private def shiftOddRows(matrix: M) = {

    val result = matrix.map(_.clone())
    val cols = matrix(0).length
    val rows = matrix.length
    for (r <- 1 until rows by 2) {
      val saved = matrix(r)(0)
      for (c <- 1 until cols) {
        result(r)(c-1) =  matrix(r)(c)
      }
      result(r)(cols-1) = saved
    }
    result
  }

  def fromBrickToCheckerboard(matrix: M): M = {
    val rows = matrix.length
    val cols = matrix(0).length
    val result = Array.ofDim[String](rows*2, cols)
    for {r <- 0 until rows
         c <- 0 until cols
    } {
      result(r)(c) = matrix(r)(c)
      result(r + rows)(((1.5 * cols) + c).toInt % cols) = matrix(r)(c)
    }
    result
  }
}
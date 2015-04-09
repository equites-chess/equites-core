// Equites, a Scala chess playground
// Copyright © 2013-2014 Frank S. Thomas <frank@timepit.eu>
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package eu.timepit.equites
package util

import org.specs2._

import BoardUtil._
import PieceAbbr.Wiki._
import SquareAbbr._

import ArbitraryInstances._

class BoardUtilSpec extends Specification with ScalaCheck {
  def is = s2"""
  BoardUtil
    readFenPlacement should be the inverse of showFenPlacement $ex1
    showFenPlacement should return correct FEN placements      $ex2
  """

  def ex1 = prop { (board: Board) =>
    readFenPlacement(showFenPlacement(board)) must_== board
  }

  def ex2 = {
    val board0 = Rules.startingBoard
    val board1 = board0.processMove(Move(pl, Draw(e2, e4)))
    val board2 = board1.processMove(Move(pd, Draw(c7, c5)))
    val board3 = board2.processMove(Move(nl, Draw(g1, f3)))

    val placements = Seq(
      board0 -> "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR",
      board1 -> "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR",
      board2 -> "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR",
      board3 -> "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R")

    placements.map {
      case (board, placement) => showFenPlacement(board) must_== placement
    }
  }
}

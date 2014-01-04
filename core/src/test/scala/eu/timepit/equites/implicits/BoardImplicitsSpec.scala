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
package implicits

import org.specs2._

import BoardImplicits._
import util.PieceAbbr.Wiki._
import util.SquareAbbr._

class BoardImplicitsSpec extends Specification { def is = s2"""
  toFenPlacement should
    return correct FEN placements $ex1
  """

  val board0 = Rules.startingBoard
  val board1 = board0.processMove(Move(pl, e2, e4))
  val board2 = board1.processMove(Move(pd, c7, c5))
  val board3 = board2.processMove(Move(nl, g1, f3))

  def ex1 =
    (board0.toFenPlacement must_==
      "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR") and
    (board1.toFenPlacement must_==
      "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR") and
    (board2.toFenPlacement must_==
      "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR") and
    (board3.toFenPlacement must_==
      "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R")
}

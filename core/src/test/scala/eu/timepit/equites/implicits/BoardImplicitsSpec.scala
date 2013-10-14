// Equites, a Scala chess playground
// Copyright © 2013 Frank S. Thomas <frank@timepit.eu>
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

import org.specs2.mutable._

import BoardImplicits._
import util.PieceAbbr.Wiki._

class BoardImplicitsSpec extends Specification {
  "RichBoard" should {
    "correctly perform toFenPlacement" in {
      val board0 = Rules.startingBoard
      board0.toFenPlacement must_==
        "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"

      val move1 = Move(pl, Square('e', 2), Square('e', 4))
      val board1 = board0.processMove(move1)
      board1.toFenPlacement must_==
        "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR"

      val move2 = Move(pd, Square('c', 7), Square('c', 5))
      val board2 = board1.processMove(move2)
      board2.toFenPlacement must_==
        "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR"

      val move3 = Move(nl, Square('g', 1), Square('f', 3))
      val board3 = board2.processMove(move3)
      board3.toFenPlacement must_==
        "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R"
    }
  }
}

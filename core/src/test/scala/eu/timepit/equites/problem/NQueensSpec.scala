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
package problem

import org.specs2.mutable._

import NQueens._

class NQueensSpec extends Specification {
  "allBoards" should {
    "place queens on a board that do not attack each other" in {
      val board = allBoards(0)
      def nonattacking(placed: Placed[Piece]): Boolean =
        board.getPlaced(Rules.possibleSquares(placed)).isEmpty

      board.placedPieces.forall(nonattacking) must beTrue
    }
  }
}

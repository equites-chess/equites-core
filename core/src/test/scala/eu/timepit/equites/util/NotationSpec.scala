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
package util

import org.specs2.ScalaCheck
import org.specs2.mutable._

import implicits.BoardImplicits._
import implicits.PieceImplicits._
import Notation._

class NotationSpec extends Specification with ScalaCheck {
  "pieceFromLetter" should {
    "be the inverse of RichPiece.toLetter" in check {
      (piece: Piece) =>
        pieceFromLetter(piece.toLetter.charAt(0)) must beSome(piece)
    }
  }

  "pieceFromFigurine" should {
    "be the inverse of RichPiece.toFigurine" in check {
      (piece: Piece) =>
        pieceFromFigurine(piece.toFigurine.charAt(0)) must beSome(piece)
    }
  }

  "boardFromFen" should {
    "be the inverse of RichBoard.toFenPlacement" in check {
      (board: Board) => boardFromFen(board.toFenPlacement) must_== board
    }
  }
}

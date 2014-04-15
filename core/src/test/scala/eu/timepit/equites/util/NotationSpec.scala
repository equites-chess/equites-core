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

import BoardUtil._
import PieceUtil._

import ArbitraryInstances._

class NotationSpec extends Specification with ScalaCheck {
  "pieceFromLetter" should {
    "be the inverse of RichPiece.toLetter" in check {
      (piece: AnyPiece) =>
        readLetter(showLetter(piece).charAt(0)) must beSome(piece)
    }
    "yield None on invalid input" in {
      readLetter('0') must beNone
    }
  }

  "pieceFromFigurine" should {
    "be the inverse of RichPiece.toFigurine" in check {
      (piece: AnyPiece) =>
        readFigurine(showFigurine(piece).charAt(0)) must beSome(piece)
    }
    "yield None on invalid input" in {
      readFigurine('0') must beNone
    }
  }

  "boardFromFen" should {
    "be the inverse of RichBoard.toFenPlacement" in check {
      (board: Board) => readFenPlacement(showFenPlacement(board)) must_== board
    }
  }
}

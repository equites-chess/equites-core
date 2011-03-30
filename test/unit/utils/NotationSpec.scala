// Equites, a simple chess interface
// Copyright © 2011 Frank S. Thomas <f.thomas@gmx.de>
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

package equites
package utils

import org.specs2.mutable._
import Notation._

class NotationSpec extends Specification {
  "object Notation" should {
    "correctly convert pieces to algebraic notation" in {
      toAlgebraic(new King(White))   must_== "K"
      toAlgebraic(new King(Black))   must_== "K"
      toAlgebraic(new Queen(White))  must_== "Q"
      toAlgebraic(new Queen(Black))  must_== "Q"
      toAlgebraic(new Rook(White))   must_== "R"
      toAlgebraic(new Rook(Black))   must_== "R"
      toAlgebraic(new Bishop(White)) must_== "B"
      toAlgebraic(new Bishop(Black)) must_== "B"
      toAlgebraic(new Knight(White)) must_== "N"
      toAlgebraic(new Knight(Black)) must_== "N"
      toAlgebraic(new Pawn(White))   must_== ""
      toAlgebraic(new Pawn(Black))   must_== ""
    }

    "correctly convert pieces to letters" in {
      toLetter(new King(White))   must_== "K"
      toLetter(new King(Black))   must_== "k"
      toLetter(new Queen(White))  must_== "Q"
      toLetter(new Queen(Black))  must_== "q"
      toLetter(new Rook(White))   must_== "R"
      toLetter(new Rook(Black))   must_== "r"
      toLetter(new Bishop(White)) must_== "B"
      toLetter(new Bishop(Black)) must_== "b"
      toLetter(new Knight(White)) must_== "N"
      toLetter(new Knight(Black)) must_== "n"
      toLetter(new Pawn(White))   must_== "P"
      toLetter(new Pawn(Black))   must_== "p"
    }

    "correctly convert pieces to symbols" in {
      toSymbol(new King(White))   must_== "♔"
      toSymbol(new King(Black))   must_== "♚"
      toSymbol(new Queen(White))  must_== "♕"
      toSymbol(new Queen(Black))  must_== "♛"
      toSymbol(new Rook(White))   must_== "♖"
      toSymbol(new Rook(Black))   must_== "♜"
      toSymbol(new Bishop(White)) must_== "♗"
      toSymbol(new Bishop(Black)) must_== "♝"
      toSymbol(new Knight(White)) must_== "♘"
      toSymbol(new Knight(Black)) must_== "♞"
      toSymbol(new Pawn(White))   must_== "♙"
      toSymbol(new Pawn(Black))   must_== "♟"
    }
  }
}

// Equites, a Scala chess playground
// Copyright © 2011, 2013 Frank S. Thomas <frank@timepit.eu>
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

import PieceImplicits._

class PieceImplicitsSpec extends Specification {
  "RichPiece" should {
    "correctly perform toAlgebraic" in {
      King(White).toAlgebraic   must_== "K"
      King(Black).toAlgebraic   must_== "K"
      Queen(White).toAlgebraic  must_== "Q"
      Queen(Black).toAlgebraic  must_== "Q"
      Rook(White).toAlgebraic   must_== "R"
      Rook(Black).toAlgebraic   must_== "R"
      Bishop(White).toAlgebraic must_== "B"
      Bishop(Black).toAlgebraic must_== "B"
      Knight(White).toAlgebraic must_== "N"
      Knight(Black).toAlgebraic must_== "N"
      Pawn(White).toAlgebraic   must_== ""
      Pawn(Black).toAlgebraic   must_== ""
    }

    "correctly perform toLetter" in {
      King(White).toLetter   must_== "K"
      King(Black).toLetter   must_== "k"
      Queen(White).toLetter  must_== "Q"
      Queen(Black).toLetter  must_== "q"
      Rook(White).toLetter   must_== "R"
      Rook(Black).toLetter   must_== "r"
      Bishop(White).toLetter must_== "B"
      Bishop(Black).toLetter must_== "b"
      Knight(White).toLetter must_== "N"
      Knight(Black).toLetter must_== "n"
      Pawn(White).toLetter   must_== "P"
      Pawn(Black).toLetter   must_== "p"
    }

    "correctly perform toFigurine" in {
      King(White).toFigurine   must_== "♔"
      King(Black).toFigurine   must_== "♚"
      Queen(White).toFigurine  must_== "♕"
      Queen(Black).toFigurine  must_== "♛"
      Rook(White).toFigurine   must_== "♖"
      Rook(Black).toFigurine   must_== "♜"
      Bishop(White).toFigurine must_== "♗"
      Bishop(Black).toFigurine must_== "♝"
      Knight(White).toFigurine must_== "♘"
      Knight(Black).toFigurine must_== "♞"
      Pawn(Black).toFigurine   must_== "♟"
    }

    "correctly perform toWikiLetters" in {
      King(White).toWikiLetters   must_== "kl"
      King(Black).toWikiLetters   must_== "kd"
      Queen(White).toWikiLetters  must_== "ql"
      Queen(Black).toWikiLetters  must_== "qd"
      Rook(White).toWikiLetters   must_== "rl"
      Rook(Black).toWikiLetters   must_== "rd"
      Bishop(White).toWikiLetters must_== "bl"
      Bishop(Black).toWikiLetters must_== "bd"
      Knight(White).toWikiLetters must_== "nl"
      Knight(Black).toWikiLetters must_== "nd"
      Pawn(White).toWikiLetters   must_== "pl"
      Pawn(Black).toWikiLetters   must_== "pd"
    }

    "correctly perform toNumeric" in {
      King(White).toNumeric   must_== ""
      King(Black).toNumeric   must_== ""
      Queen(White).toNumeric  must_== "1"
      Queen(Black).toNumeric  must_== "1"
      Rook(White).toNumeric   must_== "2"
      Rook(Black).toNumeric   must_== "2"
      Bishop(White).toNumeric must_== "3"
      Bishop(Black).toNumeric must_== "3"
      Knight(White).toNumeric must_== "4"
      Knight(Black).toNumeric must_== "4"
      Pawn(White).toNumeric   must_== ""
      Pawn(Black).toNumeric   must_== ""
    }
  }
}

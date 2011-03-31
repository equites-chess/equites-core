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
package implicits

import org.specs2.mutable._
import RichPieceImplicit._

class RichPieceSpec extends Specification {
  "class RichPiece" should {
    "correctly perform toAlgebraic" in {
      (new King(White)).toAlgebraic   must_== "K"
      (new King(Black)).toAlgebraic   must_== "K"
      (new Queen(White)).toAlgebraic  must_== "Q"
      (new Queen(Black)).toAlgebraic  must_== "Q"
      (new Rook(White)).toAlgebraic   must_== "R"
      (new Rook(Black)).toAlgebraic   must_== "R"
      (new Bishop(White)).toAlgebraic must_== "B"
      (new Bishop(Black)).toAlgebraic must_== "B"
      (new Knight(White)).toAlgebraic must_== "N"
      (new Knight(Black)).toAlgebraic must_== "N"
      (new Pawn(White)).toAlgebraic   must_== ""
      (new Pawn(Black)).toAlgebraic   must_== ""
    }

    "correctly perform toLetter" in {
      (new King(White)).toLetter   must_== "K"
      (new King(Black)).toLetter   must_== "k"
      (new Queen(White)).toLetter  must_== "Q"
      (new Queen(Black)).toLetter  must_== "q"
      (new Rook(White)).toLetter   must_== "R"
      (new Rook(Black)).toLetter   must_== "r"
      (new Bishop(White)).toLetter must_== "B"
      (new Bishop(Black)).toLetter must_== "b"
      (new Knight(White)).toLetter must_== "N"
      (new Knight(Black)).toLetter must_== "n"
      (new Pawn(White)).toLetter   must_== "P"
      (new Pawn(Black)).toLetter   must_== "p"
    }

    "correctly perform toFigurine" in {
      (new King(White)).toFigurine   must_== "♔"
      (new King(Black)).toFigurine   must_== "♚"
      (new Queen(White)).toFigurine  must_== "♕"
      (new Queen(Black)).toFigurine  must_== "♛"
      (new Rook(White)).toFigurine   must_== "♖"
      (new Rook(Black)).toFigurine   must_== "♜"
      (new Bishop(White)).toFigurine must_== "♗"
      (new Bishop(Black)).toFigurine must_== "♝"
      (new Knight(White)).toFigurine must_== "♘"
      (new Knight(Black)).toFigurine must_== "♞"
      (new Pawn(White)).toFigurine   must_== "♙"
      (new Pawn(Black)).toFigurine   must_== "♟"
    }

    "correctly perform toWikiLetters" in {
      (new King(White)).toWikiLetters   must_== "kl"
      (new King(Black)).toWikiLetters   must_== "kd"
      (new Queen(White)).toWikiLetters  must_== "ql"
      (new Queen(Black)).toWikiLetters  must_== "qd"
      (new Rook(White)).toWikiLetters   must_== "rl"
      (new Rook(Black)).toWikiLetters   must_== "rd"
      (new Bishop(White)).toWikiLetters must_== "bl"
      (new Bishop(Black)).toWikiLetters must_== "bd"
      (new Knight(White)).toWikiLetters must_== "nl"
      (new Knight(Black)).toWikiLetters must_== "nd"
      (new Pawn(White)).toWikiLetters   must_== "pl"
      (new Pawn(Black)).toWikiLetters   must_== "pd"
    }
  }
}

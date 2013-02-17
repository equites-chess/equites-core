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
import util.PieceAbbr._

class PieceImplicitsSpec extends Specification {
  "RichPiece" should {
    "correctly perform toAlgebraic" in {
      K.toAlgebraic must_== "K"
      k.toAlgebraic must_== "K"
      Q.toAlgebraic must_== "Q"
      q.toAlgebraic must_== "Q"
      R.toAlgebraic must_== "R"
      r.toAlgebraic must_== "R"
      B.toAlgebraic must_== "B"
      b.toAlgebraic must_== "B"
      N.toAlgebraic must_== "N"
      n.toAlgebraic must_== "N"
      P.toAlgebraic must_== ""
      Pawn(Black).toAlgebraic must_== ""
    }

    "correctly perform toLetter" in {
      K.toLetter must_== "K"
      k.toLetter must_== "k"
      Q.toLetter must_== "Q"
      q.toLetter must_== "q"
      R.toLetter must_== "R"
      r.toLetter must_== "r"
      B.toLetter must_== "B"
      b.toLetter must_== "b"
      N.toLetter must_== "N"
      n.toLetter must_== "n"
      P.toLetter must_== "P"
      Pawn(Black).toLetter must_== "p"
    }

    "correctly perform toFigurine" in {
      K.toFigurine must_== "♔"
      k.toFigurine must_== "♚"
      Q.toFigurine must_== "♕"
      q.toFigurine must_== "♛"
      R.toFigurine must_== "♖"
      r.toFigurine must_== "♜"
      B.toFigurine must_== "♗"
      b.toFigurine must_== "♝"
      N.toFigurine must_== "♘"
      n.toFigurine must_== "♞"
      P.toFigurine must_== "♙"
      Pawn(Black).toFigurine must_== "♟"
    }

    "correctly perform toWikiLetters" in {
      K.toWikiLetters must_== "kl"
      k.toWikiLetters must_== "kd"
      Q.toWikiLetters must_== "ql"
      q.toWikiLetters must_== "qd"
      R.toWikiLetters must_== "rl"
      r.toWikiLetters must_== "rd"
      B.toWikiLetters must_== "bl"
      b.toWikiLetters must_== "bd"
      N.toWikiLetters must_== "nl"
      n.toWikiLetters must_== "nd"
      P.toWikiLetters must_== "pl"
      Pawn(Black).toWikiLetters must_== "pd"
    }

    "correctly perform toNumeric" in {
      K.toNumeric must_== ""
      k.toNumeric must_== ""
      Q.toNumeric must_== "1"
      q.toNumeric must_== "1"
      R.toNumeric must_== "2"
      r.toNumeric must_== "2"
      B.toNumeric must_== "3"
      b.toNumeric must_== "3"
      N.toNumeric must_== "4"
      n.toNumeric must_== "4"
      P.toNumeric must_== ""
      Pawn(Black).toNumeric must_== ""
    }
  }
}

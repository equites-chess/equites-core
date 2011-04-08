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
import RichActionImplicit._

class RichActionSpec extends Specification {
  "class RichAction" should {
    val moveWhite = Move(new Queen(White), Square(0, 0), Square(7, 7))
    val moveBlack = Move(new Queen(Black), Square(0, 0), Square(7, 7))

    val checkingWhite = Move(new Rook(White), Square(2, 3), Square(2, 6))
    val checkingBlack = Move(new Rook(Black), Square(2, 3), Square(2, 6))
    checkingWhite.isChecking = true
    checkingBlack.isChecking = true

    val checkmatingWhite = Move(new Rook(White), Square(2, 3), Square(2, 6))
    val checkmatingBlack = Move(new Rook(Black), Square(2, 3), Square(2, 6))
    checkmatingWhite.isCheckmating = true
    checkmatingBlack.isCheckmating = true

    val promotionWhite =
      Promotion(new Pawn(White), Square(3, 6), Square(3, 7))
    val promotionBlack =
      Promotion(new Pawn(Black), Square(3, 1), Square(3, 0))

    val captureWhite =
      Capture(new Knight(White), Square(0, 0), Square(2, 1), new Pawn(Black))
    val captureBlack =
      Capture(new Knight(Black), Square(0, 0), Square(2, 1), new Pawn(White))

    val captPromoWhite = CaptureAndPromotion(new Pawn(White), Square(0, 6),
      Square(1, 7), new Knight(Black))
    val captPromoBlack = CaptureAndPromotion(new Pawn(Black), Square(0, 1),
      Square(1, 0), new Knight(White))

    val enPassantWhite = EnPassant(new Pawn(White), Square(4, 4),
      Square(5, 5), new Pawn(Black), Square(5, 4))
    val enPassantBlack = EnPassant(new Pawn(Black), Square(1, 3),
      Square(0, 2), new Pawn(White), Square(0, 3))

    val castlingShortWhite = CastlingShort(new King(White), new Rook(White))
    val castlingShortBlack = CastlingShort(new King(Black), new Rook(Black))
    val castlingLongWhite = CastlingLong(new King(White), new Rook(White))
    val castlingLongBlack = CastlingLong(new King(Black), new Rook(Black))

    "correctly perform toNumeric" in {
      moveWhite.toNumeric must_== "1188"
      moveBlack.toNumeric must_== "1188"

      promotionWhite.toNumeric must_== "47481"
      promotionBlack.toNumeric must_== "42411"

      castlingShortWhite.toNumeric must_== "5171"
      castlingShortBlack.toNumeric must_== "5878"
      castlingLongWhite.toNumeric  must_== "5131"
      castlingLongBlack.toNumeric  must_== "5838"
    }

    "correctly perform toLongAlgebraic and toLongFigurine" in {
      castlingShortWhite.toLongAlgebraic must_== "0-0"
      castlingShortBlack.toLongAlgebraic must_== "0-0"
      castlingShortWhite.toLongFigurine  must_== "0-0"
      castlingShortBlack.toLongFigurine  must_== "0-0"

      castlingLongWhite.toLongAlgebraic must_== "0-0-0"
      castlingLongBlack.toLongAlgebraic must_== "0-0-0"
      castlingLongWhite.toLongFigurine  must_== "0-0-0"
      castlingLongBlack.toLongFigurine  must_== "0-0-0"

      moveWhite.toLongAlgebraic must_== "Qa1-h8"
      moveBlack.toLongAlgebraic must_== "Qa1-h8"
      moveWhite.toLongFigurine  must_== "♕a1-h8"
      moveBlack.toLongFigurine  must_== "♛a1-h8"

      promotionWhite.toLongAlgebraic must_== "d7-d8Q"
      promotionBlack.toLongAlgebraic must_== "d2-d1Q"
      promotionWhite.toLongFigurine  must_== "♙d7-d8♕"
      promotionBlack.toLongFigurine  must_== "♟d2-d1♛"

      enPassantWhite.toLongAlgebraic must_== "e5xf6e.p."
      enPassantBlack.toLongAlgebraic must_== "b4xa3e.p."
      enPassantWhite.toLongFigurine  must_== "♙e5xf6e.p."
      enPassantBlack.toLongFigurine  must_== "♟b4xa3e.p."

      captureWhite.toLongAlgebraic must_== "Na1xc2"
      captureBlack.toLongAlgebraic must_== "Na1xc2"
      captureWhite.toLongFigurine  must_== "♘a1xc2"
      captureBlack.toLongFigurine  must_== "♞a1xc2"

      captPromoWhite.toLongAlgebraic must_== "a7xb8Q"
      captPromoBlack.toLongAlgebraic must_== "a2xb1Q"
      captPromoWhite.toLongFigurine  must_== "♙a7xb8♕"
      captPromoBlack.toLongFigurine  must_== "♟a2xb1♛"

      checkingWhite.toLongAlgebraic must_== "Rc4-c7+"
      checkingBlack.toLongAlgebraic must_== "Rc4-c7+"
      checkingWhite.toLongFigurine  must_== "♖c4-c7+"
      checkingBlack.toLongFigurine  must_== "♜c4-c7+"

      checkmatingWhite.toLongAlgebraic must_== "Rc4-c7#"
      checkmatingBlack.toLongAlgebraic must_== "Rc4-c7#"
      checkmatingWhite.toLongFigurine  must_== "♖c4-c7#"
      checkmatingBlack.toLongFigurine  must_== "♜c4-c7#"
    }
  }
}

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
import ActionImplicits._
import util.PieceAbbr.Wiki._

class ActionImplicitsSpec extends Specification {
  "RichAction" should {
    val moveWhite = Move(ql, Square.unsafeFrom(0, 0) to Square.unsafeFrom(7, 7))
    val moveBlack = Move(qd, Square.unsafeFrom(0, 0) to Square.unsafeFrom(7, 7))

    val promoWhite = Promotion(pl, Square.unsafeFrom(3, 6) to Square.unsafeFrom(3, 7), ql)
    val promoBlack = Promotion(pd, Square.unsafeFrom(3, 1) to Square.unsafeFrom(3, 0), qd)

    val captWhite = Capture(nl, Square.unsafeFrom(0, 0) to Square.unsafeFrom(2, 1), pd)
    val captBlack = Capture(nd, Square.unsafeFrom(0, 0) to Square.unsafeFrom(2, 1), pl)

    val captPromoWhite = CaptureAndPromotion(pl, Square.unsafeFrom(0, 6) to
      Square.unsafeFrom(1, 7), kd, ql)
    val captPromoBlack = CaptureAndPromotion(pd, Square.unsafeFrom(0, 1) to
      Square.unsafeFrom(1, 0), kl, qd)

    val enPassantWhite = EnPassant(pl, Square.unsafeFrom(4, 4) to Square.unsafeFrom(5, 5),
      pd, Square.unsafeFrom(5, 4))
    val enPassantBlack = EnPassant(pd, Square.unsafeFrom(1, 3) to Square.unsafeFrom(0, 2),
      pl, Square.unsafeFrom(0, 3))

    "correctly perform toNumeric" in {
      moveWhite.toNumeric must_== "1188"
      moveBlack.toNumeric must_== "1188"

      promoWhite.toNumeric must_== "47481"
      promoBlack.toNumeric must_== "42411"

      CastlingShort(White).toNumeric must_== "5171"
      CastlingShort(Black).toNumeric must_== "5878"
      CastlingLong(White).toNumeric must_== "5131"
      CastlingLong(Black).toNumeric must_== "5838"
    }

    "correctly perform toLongAlgebraic and toLongFigurine" in {
      moveWhite.toLongAlgebraic must_== "Qa1-h8"
      moveBlack.toLongAlgebraic must_== "Qa1-h8"
      moveWhite.toLongFigurine must_== "♕a1-h8"
      moveBlack.toLongFigurine must_== "♛a1-h8"

      promoWhite.toLongAlgebraic must_== "d7-d8Q"
      promoBlack.toLongAlgebraic must_== "d2-d1Q"
      promoWhite.toLongFigurine must_== "♙d7-d8♕"
      promoBlack.toLongFigurine must_== "♟d2-d1♛"

      enPassantWhite.toLongAlgebraic must_== "e5xf6e.p."
      enPassantBlack.toLongAlgebraic must_== "b4xa3e.p."
      enPassantWhite.toLongFigurine must_== "♙e5xf6e.p."
      enPassantBlack.toLongFigurine must_== "♟b4xa3e.p."

      captWhite.toLongAlgebraic must_== "Na1xc2"
      captBlack.toLongAlgebraic must_== "Na1xc2"
      captWhite.toLongFigurine must_== "♘a1xc2"
      captBlack.toLongFigurine must_== "♞a1xc2"

      captPromoWhite.toLongAlgebraic must_== "a7xb8Q"
      captPromoBlack.toLongAlgebraic must_== "a2xb1Q"
      captPromoWhite.toLongFigurine must_== "♙a7xb8♕"
      captPromoBlack.toLongFigurine must_== "♟a2xb1♛"

      CastlingShort(White).toLongAlgebraic must_== "0-0"
      CastlingShort(Black).toLongAlgebraic must_== "0-0"
      CastlingShort(White).toLongFigurine must_== "0-0"
      CastlingShort(Black).toLongFigurine must_== "0-0"

      CastlingLong(White).toLongAlgebraic must_== "0-0-0"
      CastlingLong(Black).toLongAlgebraic must_== "0-0-0"
      CastlingLong(White).toLongFigurine must_== "0-0-0"
      CastlingLong(Black).toLongFigurine must_== "0-0-0"
    }

    "correctly perform toPureCoordinate" in {
      moveWhite.toCoordinate must_== "a1h8"
      moveBlack.toCoordinate must_== "a1h8"

      promoWhite.toCoordinate must_== "d7d8q"
      promoBlack.toCoordinate must_== "d2d1q"

      captPromoWhite.toCoordinate must_== "a7b8q"
      captPromoBlack.toCoordinate must_== "a2b1q"

      CastlingShort(White).toCoordinate must_== "e1g1"
      CastlingShort(Black).toCoordinate must_== "e8g8"

      CastlingLong(White).toCoordinate must_== "e1c1"
      CastlingLong(Black).toCoordinate must_== "e8c8"
    }
  }
}

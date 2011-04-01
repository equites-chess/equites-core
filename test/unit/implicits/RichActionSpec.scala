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
    val moveWhite = Move(new Queen(White), Field(0, 0), Field(7, 7))
    val moveBlack = Move(new Queen(Black), Field(0, 0), Field(7, 7))

    val promotionWhite = Promotion(new Pawn(White), Field(3, 6), Field(3, 7))
    val promotionBlack = Promotion(new Pawn(Black), Field(3, 1), Field(3, 0))

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
  }
}

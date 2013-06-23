// Equites, a Scala chess playground
// Copyright © 2011-2013 Frank S. Thomas <frank@timepit.eu>
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

import org.specs2.ScalaCheck
import org.specs2.mutable._

class PieceSpec extends Specification with ScalaCheck {
  "Piece" should {
    "correctly perform isFriendOf and isOpponentOf" in {
      King(White) isFriendOf Queen(White) must beTrue
      King(White) isFriendOf Queen(Black) must beFalse

      King(White) isOpponentOf Queen(White) must beFalse
      King(White) isOpponentOf Queen(Black) must beTrue
    }

    "be one of the six subtypes" in check {
      (p: Piece) => p.maybeKing   orElse
                    p.maybeQueen  orElse
                    p.maybeRook   orElse
                    p.maybeBishop orElse
                    p.maybeKnight orElse
                    p.maybePawn   must beSome(p)
    }
  }
}

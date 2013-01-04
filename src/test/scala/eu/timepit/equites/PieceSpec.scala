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

package eu.timepit.equites

import org.specs2.mutable._

class PieceSpec extends Specification {
  "class Piece" should {
    "correctly perform isFriendOf, isOpponentOf" in {
      val king1 = new King(White)
      val king2 = new King(White)
      val queen = new Queen(Black)

      king1 isFriendOf king1 must_== true
      king1 isFriendOf king2 must_== true
      king1 isFriendOf queen must_== false

      king1 isOpponentOf king1 must_== false
      king1 isOpponentOf king2 must_== false
      king1 isOpponentOf queen must_== true
    }
  }

  "objects with PieceCompanion" should {
    "correctly perform unapply" in {
      val King(c1) = new King(White)
      val King(c2) = new King(Black)

      c1 must_== White
      c2 must_== Black
    }
  }
}

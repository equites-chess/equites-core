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

package eu.timepit

import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary

package object equites {
  implicit val arbitraryColor: Arbitrary[Color] =
    Arbitrary(Gen.oneOf(Color.values))

  implicit def arbitraryPlaced[A : Arbitrary]: Arbitrary[Placed[A]] =
    Arbitrary {
      for {
        elem <- arbitrary[A]
        square <- arbitrary[Square]
      } yield Placed(elem, square)
    }

  implicit val arbitrarySquare: Arbitrary[Square] =
    Arbitrary(Gen.oneOf(Rules.allSquares))

  implicit val arbitraryVec: Arbitrary[Vec] = Arbitrary {
    for {
      file <- arbitrary[Int]
      rank <- arbitrary[Int]
    } yield Vec(file, rank)
  }
}

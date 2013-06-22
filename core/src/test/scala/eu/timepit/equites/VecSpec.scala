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
import org.specs2.matcher.DataTables
import org.specs2.mutable._
import scalaz.scalacheck.ScalazProperties._

class VecSpec extends Specification with DataTables with ScalaCheck {
  "Vec" should {
    "satisfy the Equal laws" in check(equal.laws[Vec])
    "satisfy the Monoid laws" in check(monoid.laws[Vec])

    "correctly perform map" in {
      Vec( 0,  0).map(_ + 1) must_== Vec(1, 1)
      Vec(-1, -1).map(_.abs) must_== Vec(1, 1)
    }

    "correctly perform +(Vec)" in check {
      (x: Int, y: Int, a: Int, b: Int) =>
        Vec(x, y) + Vec(a, b) must_== Vec(x + a, y + b)
    }

    "correctly perform -(Vec)" in check {
      (x: Int, y: Int, a: Int, b: Int) =>
        Vec(x, y) - Vec(a, b) must_== Vec(x - a, y - b)
    }

    "correctly perform unary_-" in check {
      (x: Int, y: Int) => -Vec(x, y) must_== Vec(-x, -y)
    }

    "correctly perform *(Int)" in check {
      (x: Int, y: Int, z: Int) => Vec(x, y) * z must_== Vec(x * z, y * z)
    }

    "correctly perform /(Int)" in check {
      (x: Int, y: Int, z: Int) =>
        (z != 0) ==> (Vec(x, y) / z must_== Vec(x / z, y / z))
    }

    "correctly perform max" in check {
      (x: Int, y: Int) => Vec(x, y).max must_== math.max(x, y)
    }

    "correctly perform min" in check {
      (x: Int, y: Int) => Vec(x, y).min must_== math.min(x, y)
    }

    "correctly perform sum" in check {
      (x: Int, y: Int) => Vec(x, y).sum must_== x + y
    }

    "correctly perform reduced" in {
      "Vec"       | "reduced"   |
      Vec( 0,  0) ! Vec( 0,  0) |
      Vec( 1,  0) ! Vec( 1,  0) |
      Vec( 1,  1) ! Vec( 1,  1) |
      Vec( 2,  1) ! Vec( 2,  1) |
      Vec( 2,  2) ! Vec( 1,  1) |
      Vec( 3,  2) ! Vec( 3,  2) |
      Vec( 4,  2) ! Vec( 2,  1) |
      Vec(-4,  2) ! Vec(-2,  1) |
      Vec( 4, -2) ! Vec( 2, -1) |
      Vec(-4, -2) ! Vec(-2, -1) |> {
        (v, r) => v.reduced must_== r
      }
    }

    "be decomposable into component vectors" in check {
      (v: Vec) => v must_== v.fileProj + v.rankProj
    }

    "not be straight and diagonal" in check {
      (v: Vec) => (v.isStraight || v.isDiagonal) ==> {
        v.isStraight must_!= v.isDiagonal
      }
    }
  }
}

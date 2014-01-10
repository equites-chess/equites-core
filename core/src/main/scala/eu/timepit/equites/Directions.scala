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

import scala.collection.immutable._

import implicits.GenericImplicits._

object Directions {
  def apply(vs: Vec*): Directions = apply(vs)
  def apply(vs: TraversableOnce[Vec]): Directions = new Directions(vs.toSet)

  // format: OFF
  val front = Directions(Vec.front) // ↑
  val right = Directions(Vec.right) // →
  val back  = Directions(Vec.back)  // ↓
  val left  = Directions(Vec.left)  // ←

  val frontRight = Directions(Vec.frontRight) // ↗
  val backRight  = Directions(Vec.backRight)  // ↘
  val backLeft   = Directions(Vec.backLeft)   // ↙
  val frontLeft  = Directions(Vec.frontLeft)  // ↖

  val diagonalFront = Directions(frontLeft ++ frontRight) // ↖↗
  val diagonalBack  = Directions( backLeft ++  backRight) // ↙↘

  val forward  = Directions(diagonalFront ++ front) // ↖↑↗
  val backward = Directions(diagonalBack  ++ back)  // ↙↓↘

  val straight = Directions(front ++ right ++ back ++ left) // ↑→↓←
  val diagonal = Directions(diagonalFront ++ diagonalBack)  // ↖↗↙↘
  val anywhere = Directions(straight ++ diagonal)
  // format: ON

  val knightLike =
    Directions((Vec(-2, -2) to Vec(2, 2)).filter(_.l1Length == 3))
}

class Directions(val self: Set[Vec]) extends SetProxy[Vec]
    with PlayerPerspective[Directions] {

  def inverse: Directions = Directions(map(_.inverse))

  def mostSimilarTo(vec: Vec): Directions =
    Directions(self.minGroupBy((dir: Vec) => (vec - dir).l1Length))
}

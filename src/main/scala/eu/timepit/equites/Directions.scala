// Equites, a Scala chess playground
// Copyright © 2011-2014 Frank S. Thomas <frank@timepit.eu>
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

import implicits.GenericImplicits._
import util.PlayerPerspective

case class Directions(self: Set[Vec]) extends PlayerPerspective[Directions] {
  def ++(other: Directions): Directions = Directions(self ++ other.self)

  def contains(dir: Vec): Boolean = self.contains(dir)

  def toStream = self.toStream

  def inverse: Directions = Directions(self.map(_.inverse))

  def mostSimilarTo(vec: Vec): Directions =
    Directions(self.minGroupBy((dir: Vec) => (vec - dir).l1Length))
}

object Directions {
  def apply(vs: Vec*): Directions = Directions(vs)
  def apply(vs: TraversableOnce[Vec]): Directions = Directions(vs.toSet)

  // format: OFF
  val front = Directions(Vec.front)
  val right = Directions(Vec.right)
  val back  = Directions(Vec.back)
  val left  = Directions(Vec.left)

  val frontRight = Directions(Vec.frontRight)
  val backRight  = Directions(Vec.backRight)
  val backLeft   = Directions(Vec.backLeft)
  val frontLeft  = Directions(Vec.frontLeft)

  val diagonalFront = frontLeft ++ frontRight
  val diagonalBack  = backLeft ++ backRight

  val forward  = diagonalFront ++ front
  val backward = diagonalBack ++ back

  val horizontal = right ++ left
  val vertical   = front ++ back

  val straight = horizontal ++ vertical
  val diagonal = diagonalFront ++ diagonalBack
  val anywhere = straight ++ diagonal
  // format: ON

  val knightLike = {
    val moveLength = 3
    val directions = Vec(-2, -2) rectangleTo Vec(2, 2)
    Directions(directions.filter(_.l1Length == moveLength))
  }
}

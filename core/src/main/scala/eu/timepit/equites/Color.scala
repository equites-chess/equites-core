// Equites, a Scala chess playground
// Copyright © 2011, 2013-2014 Frank S. Thomas <frank@timepit.eu>
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

import scalaz._

trait ColorInstances {
  implicit val colorInstance = new Order[Color] {
    // Order
    def order(c1: Color, c2: Color): Ordering =
      if (c1 == c2) {
        Ordering.EQ
      } else if (c1 == White) {
        Ordering.GT
      } else {
        Ordering.LT
      }
  }

  implicit val colorEqual = Equal.equalA[Color]
  implicit val colorScalaOrdering = Order[Color].toScalaOrdering
  implicit val colorShow = Show.showFromToString[Color]
}

object Color extends ColorInstances {
  def all: List[Color] = List(White, Black)

  def guessFrom(direction: Vec): Option[Color] =
    direction.rankProj.reduced match {
      case Vec.front => Some(White)
      case Vec.back  => Some(Black)
      case _         => None
    }
}

sealed trait Color {
  type Opposite <: Color
  def opposite: Opposite
}

case object White extends Color {
  override type Opposite = Black.type
  override def opposite: Opposite = Black
}

case object Black extends Color {
  override type Opposite = White.type
  override def opposite: Opposite = White
}

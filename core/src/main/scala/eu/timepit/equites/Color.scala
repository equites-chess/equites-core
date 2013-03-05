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

import scalaz._

trait ColorInstances {
  implicit object colorInstance extends Equal[Color] with Order[Color] {
    // Equal
    override def equal(c1: Color, c2: Color): Boolean = c1 == c2
    // Order
    def order(c1: Color, c2: Color): Ordering = c1 match {
      case _ if c1 == c2 => Ordering.EQ
      case White         => Ordering.GT
      case _             => Ordering.LT
    }
  }
}

object Color extends ColorInstances {
  def values: Seq[Color] = Seq(White, Black)
}

sealed trait Color {
  def opposite: Color
}

case object White extends Color {
  override def opposite: Color = Black
}

case object Black extends Color {
  override def opposite: Color = White
}

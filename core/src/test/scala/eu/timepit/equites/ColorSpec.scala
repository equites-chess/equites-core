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

import org.specs2._
import scalaz.scalacheck.ScalazProperties._
import shapeless.test.illTyped

import ArbitraryInstances._

class ColorSpec extends Specification with ScalaCheck { def is = s2"""
  Color should
    satisfy the Equal laws $e1
    satisfy the Order laws $e2

  Black should be the opposite of White $e3
  White should be the opposite of Black $e4
  """

  def e1 = check(equal.laws[Color])
  def e2 = check(order.laws[Color])

  def e3 = White.opposite must_== Black
  def e4 = Black.opposite must_== White

  def testOppositeType(): Unit = {
    def isOpposite(color: Color)(opposite: color.Opposite): Unit = ()

    isOpposite(White)(Black)
    isOpposite(Black)(White)

    illTyped("isOpposite(White)(White)")
    illTyped("isOpposite(Black)(Black)")
  }
}

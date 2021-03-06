// Equites, a Scala chess playground
// Copyright © 2011-2013, 2015 Frank S. Thomas <frank@timepit.eu>
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

import eu.timepit.equites.ArbitraryInstances._
import org.specs2._
import shapeless.test.illTyped

import scalaz.scalacheck.ScalazProperties._

class ColorSpec extends Specification with ScalaCheck {
  def is = s2"""
  Color
    should satisfy the Equal laws ${equal.laws[Color]}
    should satisfy the Order laws ${order.laws[Color]}

    guessFrom should return no color for horizontal directions $e1
    guessFrom should return all colors for vertical directions $e2

  Black should be the opposite of White $e3
  White should be the opposite of Black $e4
  """

  def e1 = Directions.horizontal.self.map(Color.guessFrom).flatten == Set.empty
  def e2 = Directions.vertical.self.map(Color.guessFrom).flatten == Set(White, Black)

  def e3 = White.opposite must_== Black
  def e4 = Black.opposite must_== White

  def testOppositeType(): Unit = {
    def checkOpposite(color: Color)(opposite: color.Opposite): Unit = ()

    checkOpposite(White)(Black)
    checkOpposite(Black)(White)

    illTyped("checkOpposite(White)(White)")
    illTyped("checkOpposite(Black)(Black)")
  }
}

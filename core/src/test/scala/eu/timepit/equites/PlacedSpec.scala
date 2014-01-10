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

package eu.timepit.equites

import org.specs2._
import scalaz.scalacheck.ScalazProperties._

import ArbitraryInstances._

class PlacedSpec extends Specification with ScalaCheck { def is = s2"""
  Placed should
    satisfy the Equal laws   $e1
    satisfy the Functor laws $e2
    satisfy the Order laws   $e3
  """

  def e1 = check(equal.laws[Placed[AnyPiece]])
  def e2 = check(functor.laws[Placed])
  def e3 = check(order.laws[Placed[AnyPiece]])
}

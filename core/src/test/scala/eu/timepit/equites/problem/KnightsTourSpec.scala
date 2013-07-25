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
package problem

import org.specs2.mutable._

import KnightsTour._

class KnightsTourSpec extends Specification {
  def alwaysVisitAllSquares(tourFn: Square => Tour) = {
    "visit all squares from all starting squares" in {
      Rules.allSquaresSet.forall(sq => isComplete(tourFn(sq))) must beTrue
    }
  }

  def produceOneClosedTour(tourFn: Square => Tour) = {
    "produce at least one closed tour" in {
      Rules.allSquaresSet.exists(sq => isClosed(tourFn(sq))) must beTrue
    }
  }

  "staticTour" should {
    "not generate a complete tour" in {
      isComplete(staticTour(Square(0, 0))) must beFalse
    }
  }

  "randomTour" should {
    "not generate a complete tour" in {
      isComplete(randomTour(Square(0, 0))) must beFalse
    }
  }

  "warnsdorffTour" should {
    alwaysVisitAllSquares(warnsdorffTour)
    produceOneClosedTour(warnsdorffTour)
  }

  "randomWarnsdorffTour" should {
    alwaysVisitAllSquares(randomWarnsdorffTour)
    produceOneClosedTour(randomWarnsdorffTour)
  }
}

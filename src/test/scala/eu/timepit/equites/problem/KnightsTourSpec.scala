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

import problem.KnightsTour._
import util.SquareAbbr._

class KnightsTourSpec extends Specification {
  def alwaysVisitAllSquares(tourFn: Square => Tour) = {
    "visit all squares from all starting squares" in {
      Square.allAsSeq.forall(tourFn.andThen(isComplete)) must beTrue
    }
  }

  def produceOneClosedTour(tourFn: Square => Tour) = {
    "produce at least one closed tour" in {
      Square.allAsSeq.exists(tourFn.andThen(isClosed)) must beTrue
    }
  }

  /*
  "staticTour" should {
    "not generate a complete tour" in {
      isComplete(staticTour(a1)) must beFalse
    }
  }

  "randomTour" should {
    "not generate a complete tour" in {
      isComplete(randomTour(a1)) must beFalse
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
*/
}

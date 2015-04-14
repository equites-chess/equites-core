// Equites, a Scala chess playground
// Copyright © 2013, 2015 Frank S. Thomas <frank@timepit.eu>
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

import com.nicta.rng.Rng
import eu.timepit.equites.problem.KnightsTour._
import eu.timepit.equites.util.SquareAbbr._
import eu.timepit.scalaz.stream.contrib.processC._
import eu.timepit.scalaz.stream.contrib.unsafe._
import org.specs2.mutable._

import scalaz.stream.Process

class KnightsTourSpec extends Specification {
  private def runToStream[A](p: Process[Rng, A]): Stream[A] =
    p.runLog.run.unsafePerformIO().toStream

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

  "staticTour" should {
    "not generate a complete tour" in {
      isComplete(staticTour(a1).toStream) must beFalse
    }
  }

  "randomTour" should {
    "not generate a complete tour" in {
      isComplete(runToStream(randomTour(a1))) must beFalse
    }
  }

  "warnsdorffTour" should {
    val fn = (warnsdorffTour _).andThen(_.toStream)
    alwaysVisitAllSquares(fn)
    produceOneClosedTour(fn)
  }

  "randomWarnsdorffTour" should {
    val fn = (randomWarnsdorffTour _).andThen(runToStream)
    produceOneClosedTour(fn)
  }
}

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

import scalaz._
import Scalaz._
import scalaz.std.stream

import util.Rand._

object KnightsTour {
  type Selector = (Stream[Square], Set[Square]) => Option[Square]

  def genericTour(start: Square, selectNext: Selector): Stream[Square] =
    start #:: stream.unfold((start, Set[Square]())) {
      case (from, visited) => {
        val nextOption = selectNext(unvisited(from, visited), visited)
        nextOption.map(next => (next, (next, visited + from)))
      }
    }

  def staticTour(start: Square): Stream[Square] =
    genericTour(start, (squares, _) => squares.headOption)

  // impure
  def randomTour(start: Square): Stream[Square] =
    genericTour(start, (squares, _) => pickRandomImpure(squares))

  def warnsdorffTour(start: Square): Stream[Square] =
    genericTour(start, firstLeastDegreeSquare)

  // impure
  def randomWarnsdorffTour(start: Square): Stream[Square] = {
    Iterator.continually(genericTour(start, evalFn2(randomLeastDegreeSquare)))
      .find(_.length == Rules.allSquares.length).get
  }

  def leastDegreeSquares(squares: Stream[Square], visited: Set[Square])
      : Stream[Square] = {
    val grouped = squares.groupBy(sq => unvisited(sq, visited).length)
    if (grouped.nonEmpty) {
      val (_, ldSquares) = grouped.minBy { case (degree, _) => degree }
      ldSquares
    } else {
      Stream.empty[Square]
    }
  }

  def firstLeastDegreeSquare: Selector =
    (squares, visited) => leastDegreeSquares(squares, visited).headOption

  def randomLeastDegreeSquare(squares: Stream[Square], visited: Set[Square])
      : Rand[Option[Square]] =
    pickRandom(leastDegreeSquares(squares, visited))

  def isClosed(tour: Seq[Square]): Boolean =
    tour.nonEmpty && Directions.knightLike.contains(tour.last - tour.head)

  private def unvisited(from: Square, visited: Set[Square]) =
    Rules.unvisitedSquares(Placed(Knight(White), from), visited)
}

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

import implicits.GenericImplicits._
import util.Rand._

object KnightsTour {
  type Tour = Stream[Square]
  type Selector = (Stream[Square], Set[Square]) => Option[Square]
  type RandSelector = (Stream[Square], Set[Square]) => Rand[Option[Square]]

  def genericTour(start: Square, selectNext: Selector): Tour =
    start #:: stream.unfold((start, Set.empty[Square])) {
      case (from, visited) => {
        val nextOption = selectNext(unvisited(from, visited), visited)
        nextOption.map(next => (next, (next, visited + from)))
      }
    }

  def allTours(start: Square): Stream[Tour] = {
    def nextPaths(cand: (Tour, Set[Square])): Stream[(Tour, Set[Square])] = {
      val (path, visited) = cand
      val nextSquares = path.headOption.toStream.flatMap {
        from => unvisited(from, visited)
      }
      nextSquares.map(sq => (sq #:: path, visited + sq))
    }

    val first = (Stream(start), Set(start))
    util.backtracking(first)(nextPaths, cand => isComplete(cand._1)).map(_._1)
  }

  def staticTour(start: Square): Tour =
    genericTour(start, (squares, _) => squares.headOption)

  // impure
  def randomTour(start: Square): Tour =
    genericTour(start, (squares, _) => pickRandomImpure(squares))

  def warnsdorffTour(start: Square): Tour =
    genericTour(start, firstLeastDegreeSquare)

  // impure
  def randomWarnsdorffTour(start: Square): Tour = {
    def tour = genericTour(start, evalFn2(randomLeastDegreeSquare))
    Iterator.continually(tour).find(isComplete).get
  }

  def leastDegreeSquares(squares: Stream[Square], visited: Set[Square])
      : Stream[Square] = {
    def degree(sq: Square): Int = unvisited(sq, visited).length
    squares.minGroupBy(degree)
  }

  def firstLeastDegreeSquare: Selector =
    (squares, visited) => leastDegreeSquares(squares, visited).headOption

  def randomLeastDegreeSquare: RandSelector =
    (squares, visited) => pickRandom(leastDegreeSquares(squares, visited))

  def isComplete(tour: Tour): Boolean =
    tour.toSet == Rules.allSquaresSet

  def isClosed(tour: Tour): Boolean =
    tour.nonEmpty && Directions.knightLike.contains(tour.last - tour.head)

  private def unvisited(from: Square, visited: Set[Square]): Stream[Square] =
    Rules.unvisitedSquares(Placed(Knight(White), from), visited)
}

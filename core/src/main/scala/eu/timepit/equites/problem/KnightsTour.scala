// Equites, a Scala chess playground
// Copyright © 2013-2014 Frank S. Thomas <frank@timepit.eu>
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
    case class Candidate(path: Tour, visited: Set[Square])

    def nextPaths(c: Candidate): Stream[Candidate] = {
      val nextSquares = c.path.headOption.toStream.flatMap {
        from => unvisited(from, c.visited)
      }
      nextSquares.map(sq => Candidate(sq #:: c.path, c.visited + sq))
    }

    val first = Candidate(Stream(start), Set(start))
    util.backtrack(first)(nextPaths, c => isComplete(c.path)).map(_.path)
  }

  def staticTour(start: Square): Tour =
    genericTour(start, (squares, _) => squares.headOption)

  // impure
  def randomTour(start: Square): Tour =
    genericTour(start, (squares, _) => eval(randElem(squares)).run)

  def warnsdorffTour(start: Square): Tour =
    genericTour(start, firstLeastDegreeSquare)

  // impure
  def randomWarnsdorffTour(start: Square): Tour = {
    def tour = eval(randomLeastDegreeSquare).map(s => genericTour(start, s)).run
    Iterator.continually(tour).find(isComplete).get
  }

  def leastDegreeSquares(squares: Stream[Square], visited: Set[Square]): Stream[Square] = {
    def degree(sq: Square): Int = unvisited(sq, visited).length
    squares.minGroupBy(degree)
  }

  def firstLeastDegreeSquare: Selector =
    (squares, visited) => leastDegreeSquares(squares, visited).headOption

  def randomLeastDegreeSquare: RandSelector =
    (squares, visited) => randElem(leastDegreeSquares(squares, visited))

  def isComplete(tour: Tour): Boolean =
    tour.toSet == Square.allAsSet

  def isClosed(tour: Tour): Boolean =
    tour.nonEmpty && Directions.knightLike.contains(tour.last - tour.head)

  private def unvisited(from: Square, visited: Set[Square]): Stream[Square] = {
    val placed = Placed(Piece(White, Knight), from)
    Rules.unvisitedSquares(placed, visited)
  }
}

// Equites, a Scala chess playground
// Copyright © 2013-2015 Frank S. Thomas <frank@timepit.eu>
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
import eu.timepit.equites.implicits.GenericImplicits._
import eu.timepit.equites.util.RngUtil.chooseElem

import scalaz.Scalaz._
import scalaz._
import scalaz.stream.Process

object KnightsTour {
  type Tour = Stream[Square]
  type Selector[F[_]] = (Stream[Square], Set[Square]) => F[Option[Square]]

  def allTours(start: Square): Stream[Tour] = {
    case class Candidate(path: Tour, visited: Set[Square])

    def nextPaths(c: Candidate): Stream[Candidate] = {
      val nextSquares = c.path.headOption.toStream.flatMap { from =>
        unvisited(from, c.visited).sortBy(degree(_, c.visited))
      }
      nextSquares.map(sq => Candidate(sq #:: c.path, c.visited + sq))
    }

    val first = Candidate(Stream(start), Set(start))
    util.backtrack(first)(nextPaths, c => isComplete(c.path)).map(_.path)
  }

  def genericTour[F[_]](start: Square, selectNext: Selector[F])(implicit F: Functor[F]): Process[F, Square] =
    Process.emit(start) ++ Process.unfoldEval((start, Set.empty[Square])) {
      case (from, visited) =>
        val nextFOption = selectNext(unvisited(from, visited), visited)
        F.map(nextFOption)(_.map(next => (next, (next, visited + from))))
    }

  def staticTour(start: Square): Process[Id, Square] =
    genericTour(start, firstSquare)

  def warnsdorffTour(start: Square): Process[Id, Square] =
    genericTour(start, firstLeastDegreeSquare)

  def randomTour(start: Square): Process[Rng, Square] =
    genericTour(start, randomSquare)

  def randomWarnsdorffTour(start: Square): Process[Rng, Square] =
    genericTour(start, randomLeastDegreeSquare)

  def leastDegreeSquares(squares: Stream[Square], visited: Set[Square]): Stream[Square] =
    squares.minGroupBy((sq: Square) => degree(sq, visited))

  val firstSquare: Selector[Id] =
    (squares, _) => squares.headOption

  val firstLeastDegreeSquare: Selector[Id] =
    (squares, visited) => leastDegreeSquares(squares, visited).headOption

  val randomSquare: Selector[Rng] =
    (squares, _) => chooseElem(squares)

  val randomLeastDegreeSquare: Selector[Rng] =
    (squares, visited) => chooseElem(leastDegreeSquares(squares, visited))

  def isComplete(tour: Tour): Boolean =
    tour.toSet == Square.allAsSet

  def isClosed(tour: Tour): Boolean =
    tour.nonEmpty && Directions.knightLike.contains(tour.last - tour.head)

  def degree(square: Square, visited: Set[Square]): Int =
    unvisited(square, visited).length

  def unvisited(from: Square, visited: Set[Square]): Stream[Square] = {
    val placed = Placed(Piece(White, Knight), from)
    Rules.unvisitedSquares(placed, visited)
  }
}

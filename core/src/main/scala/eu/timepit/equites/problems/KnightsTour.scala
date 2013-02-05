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
package problems

import scalaz.std.stream

object KnightsTour {
  type Selector = (Seq[Square], Set[Square]) => Option[Square]

  def genericTour(start: Square, selectNext: Selector): Stream[Square] =
    start #:: stream.unfold((start, Set[Square]())) {
      case (from, visited) => {
        val nextOption = selectNext(unvisited(from, visited), visited)
        nextOption.map(next => (next, (next, visited + from)))
      }
    }

  def staticTour(start: Square) =
    genericTour(start, (squares, _) => squares.headOption)

  // impure
  def randomTour(start: Square) =
    genericTour(start, (squares, _) => util.pickRandom(squares))

  def warnsdorffTour(start: Square) =
    genericTour(start, leastDegreeSquare)

  def leastDegreeSquare: Selector = (squares, visited) =>
    squares.sortBy(sq => unvisited(sq, visited).length).headOption

  // impure
  def randomWarnsdorffTour(start: Square) = {
    val squaresCount = Rules.allSquares.length
    Iterator.continually(genericTour(start, randomLeastDegreeSquare))
      .find(_.length == squaresCount).get
  }

  // impure
  def randomLeastDegreeSquare: Selector = (squares, visited) => {
    val grouped = squares.groupBy(sq => unvisited(sq, visited).length)
    if (grouped.isEmpty) None
    else util.pickRandom(grouped.minBy(_._1)._2)
  }

  def isClosed(tour: Seq[Square]): Boolean = {
    if (tour.isEmpty) false
    else Directions.knightLike contains (tour.last - tour.head)
  }

  private def knightOn(square: Square) = PlacedPiece(Knight(White), square)

  private def unvisited(from: Square, visited: Set[Square]) =
    Rules.unvisitedSquares(knightOn(from), visited)
}

// Equites, a simple chess interface
// Copyright © 2013 Frank S. Thomas <f.thomas@gmx.de>
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
  type Selector = (Stream[Square], Set[Square]) => Option[Square]

  def genericTour(start: Square, select: Selector): Stream[Square] = {
    start #:: stream.unfold((start, Set[Square]())) {
      case (from, visited) => {
        val nextOption = select(unvisited(from, visited), visited)
        nextOption.map(next => (next, (next, visited + from)))
      }
    }
  }

  def staticTour(start: Square) =
    genericTour(start, (squares, _) => squares.headOption)

  // impure
  def randomTour(start: Square) =
    genericTour(start, (squares, _) => util.pickRandom(squares))

  def warnsdorffTour(start: Square) =
    genericTour(start, (squares, visited) =>
      squares.sortBy(sq => unvisited(sq, visited).length).headOption)

  private def knightOn(square: Square) = PlacedPiece(Knight(White), square)

  private def unvisited(from: Square, visited: Set[Square]) =
    Rules.unvisitedSquares(knightOn(from), visited)
}

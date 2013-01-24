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

import annotation.tailrec

object KnightsTour {
  type Selector = (Stream[Square], Set[Square]) => Option[Square]

  def genericTour(start: Square, select: Selector): Vector[Square] = {
    @tailrec
    def accumulate(from: Square, visited: Set[Square], acc: Vector[Square]):
        Vector[Square] = {
      val newAcc = acc :+ from
      select(unvisited(from, visited), visited) match {
        case Some(next) => accumulate(next, visited + from, newAcc)
        case None => newAcc
      }
    }
    accumulate(start, Set(), Vector())
  }

  def staticTour(start: Square) =
    genericTour(start, (squares, _) => squares.headOption)

  def randomTour(start: Square) =
    genericTour(start, (squares, _) => util.pickRandom(squares))

  def warnsdorffTour(start: Square) =
    genericTour(start, (squares, visited) =>
      squares.sortBy(sq => unvisited(sq, visited).length).headOption)

  private def knightOn(square: Square): PlacedPiece =
    PlacedPiece(Knight(White), square)

  private def unvisited(from: Square, visited: Set[Square]): Stream[Square] =
    Rules.possibleSquaresOf(knightOn(from)).filterNot(visited(_))
}

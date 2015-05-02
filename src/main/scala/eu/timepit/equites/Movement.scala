// Equites, a Scala chess playground
// Copyright © 2011, 2013-2015 Frank S. Thomas <frank@timepit.eu>
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

import eu.timepit.equites.Directions._
import eu.timepit.equites.Rules._
import eu.timepit.equites.implicits.PlacedImplicits._
import eu.timepit.equites.util.PieceAbbr.Textual._

import scalaz.std.stream

case class Movement(directions: Directions, distance: Int)

object Movement {
  val pieceMovements: Map[AnyPiece, Movement] = {
    def pieceMovementsBy(color: Color): Map[AnyPiece, Movement] = {
      // format: OFF
      Map(
        king(color)   -> Movement(anywhere, 1),
        queen(color)  -> Movement(anywhere, maxBoardLength),
        rook(color)   -> Movement(straight, maxBoardLength),
        bishop(color) -> Movement(diagonal, maxBoardLength),
        knight(color) -> Movement(knightLike, 1),
        pawn(color)   -> Movement(front.fromViewOf(color), 1))
      // format: ON
    }
    Color.all.map(pieceMovementsBy).reduce(_ ++ _)
  }

  val attackPieceMovements: Map[AnyPiece, Movement] =
    pieceMovements.map {
      case (p @ Piece(color, Pawn), _) => p -> Movement(diagonalFront.fromViewOf(color), 1)
      case other                       => other
    }

  def movementOf(placed: PlacedPiece): Movement = {
    val movement = pieceMovements(placed)
    placed.pieceType match {
      case Pawn if onStartingSquare(placed) => movement.copy(distance = 2)
      case _                                => movement
    }
  }

  def attackMovementOf(placed: PlacedPiece): Movement =
    attackPieceMovements(placed)

  def squaresInDirection(from: Square, direction: Vec): Stream[Square] =
    stream.unfold(from)(sq => (sq + direction).map(util.toTuple2))

  def directedReachableSquares(placed: PlacedPiece, movement: Movement): Stream[Stream[Square]] =
    movement.directions.toStream.map { dir =>
      squaresInDirection(placed.square, dir).take(movement.distance)
    }

  def undirectedReachableSquares(placed: PlacedPiece): Stream[Square] =
    directedReachableSquares(placed, movementOf(placed)).flatten

  def unvisitedSquares(placed: PlacedPiece, visited: Set[Square]): Stream[Square] =
    undirectedReachableSquares(placed).filterNot(visited)

  def reachableVacantSquares(placed: PlacedPiece, board: Board): Stream[Square] =
    directedReachableSquares(placed, movementOf(placed)).flatMap {
      _.takeWhile(board.isVacant)
    }

  def reachableOccupiedSquares(placed: PlacedPiece, board: Board): Stream[PlacedPiece] =
    directedReachableSquares(placed, attackMovementOf(placed)).flatMap {
      _.flatMap(sq => board.getPlaced(sq).toList).take(1)
    }
}

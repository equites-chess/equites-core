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

import scalaz.std.stream

import implicits.GenericImplicits._

case class GameState(
    board: Board,
    lastAction: Option[Action],
    color: Color,
    moveNumber: Int,
    halfmoveClock: Int,
    availableCastlings: Set[Castling]) {

  def updated(action: Action): GameState = copy(
    board = board.processAction(action),
    lastAction = Some(action),
    color = color.opposite,
    moveNumber = updatedMoveNumber,
    halfmoveClock = updatedHalfmoveClock(action),
    availableCastlings = updatedAvailableCastlings(action))

  def updated(ca: util.CoordinateAction): Option[GameState] =
    ActionOps.reifyAction(ca, board).map(updated)

  private[this] def updatedMoveNumber: Int =
    moveNumber + color.fold(0, 1)

  private[this] def updatedHalfmoveClock(action: Action): Int = {
    val reset = ActionOps.isCapture(action) || ActionOps.isPawnMove(action)
    if (reset) 0 else halfmoveClock + 1
  }

  private[this] def updatedAvailableCastlings(action: Action): Set[Castling] = {
    def unavailableCastlings: Seq[Castling] = action match {
      case castling: Castling =>
        Castling.allBy(castling.color)
      case capture: CaptureLike =>
        Rules.associatedCastlings(capture.placedPiece, capture.placedCaptured)
      case move: MoveLike =>
        Rules.associatedCastlings(move.placedPiece)
    }

    availableCastlings lazy_-- unavailableCastlings
  }
}

object GameState {
  def init: GameState = GameState(
    board = Rules.startingBoard,
    lastAction = None,
    color = White,
    moveNumber = 1,
    halfmoveClock = 0,
    availableCastlings = Castling.all.toSet)

  def unfold(actions: Seq[Action], first: GameState = init): Stream[GameState] =
    first #:: stream.unfold((first, actions)) {
      case (state, remActions) => remActions.headOption.map { action =>
        val updated = state.updated(action)
        (updated, (updated, remActions.tail))
      }
    }
}

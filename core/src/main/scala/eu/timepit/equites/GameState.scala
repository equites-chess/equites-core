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

object GameState {
  def init: GameState = GameState(board = Rules.startingBoard,
    lastAction = None, color = White, moveNumber = 1, halfmoveClock = 0,
    availableCastlings = Rules.allCastlings.toSet)
}

case class GameState(board: Board, lastAction: Option[Action],
  color: Color, moveNumber: Int, halfmoveClock: Int,
  availableCastlings: Set[Castling]) {

  def updated(action: Action): GameState = copy(
    board = board.processAction(action), lastAction = Some(action),
    color = color.opposite, moveNumber = updatedMoveNumber,
    halfmoveClock = updatedHalfmoveClock(action),
    availableCastlings = updatedAvailableCastlings(action))

  private[this] def updatedMoveNumber: Int =
    moveNumber + (if (color == Black) 1 else 0)

  private[this] def updatedHalfmoveClock(action: Action): Int =
    if (ActionOps.isCaptureOrPawnMove(action)) 0 else halfmoveClock + 1

  private[this] def updatedAvailableCastlings(action: Action): Set[Castling] = {
    def unavailableCastlings: Seq[Castling] = action match {
      case castling: Castling => Rules.castlingsBy(castling.color)
      case capture: CaptureLike =>
        Rules.associatedCastlings(capture.placedPiece) ++
        Rules.associatedCastlings(capture.placedCaptured)
      case move: MoveLike =>
        Rules.associatedCastlings(move.placedPiece)
    }

    if (availableCastlings.nonEmpty) {
      availableCastlings -- unavailableCastlings
    } else {
      Set.empty
    }
  }
}

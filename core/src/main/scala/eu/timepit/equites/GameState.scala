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
    lastAction = None, color = White, moveNumber = 1, halfmoveClock = 0)
}

case class GameState(board: Board, lastAction: Option[Action],
  color: Color, moveNumber: Int, halfmoveClock: Int) {

  def applyAction(action: Action): GameState = copy(
    board = board.processAction(action), lastAction = Some(action),
    color = color.opposite, moveNumber = nextMoveNumber,
    halfmoveClock = nextHalfmoveClock(action))

  private[this] def nextMoveNumber: Int =
    moveNumber + (if (color == Black) 1 else 0)

  private[this] def nextHalfmoveClock(action: Action): Int =
    if (ActionOps.isCaptureOrPawnMove(action)) 0 else halfmoveClock + 1
}

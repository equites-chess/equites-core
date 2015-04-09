// Equites, a Scala chess playground
// Copyright © 2014 Frank S. Thomas <frank@timepit.eu>
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
package format

import format.Pgn._

object PgnOps {
  def ff1(moveText: MoveTextSection): Seq[GameState] = ???

  def ff2(initial: GameState, l: List[SeqMoveElement]): Seq[GameState] = ???

  def ff4(action: SanAction, state: GameState): GameState =
    action match {
      case a: SanMoveLike          => ???
      case SanCastling(side)       => ???
      case CheckingSanAction(a, _) => ff4(a, state)
    }

  def ff5(move: SanMoveLike, state: GameState): Action = ???

  /// TDD wäre jetzt sinnvoll
}

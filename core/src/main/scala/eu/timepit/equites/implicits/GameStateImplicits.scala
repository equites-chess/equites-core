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
package implicits

import BoardImplicits._
import SquareImplicits._

object GameStateImplicits {
  implicit final class RichGameState(val self: GameState) extends AnyVal {
    def toFen: String = {
      val color = self.color.toString.charAt(0).toLower

      val castlings = {
        val letters = self.availableCastlings.map {
          case CastlingLong(White)  => "K"
          case CastlingShort(White) => "Q"
          case CastlingLong(Black)  => "k"
          case CastlingShort(Black) => "q"
        }.toSeq.sorted.mkString
        if (letters.isEmpty) "-" else letters
      }

      val enPassantTarget = self.lastAction.flatMap {
        case move: Move => ActionOps.enPassantTarget(move).map(_.toAlgebraic)
        case _          => None
      }.getOrElse("-")

      s"${self.board.toFenPlacement} ${color} ${castlings} ${enPassantTarget}" +
        s" ${self.halfmoveClock} ${self.moveNumber}"
    }

    def moveNumberIndicator: String =
      self.moveNumber.toString + (if (self.color == White) "." else "...")
  }
}

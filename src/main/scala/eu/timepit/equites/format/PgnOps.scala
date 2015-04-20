// Equites, a Scala chess playground
// Copyright © 2014-2015 Frank S. Thomas <frank@timepit.eu>
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

import eu.timepit.equites.format.Pgn._
import scalaz.Scalaz._
import scalaz._

import scalaz.Reader

object PgnOps {
  def construct(seqElems: List[SeqElem]): Reader[GameState, List[GameState]] = {
    val ri = Reader((state: GameState) => state)
    val xs = ri :: seqElems.map(update)

    val y = xs.sequenceU
    y
    //y: Reader[GameState, List[GameState]]
    //Reader(state => Vector(state))
  }

  def update(seqElem: SeqElem): Reader[GameState, GameState] =
    Reader { st =>
      seqElem match {
        case SeqMoveElement(MoveSymbol(SanMove(pt, draw))) => st.updated(Move(Piece(White, pt), util.SquareAbbr.e2 to draw.dest))
        case SeqMoveElement(MoveNumber(_, _)) => st // a move number should not trigger a new state
        case _ => st
      }
    }
}

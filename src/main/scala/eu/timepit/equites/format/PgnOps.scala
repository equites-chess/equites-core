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
import scala.annotation.tailrec
import scalaz.Scalaz._
import scalaz._

import scalaz.Reader

object PgnOps {
  def construct(seqElems: List[SeqElem]): Reader[GameState, List[GameState]] = {
    val elems = pairWithMoveNumbers(seqElems.toVector.collect { case SeqMoveElement(elem) => elem })
    val x2 = elems.collect { case (ms: MoveSymbol, n) => (ms, n) }

    val ri = Reader((state: GameState) => state)
    val xs = ri +: x2.map(update2)

    val y = xs.sequenceU
    y.map(_.toList)
    //y: Reader[GameState, List[GameState]]
    //Reader(state => Vector(state))
  }

  def update2(numeratedMoveSymbol: (MoveSymbol, Option[MoveNumber])): Reader[GameState, GameState] =
    Reader { st =>
      numeratedMoveSymbol match {
        case (MoveSymbol(SanMove(pt, draw)), _) => st.updated(Move(Piece(White, pt), util.SquareAbbr.e2 to draw.dest))
        case _                                  => st
      }

    }

  ///

  private type NumeratedMoveElement = (MoveElement, Option[MoveNumber])

  private def pairWithMoveNumbers(elems: Vector[MoveElement]): Vector[NumeratedMoveElement] = {
    @tailrec
    def go(last: Option[MoveNumber], xs: Vector[MoveElement], acc: Vector[NumeratedMoveElement]): Vector[NumeratedMoveElement] =
      xs match {
        case (number: MoveNumber) +: tail => go(Some(number), tail, acc)
        case elem +: tail                 => go(last, tail, (elem, last) +: acc)
        case Vector()                     => acc
      }
    go(None, elems, Vector.empty)
  }

}

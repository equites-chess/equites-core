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
  def reconstruct(seqElems: List[SeqElem]): Reader[GameState, List[GameState]] = {
    val elems = pairWithMoveNumbers(seqElems.toVector.collect { case SeqMoveElement(elem) => elem })
    val x2 = elems.collect { case (ms: MoveSymbol, n) => (ms, n) }

    val ri = Reader((state: GameState) => state)
    val xs: Vector[Reader[GameState, GameState]] = ri +: x2.map(update2)

    foo(xs.toList)

  }

  def update2(numeratedMoveSymbol: (MoveSymbol, Option[MoveNumber])): Reader[GameState, GameState] =
    Reader { st =>
      numeratedMoveSymbol match {
        case (MoveSymbol(sm @ SanMove(_, _)), _) =>
          update3(st, sm, st.color)

        case (MoveSymbol(sc @ SanCastling(side)), _) =>
          st.updated(Castling(st.color, side))

        case (MoveSymbol(sc @ SanCapture(_, _)), _) =>
          updateCapture(st, sc, st.color)

        case _ => st
      }

    }

  def update3(st: GameState, move: SanMove, color: Color): GameState = {
    val piece = Piece(color, move.pieceType)
    val cand = findCandidates(piece, move.draw.src, st.board)
    val possible = cand.map(pl => pl -> Movement.reachableVacantSquares(pl, st.board))
      .filter(_._2.contains(move.draw.dest))

    st.updated(Move(piece, possible.head._1.square to move.draw.dest))
  }

  def updateCapture(st: GameState, capt: SanCapture, color: Color): GameState = {
    val piece = Piece(color, capt.pieceType)
    val cand = findCandidates(piece, capt.draw.src, st.board)
    val possible = cand.map(pl => pl -> Movement.reachableOccupiedSquares(pl, st.board))
      .filter(_._2.map(_.square).contains(capt.draw.dest))

    st.updated(Capture(piece, possible.head._1.square to capt.draw.dest, st.board.get(capt.draw.dest).get))
  }

  ///

  private type NumeratedMoveElement = (MoveElement, Option[MoveNumber])

  private def pairWithMoveNumbers(elems: Vector[MoveElement]): Vector[NumeratedMoveElement] = {
    @tailrec
    def go(last: Option[MoveNumber], xs: Vector[MoveElement], acc: Vector[NumeratedMoveElement]): Vector[NumeratedMoveElement] =
      xs match {
        case (number: MoveNumber) +: tail => go(Some(number), tail, acc)
        case elem +: tail                 => go(last, tail, (elem, last) +: acc)
        case Vector()                     => acc.reverse
      }
    go(None, elems, Vector.empty)
  }

  ///

  def findCandidates(piece: AnyPiece, square: MaybeSquare, board: Board): List[Placed[AnyPiece]] =
    square.toSquare match {
      case Some(sq) => board.getPlaced(sq).toList
      case None     => board.placedPieces.filter(placed => placed.elem == piece && square.matches(placed.square)).toList
    }

  def foo[A](xs: List[Reader[A, A]]): Reader[A, List[A]] = {
    @tailrec
    def go(a: A, ys: List[Reader[A, A]], acc: List[A]): List[A] =
      ys match {
        case h :: t =>
          val a2 = h.run(a)
          go(a2, t, a2 :: acc)
        case Nil => acc.reverse
      }
    Reader(go(_, xs, Nil))
  }

}


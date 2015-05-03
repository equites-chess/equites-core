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
  def reconstruct(moveText: List[SeqElem]): Reader[GameState, List[GameState]] = {
    val elems = pairWithMoveNumbers(moveText.toVector.collect { case SeqMoveElement(elem) => elem })
    val x2 = elems.collect { case (ms: MoveSymbol, n) => (ms, n) }

    val ri = Reader((state: GameState) => state)
    val xs: Vector[Reader[GameState, GameState]] = ri +: x2.map(update2)

    foo(xs.toList)

  }

  def update2(numeratedMoveSymbol: (MoveSymbol, Option[MoveNumber])): Reader[GameState, GameState] =
    Reader { st =>
      numeratedMoveSymbol._1.action match {
        case sm @ SanMove(_, _) =>
          update3(st, sm, st.color)

        case sc @ SanCastling(side) =>
          st.updated(Castling(st.color, side))

        case sc @ SanCapture(_, _) =>
          updateCapture(st, sc, st.color)

        case CheckingSanAction(sa, _) =>
          update2((MoveSymbol(sa), None)).run(st)

        case p @ SanPromotion(_, _, _) =>
          updatePromotion(st, p)

        case cp @ SanCaptureAndPromotion(_, _, _) =>
          updateCaptureAndPromotion(st, cp)
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

    if (possible.isEmpty && st.lastAction.fold(false)(Rules.isTwoRanksPawnMoveFromStartingSquare) && piece.isPawn) {
      val last = st.lastAction.get
      val possible = Rules.enPassantSrcSquares(last)
        .filter(s => capt.draw.src.matches(s))
        .filter(s => st.board.isOccupiedBy(s, piece))
      val e = EnPassant(piece.maybePawn.get, possible.head to capt.draw.dest, last.piece.maybePawn.get, last.draw.dest)
      st.updated(e)
    } else {
      val c = Capture(piece, possible.head._1.square to capt.draw.dest, st.board.get(capt.draw.dest).get)
      st.updated(c)
    }
  }

  def updateCaptureAndPromotion(st: GameState, cp: SanCaptureAndPromotion): GameState = {
    // partially the same as updateCapture
    val piece = Piece(st.color, cp.pieceType)
    val cand = findCandidates(piece, cp.draw.src, st.board)
    val possible = cand.map(pl => pl -> Movement.reachableOccupiedSquares(pl, st.board))
      .filter(_._2.map(_.square).contains(cp.draw.dest))

    val c = CaptureAndPromotion(piece.maybePawn.get, possible.head._1.square to cp.draw.dest,
      st.board.get(cp.draw.dest).get, Piece(st.color, cp.promotedTo))
    st.updated(c)
  }

  def updatePromotion(st: GameState, p: SanPromotion): GameState = {
    val piece = Piece(st.color, p.pieceType)
    val cand = findCandidates(piece, p.draw.src, st.board)
    val possible = cand.map(pl => pl -> Movement.reachableVacantSquares(pl, st.board))
      .filter(_._2.contains(p.draw.dest))

    st.updated(Promotion(piece.maybePawn.get, possible.head._1.square to p.draw.dest, Piece(st.color, p.promotedTo)))
  }

  ///

  private type NumeratedMoveElement = (MoveElement, Option[MoveNumber])

  private def pairWithMoveNumbers(elems: Vector[MoveElement]): Vector[NumeratedMoveElement] = {
    @tailrec
    def go(last: Option[MoveNumber], xs: Vector[MoveElement], acc: Vector[NumeratedMoveElement]): Vector[NumeratedMoveElement] =
      xs match {
        case (number: MoveNumber) +: tail => go(Some(number), tail, acc)
        case elem +: tail                 => go(last, tail, acc :+ ((elem, last)))
        case Vector()                     => acc
      }
    go(None, elems, Vector.empty)
  }

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

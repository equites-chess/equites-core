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
    val x2 = moveText.collect { case SeqMoveElement(ms @ MoveSymbol(_)) => ms }.toVector

    val ri = Reader((state: GameState) => state)
    val xs: Vector[Reader[GameState, GameState]] =
      ri +: x2.map(ms => Reader((st: GameState) => updateAction(ms.action, st)))

    foo(xs.toList)
  }

  def updateMove(move: SanMove, st: GameState): GameState =
    reconstructMoves(move, st).headOption.fold(st)(st.update)

  def updateCapture(capt: SanCapture, st: GameState): GameState = {
    val piece = Piece(st.color, capt.pieceType)
    val cand = findMatchingPlacedPieces(piece, capt.draw.src, st.board)
    val possible = cand.map(pl => pl -> Movement.reachableOccupiedSquares(pl, st.board))
      .filter(_._2.map(_.square).contains(capt.draw.dest))

    if (possible.isEmpty && st.lastAction.fold(false)(Rules.isTwoRanksPawnMoveFromStartingSquare) && piece.isPawn) {
      val last = st.lastAction.get
      val possible = Rules.enPassantSrcSquares(last)
        .filter(s => capt.draw.src.matches(s))
        .filter(s => st.board.isOccupiedBy(s, piece))
      val e = EnPassant(piece.maybePawn.get, possible.head to capt.draw.dest, last.piece.maybePawn.get, last.draw.dest)
      st.update(e)
    } else {
      val c = Capture(piece, possible.head._1.square to capt.draw.dest, st.board.get(capt.draw.dest).get)
      st.update(c)
    }
  }

  def updateCaptureAndPromotion(cp: SanCaptureAndPromotion, st: GameState): GameState = {
    // partially the same as updateCapture
    val piece = Piece(st.color, cp.pieceType)
    val cand = findMatchingPlacedPieces(piece, cp.draw.src, st.board)
    val possible = cand.map(pl => pl -> Movement.reachableOccupiedSquares(pl, st.board))
      .filter(_._2.map(_.square).contains(cp.draw.dest))

    val c = CaptureAndPromotion(piece.maybePawn.get, possible.head._1.square to cp.draw.dest,
      st.board.get(cp.draw.dest).get, Piece(st.color, cp.promotedTo))
    st.update(c)
  }

  def updatePromotion(p: SanPromotion, st: GameState): GameState =
    reconstructPromotions(p, st).headOption.fold(st)(st.update)

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

  //////

  @tailrec
  def updateAction(action: SanAction, state: GameState): GameState =
    action match {
      case a: SanMove                => updateMove(a, state)
      case a: SanCapture             => updateCapture(a, state)
      case a: SanPromotion           => updatePromotion(a, state)
      case a: SanCaptureAndPromotion => updateCaptureAndPromotion(a, state)
      case SanCastling(side)         => state.update(Castling(state.color, side))
      case CheckingSanAction(a, _)   => updateAction(a, state)
    }

  def reconstructMoves(sanMove: SanMove, state: GameState): Stream[Move] = {
    val piece = Piece(state.color, sanMove.pieceType)
    val candidates = findMatchingPlacedPieces(piece, sanMove.draw.src, state.board)
    val dest = sanMove.draw.dest

    candidates
      .filter(Movement.reachableVacantSquares(_, state.board).contains(dest))
      .map(placed => Move(placed.elem, placed.square to dest))
  }

  def reconstructPromotions(sanPromotion: SanPromotion, state: GameState): Stream[Promotion] =
    reconstructMoves(sanPromotion.toSanMove, state).map { mv =>
      val piece = mv.piece.copy(pieceType = Pawn)
      val promotedTo = piece.copy(pieceType = sanPromotion.promotedTo)
      Promotion(piece, mv.draw, promotedTo)
    }

  def findMatchingPlacedPieces(piece: AnyPiece, at: MaybeSquare, board: Board): Stream[Placed[AnyPiece]] = {
    def matches(placed: Placed[AnyPiece]): Boolean =
      piece == placed.elem && at.matches(placed.square)

    at.toSquare match {
      case Some(sq) => board.getPlaced(sq).toStream
      case None     => board.placedPieces.filter(matches)
    }
  }
}

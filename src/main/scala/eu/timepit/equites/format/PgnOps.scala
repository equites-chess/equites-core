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

  def updateCapture(capt: SanCapture, st: GameState): GameState =
    reconstructCapturesOrEnPassants(capt, st).headOption.fold(st)(st.update)

  def updateCaptureAndPromotion(cp: SanCaptureAndPromotion, st: GameState): GameState =
    reconstructCaptureAndPromotions(cp, st).headOption.fold(st)(st.update)

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

  def reconstructMoves(san: SanMove, state: GameState): Stream[Move] = {
    val piece = Piece(state.color, san.pieceType)
    val candidates = findMatchingPlacedPieces(piece, san.draw.src, state.board)
    val dest = san.draw.dest

    candidates
      .filter(Movement.reachableVacantSquares(_, state.board).contains(dest))
      .map(placed => Move(placed.elem, placed.square to dest))
  }

  def reconstructCaptures(san: SanCapture, state: GameState): Stream[Capture] = {
    val piece = Piece(state.color, san.pieceType)
    val candidates = findMatchingPlacedPieces(piece, san.draw.src, state.board)
    val dest = san.draw.dest

    candidates
      .map(p => p -> Movement.reachableOpponentSquares(p, state.board))
      .filter { case (_, os) => os.exists(_.square == dest) }
      .flatMap { case (p, os) => os.map(o => Capture(piece, p.square to dest, o.elem)) }
  }

  def reconstructEnPassants(san: SanCapture, state: GameState): Stream[EnPassant] =
    state.lastAction.toStream.flatMap { last =>
      if (!Rules.isTwoRanksPawnMoveFromStartingSquare(last))
        Stream.empty
      else {
        val pawn = Piece(state.color, Pawn)
        val captured = Piece(state.color.opposite, Pawn)

        Rules.enPassantSrcSquares(last)
          .filter(sq => san.draw.src.matches(sq) && state.board.isOccupiedBy(sq, pawn))
          .map(sq => EnPassant(pawn, sq to san.draw.dest, captured, last.draw.dest))
      }
    }

  def reconstructCapturesOrEnPassants(san: SanCapture, state: GameState): Stream[Action] = {
    val captures = reconstructCaptures(san, state)
    if (captures.nonEmpty) captures else reconstructEnPassants(san, state)
  }

  def reconstructPromotions(san: SanPromotion, state: GameState): Stream[Promotion] =
    reconstructMoves(san.toSanMove, state).map { mv =>
      val pawn = mv.piece.copy(pieceType = Pawn)
      val promotedTo = pawn.copy(pieceType = san.promotedTo)
      Promotion(pawn, mv.draw, promotedTo)
    }

  def reconstructCaptureAndPromotions(san: SanCaptureAndPromotion, state: GameState): Stream[CaptureAndPromotion] =
    reconstructCaptures(san.toSanCapture, state).map { cp =>
      val pawn = cp.piece.copy(pieceType = Pawn)
      val promotedTo = pawn.copy(pieceType = san.promotedTo)
      CaptureAndPromotion(pawn, cp.draw, cp.captured, promotedTo)
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

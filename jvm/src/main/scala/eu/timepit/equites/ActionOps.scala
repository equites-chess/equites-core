// Equites, a Scala chess playground
// Copyright © 2013-2015 Frank S. Thomas <frank@timepit.eu>
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

import eu.timepit.equites.util.CoordinateAction

import scalaz.Scalaz._

object ActionOps {
  def getPromotedPiece(action: Action): Option[PromotedPiece] =
    action match {
      case p: PromotionLike => Some(p.promotedTo)
      case _                => None
    }

  def isCapture(action: Action): Boolean =
    action match {
      case _: CaptureLike => true
      case _              => false
    }

  def isPawnMove(action: Action): Boolean =
    action.piece.isPawn

  def mkCaptureAndPromotion(promotion: PromotionLike, captured: AnyPiece): CaptureAndPromotion =
    CaptureAndPromotion(promotion.piece, promotion.draw, captured, promotion.promotedTo)

  ///

  def reifyMove(draw: Draw, board: Board): Option[Move] =
    for {
      src <- draw.nonNull.option(draw.src)
      piece <- board.get(src)
    } yield Move(piece, draw)

  def reifyCapture(move: MoveLike, board: Board): Option[Capture] =
    for {
      captured <- board.get(move.draw.dest)
      if captured.isOpponentOf(move.piece)
    } yield Capture(move.piece, move.draw, captured)

  def reifyEnPassant(move: MoveLike, board: Board): Option[EnPassant] =
    for {
      pawn <- move.piece.maybePawn
      if move.draw.direction.isDiagonal
      target <- move.draw.src + move.draw.direction.fileProj
      other <- board.get(target)
      otherPawn <- other.maybePawn
      if otherPawn.isOpponentOf(pawn)
    } yield EnPassant(pawn, move.draw, otherPawn, target)

  def reifyCastling(move: MoveLike, board: Board): Option[Castling] =
    for {
      castling <- Castling.all.find(_.kingMove == move)
      _ <- reifyMove(castling.kingMove.draw, board)
      _ <- reifyMove(castling.rookMove.draw, board)
    } yield castling

  def reifyPromotion(ca: CoordinateAction, move: MoveLike): Option[Promotion] =
    for {
      pawn <- move.piece.maybePawn
      promotedTo <- ca.promotedTo
    } yield Promotion(pawn, move.draw, promotedTo)

  def reifyAction(ca: CoordinateAction, board: Board): Option[Action] = {
    val move = reifyMove(ca.draw, board)
    move.flatMap { mv =>
      lazy val capture = reifyCapture(mv, board)
      lazy val promotion = reifyPromotion(ca, mv)
      lazy val captureAndPromotion =
        ^(promotion, capture.map(_.captured))(mkCaptureAndPromotion)
      lazy val enPassant = reifyEnPassant(mv, board)
      lazy val castling = reifyCastling(mv, board)

      (castling
        orElse enPassant
        orElse captureAndPromotion
        orElse promotion
        orElse capture
        orElse move)
    }
  }
}

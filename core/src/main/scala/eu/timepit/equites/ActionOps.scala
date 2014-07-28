// Equites, a Scala chess playground
// Copyright © 2013-2014 Frank S. Thomas <frank@timepit.eu>
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

import scalaz.syntax.std.boolean._

object ActionOps {
  def promotedPiece(action: Action): Option[PromotedPiece] =
    action match {
      case p: PromotionLike => Some(p.promotedTo)
      case _                => None
    }

  /// OLD

  def reifyAsMove(board: Board, draw: Draw): Option[Move] =
    for {
      src <- draw.nonNull.option(draw.src)
      piece <- board.get(src)
    } yield Move(piece, draw)

  def reifyAsCapture(board: Board, move: MoveLike): Option[Capture] =
    for {
      captured <- board.get(move.draw.dest)
      if captured.isOpponentOf(move.piece)
    } yield Capture(move.piece, move.draw, captured)

  def reifyAsEnPassant(board: Board, move: MoveLike): Option[EnPassant] =
    for {
      pawn <- move.piece.maybePawn
      if move.draw.direction.isDiagonal
      target <- move.draw.src + move.draw.direction.fileProj
      other <- board.get(target)
      otherPawn <- other.maybePawn
      if otherPawn.isOpponentOf(pawn)
    } yield EnPassant(pawn, move.draw, otherPawn, target)

  def reifyAsCastling(board: Board, move: MoveLike): Option[Castling] =
    for {
      castling <- Castling.all.find(_.kingMove == move)
      _ <- reifyAsMove(board, castling.kingMove.draw)
      _ <- reifyAsMove(board, castling.rookMove.draw)
    } yield castling

  def reifyAsAction(board: Board, cm: util.CoordinateAction): Option[Action] = {
    val moveOpt = reifyAsMove(board, cm.draw)
    moveOpt.flatMap { move =>
      lazy val captureOpt = reifyAsCapture(board, move)

      lazy val promotionOpt =
        for {
          pawn <- move.piece.maybePawn
          promotedTo <- cm.promotedTo
        } yield Promotion(pawn, move.draw, promotedTo)

      lazy val captureAndPromotionOpt =
        for {
          capture <- captureOpt
          promotion <- promotionOpt
        } yield CaptureAndPromotion(promotion.piece, promotion.draw, capture.captured, promotion.promotedTo)

      reifyAsCastling(board, move)
        .orElse(reifyAsEnPassant(board, move))
        .orElse(captureAndPromotionOpt)
        .orElse(captureOpt)
        .orElse(promotionOpt)
        .orElse(moveOpt)
    }
  }

}

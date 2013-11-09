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

import scalaz.syntax.std.boolean._

object ActionOps {
  def drawAsMove(board: Board)(draw: DrawLike): Option[Move] =
    board.get(draw.from).map(piece => Move(piece, draw.from, draw.to))

  def moveAsCapture(board: Board)(move: MoveLike): Option[Capture] =
    for {
      captured <- board.get(move.to)
      if captured.isOpponentOf(move.piece)
    } yield Capture(move, captured)

  def moveAsEnPassant(board: Board)(move: MoveLike): Option[EnPassant] =
    for {
      pawn <- move.piece.maybePawn
      if move.direction.isDiagonal
      target = move.from + move.direction.fileProj
      other <- board.get(target)
      otherPawn <- other.maybePawn
      if otherPawn.isOpponentOf(pawn)
    } yield EnPassant(pawn, move.from, move.to, otherPawn, target)

  def moveAsCastling(board: Board)(move: MoveLike): Option[Castling] =
    for {
      castling <- Rules.allCastlings.find(_.kingMove == move)
      if drawAsMove(board)(castling.kingMove).isDefined
      if drawAsMove(board)(castling.rookMove).isDefined
    } yield castling

  def cmAsAction(board: Board)(cm: util.CoordinateMove): Option[Action] = {
    val optMove = drawAsMove(board)(cm)
    optMove.flatMap { move =>
      lazy val optCapture = moveAsCapture(board)(move)

      lazy val optPromotion =
        for {
          pawn <- move.piece.maybePawn
          promotedTo <- cm.promotedTo
        } yield Promotion(pawn, move.from, move.to, promotedTo)

      lazy val optCaptureAndPromotion =
        for {
          capture <- optCapture
          promotion <- optPromotion
        } yield CaptureAndPromotion(promotion, capture.captured)

      moveAsCastling(board)(move)
        .orElse(moveAsEnPassant(board)(move))
        .orElse(optCaptureAndPromotion)
        .orElse(optCapture)
        .orElse(optPromotion)
        .orElse(optMove)
    }
  }

  /** Returns true if the given action is a capture or a pawn move. */
  def isCaptureOrPawnMove(action: Action): Boolean =
    action match {
      case _: CaptureLike => true
      case move: MoveLike => move.piece.isPawn
      case _ => false
    }

  /** Returns the square where a pawn can be captured via an en passant if the
    * given move allows it.
    */
  def enPassantTarget(move: Move): Option[Square] =
    allowsEnPassant(move).option {
      val file = move.from.file
      val rank = Rules.enPassantTargetRankBy(move.piece.color)
      Square(file, rank)
    }

  /** Returns true if the given move allows an en passant capture. */
  def allowsEnPassant(move: Move): Boolean =
    move.piece.isPawn && move.l1Length == 2 && move.direction.isStraight
}

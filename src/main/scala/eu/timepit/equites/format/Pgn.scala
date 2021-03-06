// Equites, a Scala chess playground
// Copyright © 2014 Frank S. Thomas <frank@timepit.eu>
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

object Pgn {
  case class Comment(text: String)

  case class Tag(name: String, value: String)

  ///

  case class MaybeSquare(file: Option[File] = None, rank: Option[Rank] = None) {
    def matches(square: Square): Boolean = {
      val fileMatches = file.fold(true)(_ == square.file)
      val rankMatches = rank.fold(true)(_ == square.rank)
      fileMatches && rankMatches
    }

    def toSquare: Option[Square] =
      file.flatMap(f => rank.flatMap(r => Square.from(f, r)))
  }

  object MaybeSquare {
    def apply(square: Square): MaybeSquare =
      MaybeSquare(Some(square.file), Some(square.rank))
  }

  case class MaybeDraw(src: MaybeSquare, dest: Square)

  object MaybeDraw {
    def apply(dest: Square): MaybeDraw =
      MaybeDraw(MaybeSquare(), dest)

    def apply(draw: Draw): MaybeDraw =
      MaybeDraw(MaybeSquare(draw.src), draw.dest)
  }

  ///

  sealed trait CheckIndicator
  case object Check extends CheckIndicator
  case object CheckMate extends CheckIndicator

  ///

  sealed trait SanAction

  sealed trait SanMoveLike extends SanAction {
    def pieceType: PieceType
    def draw: MaybeDraw
  }

  sealed trait SanPromotionLike extends SanMoveLike {
    def promotedTo: PromotedPieceType
  }

  case class SanMove(
    pieceType: PieceType,
    draw: MaybeDraw)
      extends SanMoveLike

  case class SanCapture(
    pieceType: PieceType,
    draw: MaybeDraw)
      extends SanMoveLike

  case class SanPromotion(
    pieceType: Pawn.type,
    draw: MaybeDraw,
    promotedTo: PromotedPieceType)
      extends SanPromotionLike {

    def toSanMove: SanMove =
      SanMove(pieceType, draw)
  }

  case class SanCaptureAndPromotion(
    pieceType: Pawn.type,
    draw: MaybeDraw,
    promotedTo: PromotedPieceType)
      extends SanPromotionLike {

    def toSanCapture: SanCapture =
      SanCapture(pieceType, draw)
  }

  case class SanCastling(side: Side) extends SanAction

  case class CheckingSanAction(
    action: SanAction,
    indicator: CheckIndicator)
      extends SanAction

  ///

  sealed trait MoveElement

  case class MoveNumber(moveNumber: Int, color: Color) extends MoveElement

  case class MoveSymbol(action: SanAction) extends MoveElement

  case class AnnotationGlyph(glyph: Int) extends MoveElement

  ///

  sealed trait SeqElem

  case class SeqMoveElement(move: MoveElement) extends SeqElem

  case class SeqComment(comment: Comment) extends SeqElem

  case class RecursiveVariation(variation: List[SeqElem]) extends SeqElem

  ///

  case class MoveTextSection(moveText: List[SeqElem], result: GameResult)

  case class GameRecord(header: List[Tag], moveText: MoveTextSection)
}

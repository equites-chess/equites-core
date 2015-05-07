// Equites, a Scala chess playground
// Copyright © 2011, 2013-2014 Frank S. Thomas <frank@timepit.eu>
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

import eu.timepit.equites.implicits.PlacedImplicits._
import eu.timepit.equites.util.PieceAbbr.Textual._
import eu.timepit.equites.util.SquareAbbr._

import scalaz.syntax.std.boolean._

object Rules {
  val maxBoardLength: Int = math.max(File.max.value, Rank.max.value)

  val whiteBackRank: Rank = _1
  val whitePawnRank: Rank = _2

  val blackBackRank: Rank = backRankBy(Black)
  val blackPawnRank: Rank = pawnRankBy(Black)

  def rankBy(rank: Rank, color: Color): Rank =
    color.fold(rank, Rank.max - rank)

  def backRankBy(color: Color): Rank =
    rankBy(whiteBackRank, color)

  def pawnRankBy(color: Color): Rank =
    rankBy(whitePawnRank, color)

  /** Returns the rank a pawn moves from with an en passant capture. */
  def enPassantSrcRankBy(color: Color): Rank =
    rankBy(whitePawnRank + 2, color.opposite)

  /** Returns the rank a pawn moves to with an en passant capture. */
  def enPassantDestRankBy(color: Color): Rank =
    rankBy(whitePawnRank + 1, color.opposite)

  /** Returns the rank "behind" a pawn that made a two-square move. */
  def enPassantTargetRankBy(color: Color): Rank =
    enPassantDestRankBy(color.opposite)

  ///

  val kingFile: File = e_
  val queenFile: File = d_
  val rookFiles: List[File] = List(a_, h_)
  val knightFiles: List[File] = List(b_, g_)
  val bishopFiles: List[File] = List(c_, f_)

  val startingSquares: Map[AnyPiece, List[Square]] = {
    def startingSquaresBy(color: Color): Map[AnyPiece, List[Square]] = {
      val backRank = backRankBy(color)
      val pawnRank = pawnRankBy(color)
      // format: OFF
      Map(
        king(color)   -> Square.from(kingFile, backRank).toList,
        queen(color)  -> Square.from(queenFile, backRank).toList,
        rook(color)   -> rookFiles.map(Square.from(_, backRank)).flatten,
        bishop(color) -> bishopFiles.map(Square.from(_, backRank)).flatten,
        knight(color) -> knightFiles.map(Square.from(_, backRank)).flatten,
        pawn(color)   -> Square.allWithRank(pawnRank).toList)
      // format: ON
    }
    Color.all.map(startingSquaresBy).reduce(_ ++ _)
  }

  val startingBoard: Board = {
    val mapping = for {
      (piece, squares) <- startingSquares
      square <- squares
    } yield square -> piece
    Board(mapping)
  }

  def onStartingSquare(placed: PlacedPiece): Boolean =
    startingSquares(placed).contains(placed.square)

  ///

  val castlingDraws: Map[(Side, CastlingPiece), Draw] = {
    def castlingDrawFor(side: Side, piece: CastlingPiece): Option[Draw] = {
      val rookFile = rookFiles(side.fold(1, 0))
      val (srcFile, offset) = piece.pieceType match {
        case King => (kingFile, 2)
        case Rook => (rookFile, 1)
      }

      val leftOrRight = side.fold(1, -1)
      val destFile = kingFile + offset * leftOrRight
      val rank = backRankBy(piece.color)

      for {
        src <- Square.from(srcFile, rank)
        dest <- Square.from(destFile, rank)
      } yield src to dest
    }

    val mapping = for {
      side <- Side.all
      piece <- Piece.allCastling
      draw <- castlingDrawFor(side, piece)
    } yield (side, piece) -> draw
    mapping.toMap
  }

  def associatedCastlings(xs: PlacedPiece*): Seq[Castling] =
    xs.flatMap { placed =>
      placed.elem match {
        case Piece(color, King) =>
          Castling.allBy(color)
        case Piece(color, Rook) =>
          Castling.allBy(color).filter(_.rookMove.draw.src == placed.square)
        case _ => Nil
      }
    }

  ///

  def isTwoRanksPawnMoveFromStartingSquare(action: Action): Boolean =
    ActionOps.isPawnMove(action) &&
      action.draw.l1Length == 2 &&
      action.draw.direction.isVertical &&
      onStartingSquare(action.placedPiece)

  /**
   * Returns the square where a pawn can be captured via an en passant if
   * `action` allows it.
   */
  def enPassantTargetSquare(action: Action): Option[Square] =
    isTwoRanksPawnMoveFromStartingSquare(action).option {
      val file = action.draw.src.file
      val rank = enPassantTargetRankBy(action.piece.color)
      Square.from(file, rank)
    }.flatten

  def enPassantSrcSquares(action: Action): Stream[Square] = {
    // ugly!
    val rank = enPassantSrcRankBy(action.piece.color.opposite)
    val file = Directions.horizontal.toStream.map(dir => action.draw.dest + dir)
    val x = file.flatten.map(s => Square.from(s.file, rank))
    x.flatten
  }
}

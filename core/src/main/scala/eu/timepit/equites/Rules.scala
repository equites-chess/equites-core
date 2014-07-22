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

import scalaz.std.stream
import scalaz.syntax.std.boolean._

import implicits.PlacedImplicits._
import util.PieceAbbr.Textual._

object Rules {
  val fileSeq: Seq[File] = (0 to 7).map(File)
  val rankSeq: Seq[Rank] = (0 to 7).map(Rank)
  val boardLength: Int = math.max(fileSeq.last.value, rankSeq.last.value)

  def fileSquares(file: File): Seq[Square] =
    rankSeq.map(Square.from(file, _)).flatten

  def rankSquares(rank: Rank): Seq[Square] =
    fileSeq.map(Square.from(_, rank)).flatten

  val allSquaresSeq: Seq[Square] = rankSeq.flatMap(rankSquares)
  val allSquaresSet: Set[Square] = allSquaresSeq.toSet

  val whiteBackRank: Rank = rankSeq.head
  val whitePawnRank: Rank = whiteBackRank + Rank(1)

  val blackBackRank: Rank = backRankBy(Black)
  val blackPawnRank: Rank = pawnRankBy(Black)

  def rankBy(rank: Rank, color: Color): Rank =
    color.fold(rank, rankSeq.last - rank)

  def backRankBy(color: Color): Rank =
    rankBy(whiteBackRank, color)

  def pawnRankBy(color: Color): Rank =
    rankBy(whitePawnRank, color)

  /** Returns the rank a pawn moves from with an en passant capture. */
  def enPassantSrcRankBy(color: Color): Rank =
    rankBy(whitePawnRank + Rank(2), color.opposite)

  /** Returns the rank a pawn moves to with an en passant capture. */
  def enPassantDestRankBy(color: Color): Rank =
    rankBy(whitePawnRank + Rank(1), color.opposite)

  /** Returns the rank "behind" a pawn that made a two-square move. */
  def enPassantTargetRankBy(color: Color): Rank =
    enPassantDestRankBy(color.opposite)

  ///

  val kingFile: File = File(4)
  val queenFile: File = File(3)
  val rookFiles: List[File] = List(0, 7).map(File)
  val knightFiles: List[File] = List(1, 6).map(File)
  val bishopFiles: List[File] = List(2, 5).map(File)

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
        pawn(color)   -> fileSeq.map(Square.from(_, pawnRank)).flatten.toList)
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
      val destFile = kingFile + File(offset * leftOrRight)
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

  case class Movement(directions: Directions, distance: Int)

  val pieceMovements: Map[AnyPiece, Movement] = {
    def pieceMovementsBy(color: Color): Map[AnyPiece, Movement] = {
      import Directions._
      // format: OFF
      Map(
        king(color)   -> Movement(anywhere, 1),
        queen(color)  -> Movement(anywhere, boardLength),
        rook(color)   -> Movement(straight, boardLength),
        bishop(color) -> Movement(diagonal, boardLength),
        knight(color) -> Movement(knightLike, 1),
        pawn(color)   -> Movement(front.fromViewOf(color), 1))
      // format: ON
    }
    Color.all.map(pieceMovementsBy).reduce(_ ++ _)
  }

  def movementOf(placed: PlacedPiece): Movement = {
    val movement = pieceMovements(placed)
    placed.pieceType match {
      case Pawn if onStartingSquare(placed) => movement.copy(distance = 2)
      case _                                => movement
    }
  }

  def squaresInDirection(from: Square, direction: Vec): Stream[Square] =
    stream.unfold(from)(sq => (sq + direction).map(util.toTuple2))

  def directedReachableSquares(placed: PlacedPiece): Stream[Stream[Square]] = {
    val movement = movementOf(placed)
    movement.directions.toStream.map { dir =>
      squaresInDirection(placed.square, dir).take(movement.distance)
    }
  }

  def undirectedReachableSquares(placed: PlacedPiece): Stream[Square] =
    directedReachableSquares(placed).flatten

  def reachableVacantSquares(placed: PlacedPiece, board: Board): Stream[Square] =
    directedReachableSquares(placed).flatMap {
      _.takeWhile(board.isVacant)
    }

  def reachableOccupiedSquares(placed: PlacedPiece, board: Board): Stream[PlacedPiece] =
    directedReachableSquares(placed).flatMap {
      _.flatMap(sq => board.getPlaced(sq).toList).take(1)
    }

  def unvisitedSquares(placed: PlacedPiece, visited: Set[Square]): Stream[Square] =
    undirectedReachableSquares(placed).filterNot(visited)

  ///

  def isCapture(action: Action): Boolean =
    action.isInstanceOf[CaptureLike]

  def isPawnMove(action: Action): Boolean =
    action.piece.isPawn

  def isTwoRanksPawnMoveFromStartingSquare(action: Action): Boolean =
    isPawnMove(action) &&
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
}

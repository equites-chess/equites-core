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

import implicits.PlacedImplicits._

object Rules {
  val fileRange = 0 to 7
  val rankRange = 0 to 7
  val maxLength = math.max(fileRange.length, rankRange.length) - 1

  def fileSquares(file: Int): Seq[Square] = rankRange.map(Square.from(file, _)).flatten
  def rankSquares(rank: Int): Seq[Square] = fileRange.map(Square.from(_, rank)).flatten

  val allSquaresSeq: Seq[Square] = rankRange.flatMap(rankSquares)
  val allSquaresSet: Set[Square] = allSquaresSeq.toSet

  val kingFile = 4
  val queenFile = 3
  val rookFiles = List(0, 7)
  val knightFiles = List(1, 6)
  val bishopFiles = List(2, 5)

  val startingSquares: Map[AnyPiece, List[Square]] = {
    def startingSquaresBy(color: Color): Map[AnyPiece, List[Square]] = {
      val backRank = backRankBy(color)
      val pawnRank = pawnRankBy(color)
      Map(Piece(color, King) -> Square.from(kingFile, backRank).toList,
        Piece(color, Queen) -> Square.from(queenFile, backRank).toList,
        Piece(color, Rook) -> rookFiles.map(Square.from(_, backRank)).flatten,
        Piece(color, Bishop) -> bishopFiles.map(Square.from(_, backRank)).flatten,
        Piece(color, Knight) -> knightFiles.map(Square.from(_, backRank)).flatten,
        Piece(color, Pawn) -> fileRange.map(Square.from(_, pawnRank)).flatten.toList)
    }
    Color.all.map(startingSquaresBy).reduce(_ ++ _)
  }

  def onStartingSquare(placed: Placed[AnyPiece]): Boolean =
    startingSquares(placed) contains placed.square

  def onEnPassantRank(placed: Placed[AnyPawn]): Boolean =
    enPassantRankBy(placed.color) == placed.square.rank

  val castlingSquares: Map[(Side, CastlingPiece), (Square, Square)] = {
    def castlingSquaresFor(side: Side, piece: CastlingPiece): (Option[Square], Option[Square]) = {
      val rookFile = if (side == Kingside) rookFiles(1) else rookFiles(0)
      val (fromFile, pieceOffset) = piece.pieceType match {
        case King => (kingFile, 2)
        case Rook => (rookFile, 1)
      }

      val leftOrRight = if (side == Kingside) 1 else -1
      val toFile = kingFile + pieceOffset * leftOrRight
      val rank = backRankBy(piece.color)

      (Square.from(fromFile, rank), Square.from(toFile, rank))
    }

    val mapping = for {
      side <- Side.all
      piece <- Piece.allCastling
      x = castlingSquaresFor(side, piece)
      xx <- x._1
      yy <- x._2
    } yield (side, piece) -> ((xx, yy))
    mapping.toMap
  }

  def castlingsBy(color: Color): List[Castling] =
    List(CastlingShort(color), CastlingLong(color))

  val allCastlings: List[Castling] =
    castlingsBy(White) ::: castlingsBy(Black)

  def associatedCastlings(xs: Placed[AnyPiece]*): Seq[Castling] =
    xs.flatMap { placed =>
      placed.elem match {
        case Piece(color, King) =>
          castlingsBy(color)
        case Piece(color, Rook) =>
          castlingsBy(color).filter(_.rookMove.from == placed.square)
        case _ => Nil
      }
    }

  val startingBoard: Board = {
    val mapping = for {
      (piece, squares) <- startingSquares
      square <- squares
    } yield square -> piece
    Board(mapping)
  }

  def backRankBy(color: Color): Int =
    rankBy(rankRange.start, color)

  def pawnRankBy(color: Color): Int =
    rankBy(rankRange.start + 1, color)

  def enPassantRankBy(color: Color): Int =
    rankBy(rankRange.start + 3, color.opposite)

  def enPassantTargetRankBy(color: Color): Int =
    rankBy(rankRange.start + 2, color)

  def rankBy(rank: Int, color: Color): Int =
    if (color == White) rank else rankRange.end - rank

  val movementTypes: Map[AnyPiece, (Directions, Int)] = {
    def movementTypesBy(color: Color): Map[AnyPiece, (Directions, Int)] = {
      import Directions._
      Map(Piece(color, King) -> ((anywhere, 1)),
        Piece(color, Queen) -> ((anywhere, maxLength)),
        Piece(color, Rook) -> ((straight, maxLength)),
        Piece(color, Bishop) -> ((diagonal, maxLength)),
        Piece(color, Knight) -> ((knightLike, 1)),
        Piece(color, Pawn) -> ((front.fromPov(color), 1)))
    }
    Color.all.map(movementTypesBy).reduce(_ ++ _)
  }

  def movementTypeOf(placed: Placed[AnyPiece]): (Directions, Int) = {
    val (directions, dist) = movementTypes(placed.elem)
    placed.pieceType match {
      case Pawn if onStartingSquare(placed) => (directions, 2)
      case _                                => (directions, dist)
    }
  }

  def squaresInDirection(from: Square, direction: Vec): Stream[Square] =
    scalaz.std.stream.unfold(from)(sq => (sq + direction).map(x => (x, x)))
  //Stream.iterate(from)(_ + direction).tail.takeWhile(_.isValid)

  def possibleSquares(placed: Placed[AnyPiece]): Stream[Square] = {
    val (directions, dist) = movementTypeOf(placed)
    for {
      direction <- directions.toStream
      square <- squaresInDirection(placed.square, direction).take(dist)
    } yield square
  }

  def unvisitedSquares(placed: Placed[AnyPiece], visited: Set[Square]): Stream[Square] =
    possibleSquares(placed).filterNot(visited)
}

// Equites, a simple chess interface
// Copyright © 2011, 2013 Frank S. Thomas <f.thomas@gmx.de>
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

object Rules {
  val fileRange = 0 to 7
  val rankRange = 0 to 7
  val maxLength = math.max(fileRange.length, rankRange.length) - 1

  def fileSquares(file: Int) = rankRange.map(Square(file, _))
  def rankSquares(rank: Int) = fileRange.map(Square(_, rank))

  val kingFile  = 4
  val queenFile = 3
  val rookFiles   = List(0, 7)
  val knightFiles = List(1, 6)
  val bishopFiles = List(2, 5)

  val startingSquares: Map[Piece, List[Square]] = {
    (for {
      color <- Color.values
      backRank = backRankBy(color)
      pawnRank = pawnRankBy(color)
    } yield {
      Map(King(color)   -> List(Square(kingFile,  backRank)),
          Queen(color)  -> List(Square(queenFile, backRank)),
          Rook(color)   ->   rookFiles.map(Square(_, backRank)),
          Bishop(color) -> bishopFiles.map(Square(_, backRank)),
          Knight(color) -> knightFiles.map(Square(_, backRank)),
          Pawn(color)   ->   fileRange.map(Square(_, pawnRank)).toList)
    }).flatten.toMap
  }

  val castlingSquares: Map[(Side, Piece), (Square, Square)] = {
    def castlingSquaresFor(side: Side, piece: Piece): (Square, Square) = {
      val rookFile = if (side == Kingside) rookFiles(1) else rookFiles(0)
      val (fromFile, pieceOffset) = piece match {
        case King(_) => (kingFile, 2)
        case Rook(_) => (rookFile, 1)
        case _ => throw new IllegalArgumentException
      }

      val leftOrRight = if (side == Kingside) 1 else -1
      val toFile = kingFile + pieceOffset * leftOrRight
      val rank = backRankBy(piece.color)

      (Square(fromFile, rank), Square(toFile, rank))
    }

    val mapping = for {
      side  <- Side.values
      color <- Color.values
      piece <- List(King(color), Rook(color))
    } yield (side, piece) -> castlingSquaresFor(side, piece)
    mapping.toMap
  }

  val startingBoard: Board = {
    val mapping = for {
      (piece, squares) <- startingSquares
      square <- squares
    } yield square -> piece
    Board(mapping)
  }

  def backRankBy(color: Color): Int =
    if (color == White) rankRange.start else rankRange.end

  def pawnRankBy(color: Color): Int =
    if (color == White) rankRange.start + 1 else rankRange.end - 1

  def rankBy(rank: Int, color: Color): Int =
    if (color == White) rank else rankRange.end - rank

  val movementType: Map[Piece, (Directions, Int)] = {
    import Directions._
    (for {
      color <- Color.values
    } yield {
      Map(King(color)   -> (anywhere, 1),
          Queen(color)  -> (anywhere, maxLength),
          Rook(color)   -> (straight, maxLength),
          Bishop(color) -> (diagonal, maxLength),
          Knight(color) -> (knightLike, 1),
          Pawn(color)   -> (front.fromPOV(color), 1))
    }).flatten.toMap
  }

  def squaresInDirection(from: Square, direction: Vec): Stream[Square] = {
    val next = from + direction
    if (next.isValid) next #:: squaresInDirection(next, direction)
    else Stream.Empty
  }
}

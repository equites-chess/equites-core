// Equites, a simple chess interface
// Copyright © 2011 Frank S. Thomas <f.thomas@gmx.de>
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

package equites

object Rules {
  val fileRange = 0 to 7
  val rankRange = 0 to 7
  val maxLength = math.max(fileRange.length, rankRange.length) - 1

  val kingFile  = 4
  val queenFile = 3
  val rookFiles   = List(0, 7)
  val knightFiles = List(1, 6)
  val bishopFiles = List(2, 5)

  val movementType: Map[PieceType, Pair[Directions, Int]] = {
    import Directions._
    Map(King   -> (anywhere,   1),
        Queen  -> (anywhere,   maxLength),
        Rook   -> (orthogonal, maxLength),
        Bishop -> (diagonal,   maxLength),
        Knight -> (knightLike, 1))
  }

  val startingFields: Map[Color, Map[PieceType, List[Field]]] = {
    def startingFieldsFor(color: Color): Map[PieceType, List[Field]] = {
      val backRank = backRankBy(color)
      val pawnRank = pawnRankBy(color)

      Map[PieceType, List[Field]](
        King   -> List(Field(kingFile,  backRank)),
        Queen  -> List(Field(queenFile, backRank)),
        Rook   ->   rookFiles.map(Field(_, backRank)),
        Bishop -> bishopFiles.map(Field(_, backRank)),
        Knight -> knightFiles.map(Field(_, backRank)),
        Pawn   ->   fileRange.map(Field(_, pawnRank)).toList)
    }

    Map[Color, Map[PieceType, List[Field]]](
      White -> startingFieldsFor(White),
      Black -> startingFieldsFor(Black))
  }

  val castlingFields:
    Map[Triple[Side, Color, PieceType], Pair[Field, Field]] = {

    def castlingFieldsFor(side: Side, color: Color, pieceType: PieceType):
      Pair[Field, Field] = {

      val rank = backRankBy(color)
      val rookFile    = if (side == Kingside) rookFiles(1) else rookFiles(0)
      val leftOrRight = if (side == Kingside) 1 else -1

      pieceType match {
        case King =>
          (Field(kingFile, rank), Field(kingFile + 2 * leftOrRight, rank))
        case Rook =>
          (Field(rookFile, rank), Field(kingFile + 1 * leftOrRight, rank))
        case _ => throw new IllegalArgumentException
      }
    }

    val mappings = for {
      side  <- List(Kingside, Queenside)
      color <- List(White, Black)
      piece <- List(King, Rook)
    } yield (side, color, piece) -> castlingFieldsFor(side, color, piece)
    Map[Triple[Side, Color, PieceType], Pair[Field, Field]]() ++ mappings
  }

  def startingPositions(color: Color): Map[Field, Piece] = {
    def embattlePieces(pieceType: PieceType, newPiece: => Piece):
      List[Pair[Field, Piece]] = {

      startingFields(color)(pieceType).map(_ -> newPiece)
    }

    Map[Field, Piece]() ++
      embattlePieces(King,   new King(color))   ++
      embattlePieces(Queen,  new Queen(color))  ++
      embattlePieces(Rook,   new Rook(color))   ++
      embattlePieces(Bishop, new Bishop(color)) ++
      embattlePieces(Knight, new Knight(color)) ++
      embattlePieces(Pawn,   new Pawn(color))
  }

  def startingPositions: Map[Field, Piece] = {
    startingPositions(White) ++ startingPositions(Black)
  }

  def backRankBy(color: Color): Int = {
    if (color == White) rankRange.start else rankRange.end
  }

  def pawnRankBy(color: Color): Int = {
    if (color == White) rankRange.start + 1 else rankRange.end - 1
  }

  def fieldsInDirection(from: Field, direction: Vector,
    maxDist: Int = maxLength): Stream[Field] = {

    if (maxDist < 1 || !Field.validSum(from, direction))
      Stream.empty
    else {
      val next: Field = from + direction
      Stream.cons(next, fieldsInDirection(next, direction, maxDist - 1))
    }
  }
}

class Rules(board: Board) {
  def takeUntilOccupied(fields: Stream[Field], color: Color): List[Field] = {
    val occupied = fields.findIndexOf(board.occupied)
    if (occupied == -1)
      fields.toList
    else {
      val offset = if (board.opponentAt(fields(occupied), color)) 1 else 0
      fields.take(occupied + offset).toList
    }
  }
}

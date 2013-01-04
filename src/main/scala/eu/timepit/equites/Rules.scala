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

package eu.timepit.equites

object Rules {
  val fileRange = 0 to 7
  val rankRange = 0 to 7
  val maxLength = math.max(fileRange.length, rankRange.length) - 1

  val kingFile  = 4
  val queenFile = 3
  val rookFiles   = List(0, 7)
  val knightFiles = List(1, 6)
  val bishopFiles = List(2, 5)
/*
  val movementType: Map[PieceType, (Directions, Int)] = {
    import Directions._
    Map(King   -> (anywhere,   1),
        Queen  -> (anywhere,   maxLength),
        Rook   -> (straight,   maxLength),
        Bishop -> (diagonal,   maxLength),
        Knight -> (knightLike, 1))
  }

  val startingSquares: Map[Color, Map[PieceType, List[Square]]] = {
    def startingSquaresFor(color: Color): Map[PieceType, List[Square]] = {
      val backRank = backRankBy(color)
      val pawnRank = pawnRankBy(color)

      Map[PieceType, List[Square]](
        King   -> List(Square(kingFile,  backRank)),
        Queen  -> List(Square(queenFile, backRank)),
        Rook   ->   rookFiles.map(Square(_, backRank)),
        Bishop -> bishopFiles.map(Square(_, backRank)),
        Knight -> knightFiles.map(Square(_, backRank)),
        Pawn   ->   fileRange.map(Square(_, pawnRank)).toList)
    }

    Map[Color, Map[PieceType, List[Square]]](
      White -> startingSquaresFor(White),
      Black -> startingSquaresFor(Black))
  }
  */


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

    val mappings = for {
      side  <- List(Kingside, Queenside)
      color <- List(White, Black)
      piece <- List(King(color), Rook(color))
    } yield (side, piece) -> castlingSquaresFor(side, piece)
    mappings.toMap
  }

  /*
  def startingPositions(color: Color): Map[Square, Piece] = {
    def embattlePieces(pieceType: PieceType, newPiece: => Piece):
      List[(Square, Piece)] = {

      startingSquares(color)(pieceType).map(_ -> newPiece)
    }

    Map[Square, Piece]() ++
      embattlePieces(King,   new King(color))   ++
      embattlePieces(Queen,  new Queen(color))  ++
      embattlePieces(Rook,   new Rook(color))   ++
      embattlePieces(Bishop, new Bishop(color)) ++
      embattlePieces(Knight, new Knight(color)) ++
      embattlePieces(Pawn,   new Pawn(color))
  }

  def startingPositions: Map[Square, Piece] =
    startingPositions(White) ++ startingPositions(Black)
*/

  def backRankBy(color: Color): Int =
    if (color == White) rankRange.start else rankRange.end

  def pawnRankBy(color: Color): Int =
    if (color == White) rankRange.start + 1 else rankRange.end - 1

/*
  def rankBy(rank: Int, color: Color): Int =
    if (color == White) rank else rankRange.end - rank

  // better use an iterator
  def squaresInDirection(from: Square, direction: Vector,
    maxDist: Int = maxLength): Stream[Square] = {

    if (maxDist > 0 && Square.validSum(from, direction)) {
      val next: Square = from + direction
      Stream.cons(next, squaresInDirection(next, direction, maxDist - 1))
    }
    else Stream.empty
  }
  */
}

//class Rules(board: Board) {

/*
  def takeUntilOccupied(squares: Stream[Square], color: Color): List[Square] = {
    val occupied = squares.findIndexOf(board.occupied)
    if (occupied == -1)
      squares.toList
    else {
      val offset = if (board.opponentAt(squares(occupied), color)) 1 else 0
      squares.take(occupied + offset).toList
    }
  }
*/
/*  def possibleEnPassants(pawn: Pawn, at: Square): List[EnPassant] = {
    //if (rankBy(at.rank, pawn.color) != 4) Nil

    // schaue auf file +- 1 ob dort ein bauer steht,
    // wenn nein -> Nil
    // wenn ja ..
   // schaue in die history ob der pawn der letzte bewegte stein ist!!!
    Nil
  }
*/
/*
  def possibleCastlings(king: King, at: Square): List[Action] = {
    if (board.hasMoved(king)) Nil
  
    List()
  }

datenstruktur für piece auf dem Board: Pair[Piece, Square]

Rules sollte davon ausgehen, dass alle informationen über das board korrekt
sind. d.h. es sollten keine sanity checks auf dieser ebene durchgeführt werden
*/

  //def possibleActions(pawn: Pawn, at: Square)
  //def possibleActions(king: King, at: Square)

//def possibleCastlings
// def possiblePromotions
// de possibleEnPassants
// achte auf züge, die den könig matt setzen
//}

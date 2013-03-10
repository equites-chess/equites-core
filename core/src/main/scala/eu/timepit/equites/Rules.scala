// Equites, a Scala chess playground
// Copyright © 2011, 2013 Frank S. Thomas <frank@timepit.eu>
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

import scalaz._

import implicits.PlacedImplicits._

object Rules {
  val fileRange = 0 to 7
  val rankRange = 0 to 7
  val maxLength = math.max(fileRange.length, rankRange.length) - 1

  def fileSquares(file: Int): Seq[Square] = rankRange.map(Square(file, _))
  def rankSquares(rank: Int): Seq[Square] = fileRange.map(Square(_, rank))
  def allSquares: Seq[Square] = fileRange.flatMap(fileSquares(_))

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

  def onStartingSquare(placed: Placed[Piece]): Boolean =
    startingSquares(placed.piece) contains placed.square

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
      piece <- Seq(King(color), Rook(color))
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
    rankBy(rankRange.start, color)

  def pawnRankBy(color: Color): Int =
    rankBy(rankRange.start + 1, color)

  def enPassantRankBy(color: Color): Int =
    rankBy(rankRange.start + 3, color.opposite)

  def rankBy(rank: Int, color: Color): Int =
    if (color == White) rank else rankRange.end - rank

  val movementTypes: Map[Piece, (Directions, Int)] = {
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

  def movementTypeOf(placed: Placed[Piece]): (Directions, Int) = {
    val (directions, dist) = movementTypes(placed.piece)
    placed.piece match {
      case Pawn(_) if onStartingSquare(placed) => (directions, 2)
      case _ => (directions, dist)
    }
  }

  def squaresInDirection(from: Square, direction: Vec): Stream[Square] =
    Stream.iterate(from)(_ + direction).tail.takeWhile(_.isValid)

  def possibleSquares(placed: Placed[Piece]): Stream[Square] = {
    val (directions, dist) = movementTypeOf(placed)
    for {
      direction <- directions.toStream
      square <- squaresInDirection(placed.square, direction).take(dist)
    } yield square
  }

  def unvisitedSquares(placed: Placed[Piece], visited: Set[Square])
      : Stream[Square] =
    possibleSquares(placed).filterNot(visited)

  type BoardT[A] = State[Board, A]

  def movesInDirection(placed: Placed[Piece], direction: Vec)
      : BoardT[Stream[MoveLike]] = {
    def iterate(from: Square, board: Board): Stream[MoveLike] = {
      val next = from + direction
      board.get(next) match {
        case Some(piece) if (piece isOpponentOf placed)
          => Stream(Capture(placed, next, piece))
        case None
          => Move(placed, next) #:: iterate(next, board)
        case _
          => Stream()
      }
    }
    State(board => (board, iterate(placed.square, board)))
  }

  def genericMoves(placed: Placed[Piece]): BoardT[Stream[MoveLike]] = {
    val (directions, dist) = movementTypeOf(placed)
    def allMoves(board: Board) = for {
      direction <- directions.toStream
      move <- movesInDirection(placed, direction).eval(board).take(dist)
    } yield move
    State(board => (board, allMoves(board)))
  }

  def pawnMoves(placed: Placed[Pawn]): BoardT[Stream[MoveLike]] = {
    //moves
    //captures
    //enPassants
    //promotions
    ???
  }

  def kingMoves(placed: Placed[King]): BoardT[Stream[Action]] = {
    //moves
    //castlings
    ???
  }

  def possibleActions(placed: Placed[Piece]): BoardT[Stream[Action]] = {
    placed.piece match {
      case King(_) => kingMoves(placed.asInstanceOf[Placed[King]])
      case Pawn(_) => pawnMoves(placed.asInstanceOf[Placed[Pawn]])
      case _ => genericMoves(placed)
    }
    //checkmate
  }

  def isAttackedBy(square: Square, color: Color): BoardT[Boolean] = {
    // there needs to be a piece on square
    def xxx(board: Board) = {
      for {
        placed <- board.placedPieces.filter(_.color == color)
        action <- possibleActions(placed).eval(board)
        if action.isInstanceOf[CaptureLike]
        capture = action.asInstanceOf[CaptureLike]
        if capture.from == square
      } yield capture
    }
    State(board => (board, xxx(board).nonEmpty))
  }
}

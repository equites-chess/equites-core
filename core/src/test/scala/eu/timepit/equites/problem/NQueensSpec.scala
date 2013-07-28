package eu.timepit.equites
package problem

import org.specs2.mutable._

import NQueens._

class NQueensSpec extends Specification {
  "allBoards" should {
    "place queens on a board that do not attack each other" in {
      val board = allBoards(0)
      def nonattacking(placed: Placed[Piece]): Boolean =
        board.getPlaced(Rules.possibleSquares(placed)).isEmpty

      board.placedPieces.forall(nonattacking) must beTrue
    }
  }
}

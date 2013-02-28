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
package util

object BoardFactory {
  def apply(): BoardFactory =
    new BoardFactory(Board(), Square(0, Rules.rankRange.last))

  def |> : BoardFactory = apply()
}

class BoardFactory(board: Board, square: Square) {
  def K: BoardFactory = embattle(King(White))
  def Q: BoardFactory = embattle(Queen(White))
  def R: BoardFactory = embattle(Rook(White))
  def B: BoardFactory = embattle(Bishop(White))
  def N: BoardFactory = embattle(Knight(White))
  def P: BoardFactory = embattle(Pawn(White))

  def k: BoardFactory = embattle(King(Black))
  def q: BoardFactory = embattle(Queen(Black))
  def r: BoardFactory = embattle(Rook(Black))
  def b: BoardFactory = embattle(Bishop(Black))
  def n: BoardFactory = embattle(Knight(Black))
  def p: BoardFactory = embattle(Pawn(Black))

  def ♔ : BoardFactory = K ; def ♚ : BoardFactory = k
  def ♕ : BoardFactory = Q ; def ♛ : BoardFactory = q
  def ♖ : BoardFactory = R ; def ♜ : BoardFactory = r
  def ♗ : BoardFactory = B ; def ♝ : BoardFactory = b
  def ♘ : BoardFactory = N ; def ♞ : BoardFactory = n
  def ♙ : BoardFactory = P ; def ♟ : BoardFactory = p

  def - : BoardFactory = empty
  def <| : Board = board

  private def embattle(piece: Piece): BoardFactory =
    new BoardFactory(board + (square -> piece), nextSquare)

  private def empty: BoardFactory =
    new BoardFactory(board, nextSquare)

  private def nextSquare: Square = {
    val right = square.right
    if (right.isValid) right else square.down.leftmost
  }
}

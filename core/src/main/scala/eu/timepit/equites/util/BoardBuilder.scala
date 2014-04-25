// Equites, a Scala chess playground
// Copyright © 2013-2014 Frank S. Thomas <frank@timepit.eu>
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

import PieceAbbr.Algebraic

class BoardBuilder private (board: Board, square: Square) {
  def K: BoardBuilder = embattle(Algebraic.K)
  def Q: BoardBuilder = embattle(Algebraic.Q)
  def R: BoardBuilder = embattle(Algebraic.R)
  def B: BoardBuilder = embattle(Algebraic.B)
  def N: BoardBuilder = embattle(Algebraic.N)
  def P: BoardBuilder = embattle(Algebraic.P)

  def k: BoardBuilder = embattle(Algebraic.k)
  def q: BoardBuilder = embattle(Algebraic.q)
  def r: BoardBuilder = embattle(Algebraic.r)
  def b: BoardBuilder = embattle(Algebraic.b)
  def n: BoardBuilder = embattle(Algebraic.n)
  def p: BoardBuilder = embattle(Algebraic.p)

  def ♔ : BoardBuilder = K; def ♚ : BoardBuilder = k
  def ♕ : BoardBuilder = Q; def ♛ : BoardBuilder = q
  def ♖ : BoardBuilder = R; def ♜ : BoardBuilder = r
  def ♗ : BoardBuilder = B; def ♝ : BoardBuilder = b
  def ♘ : BoardBuilder = N; def ♞ : BoardBuilder = n
  def ♙ : BoardBuilder = P; def ♟ : BoardBuilder = p

  def | : BoardBuilder = nextBuilder(None)

  def << : Board = board

  private[this] def embattle(piece: AnyPiece): BoardBuilder =
    nextBuilder(Some(piece))

  private[this] def nextBuilder(pieceOpt: Option[AnyPiece]): BoardBuilder = {
    val nextBoard = pieceOpt.fold(board)(piece => board + (square -> piece))
    val nextSquare = square.right.orElse(square.down.map(_.leftmost)).getOrElse(square)
    new BoardBuilder(nextBoard, nextSquare)
  }
}

object BoardBuilder {
  def >> : BoardBuilder = new BoardBuilder(Board.empty, Square.topLeft)
}

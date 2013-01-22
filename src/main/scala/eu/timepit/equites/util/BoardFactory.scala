// Equites, a simple chess interface
// Copyright © 2013 Frank S. Thomas <f.thomas@gmx.de>
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
  def apply() = new BoardFactory(Board(), Square(0, Rules.rankRange.last))
  def |> = apply()
}

class BoardFactory(board: Board, square: Square) {
  def K = embattle(King(White))
  def Q = embattle(Queen(White))
  def R = embattle(Rook(White))
  def B = embattle(Bishop(White))
  def N = embattle(Knight(White))
  def P = embattle(Pawn(White))

  def k = embattle(King(Black))
  def q = embattle(Queen(Black))
  def r = embattle(Rook(Black))
  def b = embattle(Bishop(Black))
  def n = embattle(Knight(Black))
  def p = embattle(Pawn(Black))

  def ♜ = r

  def - = empty
  def <| = board

  private def embattle(piece: Piece): BoardFactory =
    new BoardFactory(board + (square -> piece), nextSquare)

  private def empty: BoardFactory =
    new BoardFactory(board, nextSquare)

  private def nextSquare: Square = {
    val right = square + Vec(1, 0)
    if (right.isValid) right
    else square - Vec(square.file, 1)
  }
}

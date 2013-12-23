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

trait AlgebraicAbbr {
  def K: WhiteKing   = Piece(White, King)
  def Q: WhiteQueen  = Piece(White, Queen)
  def R: WhiteRook   = Piece(White, Rook)
  def B: WhiteBishop = Piece(White, Bishop)
  def N: WhiteKnight = Piece(White, Knight)
  def P: WhitePawn   = Piece(White, Pawn)

  def k: BlackKing   = Piece(Black, King)
  def q: BlackQueen  = Piece(Black, Queen)
  def r: BlackRook   = Piece(Black, Rook)
  def b: BlackBishop = Piece(Black, Bishop)
  def n: BlackKnight = Piece(Black, Knight)
  def p: BlackPawn   = Piece(Black, Pawn)
}

trait FigurineAbbr {
  import PieceAbbr.Algebraic._

  def ♔ : WhiteKing   = K
  def ♕ : WhiteQueen  = Q
  def ♖ : WhiteRook   = R
  def ♗ : WhiteBishop = B
  def ♘ : WhiteKnight = N
  def ♙ : WhitePawn   = P

  def ♚ : BlackKing   = k
  def ♛ : BlackQueen  = q
  def ♜ : BlackRook   = r
  def ♝ : BlackBishop = b
  def ♞ : BlackKnight = n
  def ♟ : BlackPawn   = p
}

trait WikiAbbr {
  import PieceAbbr.Algebraic._

  def kl: WhiteKing   = K
  def ql: WhiteQueen  = Q
  def rl: WhiteRook   = R
  def bl: WhiteBishop = B
  def nl: WhiteKnight = N
  def pl: WhitePawn   = P

  def kd: BlackKing   = k
  def qd: BlackQueen  = q
  def rd: BlackRook   = r
  def bd: BlackBishop = b
  def nd: BlackKnight = n
  def pd: BlackPawn   = p
}

object PieceAbbr {
  object Algebraic extends AlgebraicAbbr
  object Figurine extends FigurineAbbr
  object Wiki extends WikiAbbr
}

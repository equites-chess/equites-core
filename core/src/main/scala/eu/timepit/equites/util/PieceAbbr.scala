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
  def K: King   = King(White)
  def Q: Queen  = Queen(White)
  def R: Rook   = Rook(White)
  def B: Bishop = Bishop(White)
  def N: Knight = Knight(White)
  def P: Pawn   = Pawn(White)

  def k: King   = King(Black)
  def q: Queen  = Queen(Black)
  def r: Rook   = Rook(Black)
  def b: Bishop = Bishop(Black)
  def n: Knight = Knight(Black)
  def p: Pawn   = Pawn(Black)
}

trait FigurineAbbr {
  import PieceAbbr.Algebraic._

  def ♔ : King   = K
  def ♕ : Queen  = Q
  def ♖ : Rook   = R
  def ♗ : Bishop = B
  def ♘ : Knight = N
  def ♙ : Pawn   = P

  def ♚ : King   = k
  def ♛ : Queen  = q
  def ♜ : Rook   = r
  def ♝ : Bishop = b
  def ♞ : Knight = n
  def ♟ : Pawn   = p
}

trait WikiAbbr {
  import PieceAbbr.Algebraic._

  def kl: King   = K
  def ql: Queen  = Q
  def rl: Rook   = R
  def bl: Bishop = B
  def nl: Knight = N
  def pl: Pawn   = P

  def kd: King   = k
  def qd: Queen  = q
  def rd: Rook   = r
  def bd: Bishop = b
  def nd: Knight = n
  def pd: Pawn   = p
}

object PieceAbbr {
  object Algebraic extends AlgebraicAbbr
  object Figurine extends FigurineAbbr
  object Wiki extends WikiAbbr
}

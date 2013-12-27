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

import shapeless._
import nat._

import PieceAbbr.Algebraic

object SizedBoardBuilder {
  def K = Some(Algebraic.K)
  def Q = Some(Algebraic.Q)
  def R = Some(Algebraic.R)
  def B = Some(Algebraic.B)
  def N = Some(Algebraic.N)
  def P = Some(Algebraic.P)

  def k = Some(Algebraic.k)
  def q = Some(Algebraic.q)
  def r = Some(Algebraic.r)
  def b = Some(Algebraic.b)
  def n = Some(Algebraic.n)
  def p = Some(Algebraic.p)

  def ♔ = K; def ♚ = k
  def ♕ = Q; def ♛ = q
  def ♖ = R; def ♜ = r
  def ♗ = B; def ♝ = b
  def ♘ = N; def ♞ = n
  def ♙ = P; def ♟ = p

  def | : Option[AnyPiece] = None

  def >> = Sized

  type SizedRank[N <: Nat] = Sized[Seq[Option[AnyPiece]], N]
  type SizedBoard[N <: Nat] = Sized[Seq[SizedRank[N]], N]

  implicit final class RichSizedBoard[N <: Nat](val sizedBoard: SizedBoard[N]) {
    def toBoard[M](implicit ev: N =:= M): Board = buildBoard
    def toBoard8x8(implicit ev: N =:= _8): Board = buildBoard

    private[this] def buildBoard: Board = {
      val maxRank = sizedBoard.length - 1
      val mapping = for {
        (sizedRank, rank) <- sizedBoard.zipWithIndex
        (pieceOpt, file) <- sizedRank.zipWithIndex
        piece <- pieceOpt
        square = Square(file, maxRank - rank)
      } yield square -> piece
      Board(mapping.toMap)
    }
  }
}

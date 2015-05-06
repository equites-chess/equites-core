// Equites, a Scala chess playground
// Copyright © 2013-2015 Frank S. Thomas <frank@timepit.eu>
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

import eu.timepit.equites.util.PieceAbbr.Algebraic
import shapeless._
import shapeless.nat._

object SizedBoardBuilder {
  def K: Option[AnyPiece] = Some(Algebraic.K)
  def Q: Option[AnyPiece] = Some(Algebraic.Q)
  def R: Option[AnyPiece] = Some(Algebraic.R)
  def B: Option[AnyPiece] = Some(Algebraic.B)
  def N: Option[AnyPiece] = Some(Algebraic.N)
  def P: Option[AnyPiece] = Some(Algebraic.P)

  def k: Option[AnyPiece] = Some(Algebraic.k)
  def q: Option[AnyPiece] = Some(Algebraic.q)
  def r: Option[AnyPiece] = Some(Algebraic.r)
  def b: Option[AnyPiece] = Some(Algebraic.b)
  def n: Option[AnyPiece] = Some(Algebraic.n)
  def p: Option[AnyPiece] = Some(Algebraic.p)

  def ♔ : Option[AnyPiece] = K
  def ♕ : Option[AnyPiece] = Q
  def ♖ : Option[AnyPiece] = R
  def ♗ : Option[AnyPiece] = B
  def ♘ : Option[AnyPiece] = N
  def ♙ : Option[AnyPiece] = P

  def ♚ : Option[AnyPiece] = k
  def ♛ : Option[AnyPiece] = q
  def ♜ : Option[AnyPiece] = r
  def ♝ : Option[AnyPiece] = b
  def ♞ : Option[AnyPiece] = n
  def ♟ : Option[AnyPiece] = p

  def | : Option[AnyPiece] = None

  def >> = Sized

  private type SizedRank[N <: Nat] = Sized[Seq[Option[AnyPiece]], N]
  private type SizedBoard[N <: Nat] = Sized[Seq[SizedRank[N]], N]

  implicit final class RichSizedBoard[N <: Nat](val sizedBoard: SizedBoard[N]) {
    def toBoard[M](implicit ev: N =:= M): Board = buildBoard
    def toBoard8x8(implicit ev: N =:= _8): Board = buildBoard

    private[this] def buildBoard: Board = {
      val maxRank = sizedBoard.length - 1
      val mapping = for {
        (sizedRank, reverseRank) <- sizedBoard.zipWithIndex
        (pieceOpt, file) <- sizedRank.zipWithIndex
        piece <- pieceOpt
        rank = maxRank - reverseRank
        square <- Square.from(File(file), Rank(rank))
      } yield square -> piece
      Board(mapping.toMap)
    }
  }
}

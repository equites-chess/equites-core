// Equites, a Scala chess playground
// Copyright © 2011-2013 Frank S. Thomas <frank@timepit.eu>
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

import scala.collection.immutable._

object Directions {
  def apply(vectors: Vec*): Directions = apply(vectors)
  def apply(vectors: TraversableOnce[Vec]): Directions =
    new Directions(vectors.toSet)

  val front = Directions(Vec( 0,  1)) // ↑
  val right = Directions(Vec( 1,  0)) // →
  val back  = Directions(Vec( 0, -1)) // ↓
  val left  = Directions(Vec(-1,  0)) // ←

  val frontRight = Directions(Vec( 1,  1)) // ↗
  val backRight  = Directions(Vec( 1, -1)) // ↘
  val backLeft   = Directions(Vec(-1, -1)) // ↙
  val frontLeft  = Directions(Vec(-1,  1)) // ↖

  val diagonalFront = Directions(frontLeft ++ frontRight) // ↖↗
  val diagonalBack  = Directions( backLeft ++  backRight) // ↙↘

  val forward  = Directions(diagonalFront ++ front) // ↖↑↗
  val backward = Directions(diagonalBack  ++ back)  // ↙↓↘

  val straight = Directions(front ++ right ++ back ++ left)
  val diagonal = Directions(diagonalFront ++ diagonalBack)
  val anywhere = Directions(straight ++ diagonal)

  val knightLike = Directions({
    for {
      vec <- Vec(-2, -2) to Vec(2, 2)
      if vec.l1Length == 3
    } yield vec
  })
}

class Directions(val self: Set[Vec]) extends SetProxy[Vec] {
  def inverse: Directions = Directions(map(_ * -1))

  def inverseIfWhite(color: Color): Directions =
    if (color == White) inverse else this

  def inverseIfBlack(color: Color): Directions =
    if (color == Black) inverse else this

  def fromPov(color: Color): Directions = inverseIfBlack(color)
  def fromPov(piece: Piece): Directions = fromPov(piece.color)
}

// Equites, a simple chess interface
// Copyright © 2011 Frank S. Thomas <f.thomas@gmx.de>
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

package equites

import scala.collection._

object Directions {
  def apply(vectors: Vector*): Directions = apply(vectors)
  def apply(vectors: TraversableOnce[Vector]): Directions =
    new Directions(vectors.toSet)

  val front = Directions(Vector( 0,  1)) // ↑
  val right = Directions(Vector( 1,  0)) // →
  val back  = Directions(Vector( 0, -1)) // ↓
  val left  = Directions(Vector(-1,  0)) // ←

  val frontRight = Directions(Vector( 1,  1)) // ↗
  val backRight  = Directions(Vector( 1, -1)) // ↘
  val backLeft   = Directions(Vector(-1, -1)) // ↙
  val frontLeft  = Directions(Vector(-1,  1)) // ↖

  val diagonalFront = Directions(frontLeft ++ frontRight) // ↖↗
  val diagonalBack  = Directions( backLeft ++  backRight) // ↙↘

  val forward  = Directions(diagonalFront ++ front) // ↖↑↗
  val backward = Directions(diagonalBack  ++ back)  // ↙↓↘

  val straight = Directions(front ++ right ++ back ++ left)
  val diagonal = Directions(diagonalFront ++ diagonalBack)
  val anywhere = Directions(straight ++ diagonal)

  val knightLike = Directions({
    for {
      file <- -2 to 2
      rank <- -2 to 2
      if file.abs + rank.abs == 3
    } yield Vector(file, rank)
  })
}

class Directions private (val self: Set[Vector])
  extends SetProxy[Vector] {

  def inverse: Directions = Directions(map(_ * -1))

  def inverseIfBlack(color: Color): Directions =
    if (color == Black) inverse else this
}

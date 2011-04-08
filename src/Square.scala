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

object Square {
  def validCoordinates(file: Int, rank: Int): Boolean = {
    Rules.fileRange.contains(file) &&
    Rules.rankRange.contains(rank)
  }

  def validSum(that: Square, vec: Vector): Boolean =
    validCoordinates(that.file + vec.file, that.rank + vec.rank)

  def l1Dist(p: Square, q: Square): Int = (p - q).map(_.abs).sum
  def lInfDist(p: Square, q: Square): Int = (p - q).map(_.abs).max
}

case class Square(file: Int, rank: Int) {
  require(Square.validCoordinates(file, rank))

  def +(vec: Vector): Square = Square(file + vec.file, rank + vec.rank)
  def -(vec: Vector): Square = Square(file - vec.file, rank - vec.rank)
  def -(that: Square): Vector = Vector(file - that.file, rank - that.rank)
}

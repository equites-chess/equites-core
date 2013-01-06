// Equites, a simple chess interface
// Copyright © 2011-2013 Frank S. Thomas <f.thomas@gmx.de>
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

import utils.Math._
import utils.Notation._

object Square {
  def apply(algebraicFile: Char, algebraicRank: Int): Square =
    Square(algebraicFileRange.indexOf(algebraicFile),
           algebraicRankRange.indexOf(algebraicRank))

  def validCoordinates(file: Int, rank: Int): Boolean = {
    Rules.fileRange.contains(file) &&
    Rules.rankRange.contains(rank)
  }

  def validSum(that: Square, vec: Vec): Boolean =
    validCoordinates(that.file + vec.file, that.rank + vec.rank)

  def l1Dist(p: Square, q: Square): Int = p.l1Dist(q)
  def lInfDist(p: Square, q: Square): Int = p.lInfDist(q)
}

case class Square(file: Int, rank: Int) {
  require(Square.validCoordinates(file, rank))

  def +(vec: Vec): Square = Square(file + vec.file, rank + vec.rank)
  def -(vec: Vec): Square = Square(file - vec.file, rank - vec.rank)

  def +(that: Square): Vec = Vec(file + that.file, rank + that.rank)
  def -(that: Square): Vec = Vec(file - that.file, rank - that.rank)

  def isLight: Boolean = isOdd(sum)
  def isDark: Boolean = isEven(sum)

  def l1Dist(that: Square): Int = (this - that).l1Length
  def lInfDist(that: Square): Int = (this - that).lInfLength

  private def sum: Int = file + rank
}

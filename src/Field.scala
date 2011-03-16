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

object Field {
  def apply(str: String): Field = fromAlgebraicNotation(str)

  def fromAlgebraicNotation(str: String): Field = {
    require(str.length == 2)
    require(isValidFile(str(0)) &&
            isValidRank(str(1)))

    val file: Int = str(0) - 'a'
    val rank: Int = str(1) - '1'
    Field(file, rank)
  }

  private def isValidCoordinate(i: Int): Boolean = i >= 0 && i <= 7
  private def isValidFile(c: Char): Boolean = c >= 'a' && c <= 'h'
  private def isValidRank(c: Char): Boolean = c >= '1' && c <= '8'
}

case class Field(file: Int, rank: Int) {
  require(Field.isValidCoordinate(file) &&
          Field.isValidCoordinate(rank))

  def +(vec: Vector): Field = Field(file + vec.file, rank + vec.rank)
  def -(vec: Vector): Field = Field(file - vec.file, rank - vec.rank)

  def toAlgebraicNotation(): String = {
    ('a' + file).toChar.toString + (1 + rank).toString
  }
}

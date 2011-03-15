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
  def apply(file: Int, rank: Int): Field = new Field(file, rank)

  def apply(str: String): Field = fromAlgebraicNotation(str)

  def fromAlgebraicNotation(str: String): Field = {
    AlgebraicField(str).toField
  }
}

class Field(val file: Int, val rank: Int)
  extends Pair[Int, Int](file, rank) {

  require(file >= 0 && rank >= 0)

  def toAlgebraicNotation(): String = AlgebraicField(this).toString
}

object AlgebraicField {
  def apply(str: String): AlgebraicField = new AlgebraicField(str)

  def apply(field: Field): AlgebraicField = fromField(field)

  def fromField(field: Field): AlgebraicField = {
    require(field.file < 8 && field.rank < 8)

    val str = ('a' + field.file).toChar.toString + (1 + field.rank).toString
    AlgebraicField(str)
  }
}

class AlgebraicField(str: String) {
  require(str.length == 2)
  require('a' to 'h' contains str(0))
  require('1' to '8' contains str(1))

  private val file: Int = str(0) - 'a'
  private val rank: Int = str(1) - '1'
  private val field = Field(file, rank)

  def toField(): Field = field

  override def toString(): String = str
}

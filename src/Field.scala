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

case class Field(file: Int, rank: Int) {
  require(isValidCoordinate(file) &&
          isValidCoordinate(rank))

  def +(vec: Vector): Field = Field(file + vec.file, rank + vec.rank)
  def -(vec: Vector): Field = Field(file - vec.file, rank - vec.rank)
  def -(that: Field): Vector = Vector(file - that.file, rank - that.rank)
  def distTo(that: Field): Int = (that - this).map(_.abs).max

  private def isValidCoordinate(i: Int): Boolean = i >= 0 && i <= 7
}

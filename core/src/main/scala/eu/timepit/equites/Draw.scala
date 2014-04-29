// Equites, a Scala chess playground
// Copyright © 2014 Frank S. Thomas <frank@timepit.eu>
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

case class Draw(from: Square, to: Square) {
  def direction: Vec = to - from

  def isNull: Boolean = from == to
  def nonNull: Boolean = !isNull

  def l1Length: Int = direction.l1Length
  def lInfLength: Int = direction.lInfLength

  def squares: Seq[Square] = Seq(from, to)
}
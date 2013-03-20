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

trait PlayerPerspective[T] {
  self: T =>

  def inverse: T

  def inverseIfWhite(color: Color): T =
    if (color == White) inverse else this

  def inverseIfBlack(color: Color): T =
    if (color == Black) inverse else this

  def fromPov(color: Color): T = inverseIfBlack(color)
}

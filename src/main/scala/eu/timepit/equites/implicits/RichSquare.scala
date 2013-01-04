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

package eu.timepit.equites
package implicits

import utils.Notation

object RichSquareImplicit {
  implicit def   wrapSquare(square: Square) = new RichSquare(square)
  implicit def unwrapSquare(square: RichSquare) = square.self
}

final class RichSquare(val self: Square) extends Proxy {
  def toAlgebraic: String = {
    Notation.algebraicFileRange(self.file).toString +
    Notation.algebraicRankRange(self.rank).toString
  }

  def toNumeric: String = {
    Notation.numericFileRange(self.file).toString +
    Notation.numericRankRange(self.rank).toString
  }
}

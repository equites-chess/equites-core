// Equites, a Scala chess playground
// Copyright © 2011, 2013 Frank S. Thomas <frank@timepit.eu>
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

import util.Notation._

object SquareImplicits {
  implicit final class RichSquare(val self: Square) extends AnyVal {
    def toAlgebraic: String = {
      algebraicFileRange(self.file).toString +
      algebraicRankRange(self.rank).toString
    }

    def toNumeric: String = {
      numericFileRange(self.file).toString +
      numericRankRange(self.rank).toString
    }
  }
}

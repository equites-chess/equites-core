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
package implicits

import RichActionImplicit._
import RichFieldImplicit._
import RichPieceImplicit._
import utils.Notation

object RichActionImplicit {
  implicit def actionWrapper(action: Action) = new RichAction(action)
}

final class RichAction(val action: Action) {
  def toNumeric: String = action match {
    case p: PromotionLike
      => p.from.toNumeric + p.to.toNumeric + p.newPiece.toNumeric
    case m: MoveLike
      => m.from.toNumeric + m.to.toNumeric
    case c: Castling
      => c.kingMove.toNumeric
  }
}

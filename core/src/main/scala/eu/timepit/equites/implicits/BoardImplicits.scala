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
package implicits

import implicits.PieceImplicits._

object BoardImplicits {
  implicit final class RichBoard(val self: Board) extends AnyVal {
    def toFenPlacement: String = {
      def replaceOnes(target: String): String =
        "1{2,}".r.replaceAllIn(target, _.toString.length.toString)

      Rules.rankRange.reverse.map { rank =>
        val rankStr = Rules.fileRange.map { file =>
          val square = Square(file, rank)
          val optPiece = self.get(square)
          optPiece.fold("1")(_.toLetter)
        }.mkString
        replaceOnes(rankStr)
      }.mkString("/")
    }
  }
}

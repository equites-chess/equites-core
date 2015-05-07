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
package util

import GameResult._

object GameResultUtil {
  def showPgnMarker(result: GameResult): String =
    result match {
      case WhiteWon => "1-0"
      case BlackWon => "0-1"
      case Draw     => "1/2-1/2"
      case Other    => "*"
    }
}

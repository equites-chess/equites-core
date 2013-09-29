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
package util

import java.nio.charset.Charset

trait TextCommand extends Product {
  def cmdName: String = productPrefix.toLowerCase
  def cmdArgs: Seq[String] = productIterator.toSeq.map(_.toString)
  override def toString: String = cmdName + cmdArgs.map(" " + _).mkString

  def toUtf8: Array[Byte] = toString.getBytes(utf8Charset)
  private val utf8Charset: Charset = Charset.forName("UTF-8")
}

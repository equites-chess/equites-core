// Equites, a Scala chess playground
// Copyright © 2013-2014 Frank S. Thomas <frank@timepit.eu>
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

import scala.util.Random
import scalaz._
import scalaz.concurrent.Task

object Rand {
  type Rand[A] = State[Random, A]

  def init: Rand[Random] = State.init

  def pure[A](a: A): Rand[A] = State.state(a)

  def randInt(n: Int): Rand[Int] = init.map(_.nextInt(n))

  def randElem[A](from: Seq[A]): Rand[Option[A]] =
    from.length match {
      case 0 => pure(None)
      case n => randInt(n).map(n => Some(from(n)))
    }

  def randRangeElem(range: Range): Rand[Int] =
    randElem(range).map(_.getOrElse(range.start))

  def eval[A](rand: Rand[A]): Task[A] = Task.delay(rand.eval(Random))

  def eval[T1, T2, R](f: (T1, T2) => Rand[R]): Task[(T1, T2) => R] =
    Task.delay((v1, v2) => f(v1, v2).eval(Random))
}

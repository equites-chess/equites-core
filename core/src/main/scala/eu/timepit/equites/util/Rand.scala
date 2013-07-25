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

import scala.language.higherKinds

import scala.util.Random
import scalaz._

object Rand {
  type Rand[A] = State[Random, A]

  def rand[A](a: A): Rand[A] = State.state(a)

  def nextInt(n: Int): Rand[Int] = State.init.map(_.nextInt(n))

  // impure
  def eval[A](rand: Rand[A]): A = rand.eval(Random)

  // impure
  def evalFn2[T1, T2, R](f: (T1, T2) => Rand[R]): (T1, T2) => R =
    (v1, v2) => eval(f(v1, v2))

  def pickRandom[A, C[A]](from: C[A])(implicit I: Index[C], L: Length[C])
      : Rand[Option[A]] =
    L.length(from) match {
      case 0 => rand(None)
      case x => nextInt(x).map(I.index(from, _))
    }

  def pickRandomImpure[A, C[A] : Index : Length](from: C[A]): Option[A] =
    eval(pickRandom(from))
}

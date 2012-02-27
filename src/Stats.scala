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

import scala.collection._

class Stats private (val self: List[Action]) extends SeqProxy[Action] {
  def this() = this(Nil)

  def moves: Int = self.size

//  def l1Dist: Int = self.map(_.l1Dist)
//  def lInfDist: Int =

// collectMoves: List[Moves]
}

/*
class StatsMap private (val self: Map[Piece, Stats])
  extends MapProxy[Piece, Stats] {

  def this() = this(Map())
//  def this(pieces: Traversable[Piece]) =
//    this(pieces map ((_, new Stats())) toMap)
}
*/

/*
case class Stats(
  // alle moves aufzeichnen als list und daraus anzahl der moves berechnen
  // wie auch alles andere! toll
  moves:    Int = 0,
  l1Dist:   Int = 0,
  lInfDist: Int = 0,
  checking: Int = 0,
  checkmating: Boolean = false,
  stalemating: Boolean = false,
  // wasCaptured?
  visited:  List[Square] = Nil,
  captured: List[(Square, Piece)] = Nil) {

  def hasMoved: Boolean = moves > 0
}

object StatsMap {
  def apply(pieces: Piece*): StatsMap = apply(pieces)
  def apply(pieces: Traversable[Piece]): StatsMap =
    new StatsMap(pieces.map(piece => (piece, Stats())).toMap)
}

class StatsMap private (val self: Map[Piece, Stats])
  extends MapProxy[Piece, Stats] {

}
*/

/*
// StatsMap soll Actions verarbeiten können
StatsMap extends MapLike[Piece, Stats]
Stats

class Stats private (private val stats: Map[Piece, PieceStats])
  extends Iterable[(Piece, PieceStats)] {

}
*/
/*
make object with apply to create Stats with 
*/

/*
case class StatsEntry(
  moves:    Int = 0,
  l1Dist:   Int = 0,
  lInfDist: Int = 0,
  checking: Int = 0,
  captured: List[Piece] = Nil) {

  def hasMoved: Boolean = moves > 0
}

class Stats extends ActionListener with Iterable[(Piece, StatsEntry)] {
  def apply(piece: Piece): StatsEntry = stats(piece)

  def add(piece: Piece): Option[StatsEntry] = stats.put(piece, StatsEntry())
  def remove(piece: Piece): Option[StatsEntry] = stats.remove(piece)

  def add(pieces: TraversableOnce[Piece]) {
    pieces.foreach(add)
  }

  def remove(pieces: TraversableOnce[Piece]) {
    pieces.foreach(remove)
  }

  def clear() {
    stats.clear()
  }

  // benutze noch eine maps für StatsEntry?!?!
  // lass StatsEntry sich selber aktualisieren
  def perform(move: MoveLike) {
    //updateEntry(move.piece, 1, move.l1Dist, move.lInfDist, 0, Nil)
    //require(stats contains move.piece)
  }

  def reverse(move: MoveLike) {
  }

  def iterator: Iterator[(Piece, StatsEntry)] = stats.iterator

  private val stats: mutable.Map[Piece, StatsEntry] = mutable.Map()
}
*/

/*
class MoveCounter extends ActionListener {
  def totalMoves(piece: Piece): Int = {
    require(counter contains piece)
    counter(piece)
  }

  def hasMoved(piece: Piece): Boolean = {
    require(counter contains piece)
    counter(piece) > 0
  }

  def processAction(move: MoveLike) {
    incr(move.piece)
  }

  def reverseAction(move: MoveLike) {
    decr(move.piece)
  }

  def processAction(promo: PromotionLike) {
    incr(promo.piece)
    addPiece(promo.newPiece)
  }

  def reverseAction(promo: PromotionLike) {
    removePiece(promo.newPiece)
    decr(promo.piece)
  }

  def processAction(castling: Castling) {
    processAction(castling.kingMove)
  }

  def reverseAction(castling: Castling) {
    reverseAction(castling.kingMove)
  }

  // changeCount of=... by=...
  private def changeCount(piece: Piece, by: Int): Int = {
    require(counter contains piece)
    val result = counter(piece) + by
    require(result >= 0)

    counter(piece) = result
    result
  }

  private def incr(piece: Piece): Int = changeCount(piece,  1)
  private def decr(piece: Piece): Int = changeCount(piece, -1)
}
*/

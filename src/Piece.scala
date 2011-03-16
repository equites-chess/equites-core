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

abstract class PieceCompanion(val algebraicNotation: String)

object King   extends PieceCompanion(algebraicNotation = "K")
object Queen  extends PieceCompanion(algebraicNotation = "Q")
object Rook   extends PieceCompanion(algebraicNotation = "R")
object Bishop extends PieceCompanion(algebraicNotation = "B")
object Knight extends PieceCompanion(algebraicNotation = "N")
object Pawn   extends PieceCompanion(algebraicNotation = "P")


object Piece {
  def apply(str: String, color: Color): Piece = {
    fromAlgebraicNotation(str, color)
  }

  def fromAlgebraicNotation(str: String, color: Color): Piece = {
    str.toUpperCase match {
      case King.algebraicNotation   => new King(color)
      case Queen.algebraicNotation  => new Queen(color)
      case Rook.algebraicNotation   => new Rook(color)
      case Bishop.algebraicNotation => new Bishop(color)
      case Knight.algebraicNotation => new Knight(color)
      case Pawn.algebraicNotation   => new Pawn(color)
      case _ => throw new IllegalArgumentException
    }
  }
}

abstract case class Piece(val color: Color,
  private val companion: PieceCompanion) {

  def toAlgebraicNotation(): String = companion.algebraicNotation
}

class King(color: Color)   extends Piece(color, King)
class Queen(color: Color)  extends Piece(color, Queen)
class Rook(color: Color)   extends Piece(color, Rook)
class Bishop(color: Color) extends Piece(color, Bishop)
class Knight(color: Color) extends Piece(color, Knight)
class Pawn(color: Color)   extends Piece(color, Pawn)

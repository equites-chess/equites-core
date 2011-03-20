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
package utils


/*
  def fromAlgebraicNotation(str: String): Field = {
    require(str.length == 2)
    require(isValidFile(str(0)) &&
            isValidRank(str(1)))

    val file: Int = str(0) - 'a'
    val rank: Int = str(1) - '1'
    Field(file, rank)
  }

  private def isValidFile(c: Char): Boolean = c >= 'a' && c <= 'h'
  private def isValidRank(c: Char): Boolean = c >= '1' && c <= '8'
*/

/*
object Move {
  def apply(from: Field, vec: Vector): Move = Move(from, from + vec)
  def apply(str: String): Move = fromAlgebraicNotation(str)

  def fromAlgebraicNotation(str: String): Move = {
    require(str.length == 5)
    require(List('-', 'x') contains str(2))

    val from = Field(str.substring(0, 2))
    val to   = Field(str.substring(3, 5))
    Move(from, to)
  }
}
*/

/*
abstract class Notation {
  def serialize(field: Field): String
  def serialize(move: Move): String
  def serialize(piece: Piece): String

  //def unserializeField(str: String): Field
  //def unserializeMove(str: String): Move
}

class LongAlgebraicNotation extends Notation {
  override def serialize(field: Field): String = {
    ('a' + field.file).toChar.toString + (1 + field.rank).toString
  }

  override def serialize(move: Move): String = {
    serialize(move.from) + "-" + serialize(move.to)
  }

}
*/

/*
trait LetterPieces {
  def serialize(piece: Piece): String = {
    piece match {
      case King(_)   => "K"
      case Queen(_)  => "Q"
      case Rook(_)   => "R"
      case Bishop(_) => "B"
      case Knight(_) => "N"
      case Pawn(_)   => ""
    }
  }
*/
/*
  def unserializePiece(str: String): Piece = {
    str match {
      case "K" => new King(color)
      case "Q" => new Queen(color)
      case "R" => new Rook(color)
      case "B" => new Bishop(color)
      case "N" => new Knight(color)
      case ""  => new Pawn(color)
    }
  }
}
*/

/*
trait FigurinePieces {
  def serialize(piece: Piece): String = {
    import Color._
    piece match {
      case King(color)   => selectBy(color, "\u2654", "\u265A") // ♔ ♚
      case Queen(color)  => selectBy(color, "\u2655", "\u265B") // ♕ ♛
      case Rook(color)   => selectBy(color, "\u2656", "\u265C") // ♖ ♜
      case Bishop(color) => selectBy(color, "\u2657", "\u265D") // ♗ ♝
      case Knight(color) => selectBy(color, "\u2658", "\u265E") // ♘ ♞
      case Pawn(color)   => selectBy(color, "\u2659", "\u265F") // ♙ ♟
    }
  }
}
*/

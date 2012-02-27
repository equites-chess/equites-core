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
package text

import org.specs2.mutable._

class TextBoardSpec extends Specification {
  "class TextBoard" should {
    "correctly perform (un)labeledBoard with Letters" in {
      val board = new Board
      board.putPieces(Rules.startingPositions)
      val tb = new TextBoard(board) with Letters

      tb.unlabeledBoard must_==
        "r n b q k b n r \n" +
        "p p p p p p p p \n" +
        ". . . . . . . . \n" +
        ". . . . . . . . \n" +
        ". . . . . . . . \n" +
        ". . . . . . . . \n" +
        "P P P P P P P P \n" +
        "R N B Q K B N R \n"

      tb.labeledBoard must_==
        "r n b q k b n r  8\n" +
        "p p p p p p p p  7\n" +
        ". . . . . . . .  6\n" +
        ". . . . . . . .  5\n" +
        ". . . . . . . .  4\n" +
        ". . . . . . . .  3\n" +
        "P P P P P P P P  2\n" +
        "R N B Q K B N R  1\n" +
        "\n" +
        "a b c d e f g h \n"

      board.clear()
      tb.labeledBoard must_==
        ". . . . . . . .  8\n" +
        ". . . . . . . .  7\n" +
        ". . . . . . . .  6\n" +
        ". . . . . . . .  5\n" +
        ". . . . . . . .  4\n" +
        ". . . . . . . .  3\n" +
        ". . . . . . . .  2\n" +
        ". . . . . . . .  1\n" +
        "\n" +
        "a b c d e f g h \n"
    }

    "correctly perform (un)labeledBoard with Figurine" in {
      val board = new Board
      board.putPieces(Rules.startingPositions)
      val tb = new TextBoard(board) with Figurine

      tb.unlabeledBoard must_==
        "♜ ♞ ♝ ♛ ♚ ♝ ♞ ♜ \n" +
        "♟ ♟ ♟ ♟ ♟ ♟ ♟ ♟ \n" +
        "· · · · · · · · \n" +
        "· · · · · · · · \n" +
        "· · · · · · · · \n" +
        "· · · · · · · · \n" +
        "♙ ♙ ♙ ♙ ♙ ♙ ♙ ♙ \n" +
        "♖ ♘ ♗ ♕ ♔ ♗ ♘ ♖ \n"

      tb.labeledBoard must_==
        "♜ ♞ ♝ ♛ ♚ ♝ ♞ ♜│ 8\n" +
        "♟ ♟ ♟ ♟ ♟ ♟ ♟ ♟│ 7\n" +
        "· · · · · · · ·│ 6\n" +
        "· · · · · · · ·│ 5\n" +
        "· · · · · · · ·│ 4\n" +
        "· · · · · · · ·│ 3\n" +
        "♙ ♙ ♙ ♙ ♙ ♙ ♙ ♙│ 2\n" +
        "♖ ♘ ♗ ♕ ♔ ♗ ♘ ♖│ 1\n" +
        "───────────────┘\n" +
        "a b c d e f g h \n"

      board.clear()
      tb.labeledBoard must_==
        "· · · · · · · ·│ 8\n" +
        "· · · · · · · ·│ 7\n" +
        "· · · · · · · ·│ 6\n" +
        "· · · · · · · ·│ 5\n" +
        "· · · · · · · ·│ 4\n" +
        "· · · · · · · ·│ 3\n" +
        "· · · · · · · ·│ 2\n" +
        "· · · · · · · ·│ 1\n" +
        "───────────────┘\n" +
        "a b c d e f g h \n"
    }
  }
}

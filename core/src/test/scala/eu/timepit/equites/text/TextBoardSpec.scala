// Equites, a Scala chess playground
// Copyright © 2011, 2013 Frank S. Thomas <frank@timepit.eu>
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
package text

import org.specs2.mutable._

class TextBoardSpec extends Specification {
  "TextBoard" should {
    "make (un)labeled boards with letters" in {
      val board = Rules.startingBoard
      val tb = new TextBoard with LetterRepr

      tb.mkUnlabeled(board) must_==
        "r n b q k b n r\n" +
        "p p p p p p p p\n" +
        ". . . . . . . .\n" +
        ". . . . . . . .\n" +
        ". . . . . . . .\n" +
        ". . . . . . . .\n" +
        "P P P P P P P P\n" +
        "R N B Q K B N R\n"

      tb.mkLabeled(board) must_==
        "r n b q k b n r  8\n" +
        "p p p p p p p p  7\n" +
        ". . . . . . . .  6\n" +
        ". . . . . . . .  5\n" +
        ". . . . . . . .  4\n" +
        ". . . . . . . .  3\n" +
        "P P P P P P P P  2\n" +
        "R N B Q K B N R  1\n" +
        " \n" +
        "a b c d e f g h \n"

      tb.mkUnlabeled(Board()) must_==
        ". . . . . . . .\n" +
        ". . . . . . . .\n" +
        ". . . . . . . .\n" +
        ". . . . . . . .\n" +
        ". . . . . . . .\n" +
        ". . . . . . . .\n" +
        ". . . . . . . .\n" +
        ". . . . . . . .\n"

      tb.mkLabeled(Board()) must_==
        ". . . . . . . .  8\n" +
        ". . . . . . . .  7\n" +
        ". . . . . . . .  6\n" +
        ". . . . . . . .  5\n" +
        ". . . . . . . .  4\n" +
        ". . . . . . . .  3\n" +
        ". . . . . . . .  2\n" +
        ". . . . . . . .  1\n" +
        " \n" +
        "a b c d e f g h \n"
    }

    "make (un)labeled boards with figurines" in {
      val board = Rules.startingBoard
      val tb = new TextBoard with FigurineRepr

      tb.mkUnlabeled(board) must_==
        "♜ ♞ ♝ ♛ ♚ ♝ ♞ ♜\n" +
        "♟ ♟ ♟ ♟ ♟ ♟ ♟ ♟\n" +
        "· · · · · · · ·\n" +
        "· · · · · · · ·\n" +
        "· · · · · · · ·\n" +
        "· · · · · · · ·\n" +
        "♙ ♙ ♙ ♙ ♙ ♙ ♙ ♙\n" +
        "♖ ♘ ♗ ♕ ♔ ♗ ♘ ♖\n"

      tb.mkLabeled(board) must_==
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

      tb.mkUnlabeled(Board()) must_==
        "· · · · · · · ·\n" +
        "· · · · · · · ·\n" +
        "· · · · · · · ·\n" +
        "· · · · · · · ·\n" +
        "· · · · · · · ·\n" +
        "· · · · · · · ·\n" +
        "· · · · · · · ·\n" +
        "· · · · · · · ·\n"

      tb.mkLabeled(Board()) must_==
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

    "make (un)labeled boards as MediaWiki diagram" in {
      val board = Rules.startingBoard
      val tb = new TextBoard with WikiRepr

      tb.mkUnlabeled(board) must_==
        "|rd|nd|bd|qd|kd|bd|nd|rd|=\n" +
        "|pd|pd|pd|pd|pd|pd|pd|pd|=\n" +
        "|  |  |  |  |  |  |  |  |=\n" +
        "|  |  |  |  |  |  |  |  |=\n" +
        "|  |  |  |  |  |  |  |  |=\n" +
        "|  |  |  |  |  |  |  |  |=\n" +
        "|pl|pl|pl|pl|pl|pl|pl|pl|=\n" +
        "|rl|nl|bl|ql|kl|bl|nl|rl|=\n"

      tb.mkLabeled(board) must_==
        "8 |rd|nd|bd|qd|kd|bd|nd|rd|=\n" +
        "7 |pd|pd|pd|pd|pd|pd|pd|pd|=\n" +
        "6 |  |  |  |  |  |  |  |  |=\n" +
        "5 |  |  |  |  |  |  |  |  |=\n" +
        "4 |  |  |  |  |  |  |  |  |=\n" +
        "3 |  |  |  |  |  |  |  |  |=\n" +
        "2 |pl|pl|pl|pl|pl|pl|pl|pl|=\n" +
        "1 |rl|nl|bl|ql|kl|bl|nl|rl|=\n" +
        "   a  b  c  d  e  f  g  h \n"

      tb.mkUnlabeled(Board()) must_==
        "|  |  |  |  |  |  |  |  |=\n" +
        "|  |  |  |  |  |  |  |  |=\n" +
        "|  |  |  |  |  |  |  |  |=\n" +
        "|  |  |  |  |  |  |  |  |=\n" +
        "|  |  |  |  |  |  |  |  |=\n" +
        "|  |  |  |  |  |  |  |  |=\n" +
        "|  |  |  |  |  |  |  |  |=\n" +
        "|  |  |  |  |  |  |  |  |=\n"

      tb.mkLabeled(Board()) must_==
        "8 |  |  |  |  |  |  |  |  |=\n" +
        "7 |  |  |  |  |  |  |  |  |=\n" +
        "6 |  |  |  |  |  |  |  |  |=\n" +
        "5 |  |  |  |  |  |  |  |  |=\n" +
        "4 |  |  |  |  |  |  |  |  |=\n" +
        "3 |  |  |  |  |  |  |  |  |=\n" +
        "2 |  |  |  |  |  |  |  |  |=\n" +
        "1 |  |  |  |  |  |  |  |  |=\n" +
        "   a  b  c  d  e  f  g  h \n"
    }
  }
}

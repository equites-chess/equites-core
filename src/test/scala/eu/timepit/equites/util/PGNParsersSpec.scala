// Equites, a simple chess interface
// Copyright © 2013 Frank S. Thomas <f.thomas@gmx.de>
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

import org.specs2.mutable._
import org.specs2.matcher.ParserMatchers

class PGNParsersSpec extends Specification with ParserMatchers {
  val parsers = PGNParsers
  import parsers._

  "string" should {
    "succed on the empty string" in {
      string must succeedOn("\"\"").withResult("")
    }
    "succed on simple strings" in {
      string must succeedOn("\"hello\"").withResult("hello")
      string must succeedOn("\"hello world\"").withResult("hello world")
    }
    "succeed on escaped quotes" in {
      string must succeedOn(""""hello \" world"""")
        .withResult("hello \" world")
      string must succeedOn(""""hello \"\" world"""")
        .withResult("hello \"\" world")
    }
    "succeed on escaped backslashes" in {
      string must succeedOn(""""hello \\ world"""")
      string must succeedOn(""""hello \\ \\ world"""")
    }
    "fail on unquoted strings" in {
      string must failOn("hello")
      string must failOn("\"hello")
      string must failOn("hello\"")
    }
    "fail on unescaped quotes" in {
      string must failOn(""""hello " world"""")
      string must failOn(""""hello "" world"""")
    }
  }

  "blockComment" should {
    "succeed on valid input" in {
      blockComment must succeedOn("{no comment}")
      blockComment must succeedOn("{}")
      blockComment must succeedOn("{{}")
    }
    "fail on unbalanced parens" in {
      blockComment must failOn("{unbalanced} parens}")
    }
    "fail on missing paren" in {
      blockComment must failOn("{missing paren")
    }
  }

  "lineComment" should {
    "succeed on valid input" in {
      lineComment must succeedOn("; no comment")
      lineComment must succeedOn(";")
    }
  }

  "tagPair" should {
    "succeed on valid input" in {
      val result = ("name", "value")
      tagPair must succeedOn("""[name"value"]""").withResult(result)
      tagPair must succeedOn("""[name "value"]""").withResult(result)
      tagPair must succeedOn("""[ name "value"]""").withResult(result)
      tagPair must succeedOn("""[ name "value" ]""").withResult(result)
    }
    "fail on invalid input" in {
      tagPair must failOn("""[name "value"""")
      tagPair must failOn("""name "value"]""")
      tagPair must failOn("""[name ["value"]""")
    }
  }

  "tagSection" should {
    "succeed on valid input" in {
      val section =
        """|[Event "F/S Return Match"]
           |[Site "Belgrade, Serbia Yugoslavia|JUG"]
           |[Date "1992.11.04"]
           |[Round "29"]
           |[White "Fischer, Robert J."]
           |[Black "Spassky, Boris V."]
           |[Result "1/2-1/2"]""".stripMargin
      val result = List(
        ("Event", "F/S Return Match"),
        ("Site", "Belgrade, Serbia Yugoslavia|JUG"),
        ("Date", "1992.11.04"),
        ("Round", "29"),
        ("White", "Fischer, Robert J."),
        ("Black", "Spassky, Boris V."),
        ("Result", "1/2-1/2"))
      tagSection must succeedOn(section).withResult(result)
    }
    "succeed on valid input with trailing comments" in {
      val section =
        """|[name1 "value1"] ; no comment
           |[name2 "value2"] { comment1 } { comment2 }
           |[name3 "value3"]""".stripMargin
      val result = List(
        ("name1", "value1"),
        ("name2", "value2"),
        ("name3", "value3"))
      tagSection must succeedOn(section).withResult(result)
    }
  }

  "moveNumberIndicator" should {
    "succeed on numbers with on or three periods" in {
      moveNumberIndicator must succeedOn("23.").withResult((23, White))
      moveNumberIndicator must succeedOn("42...").withResult((42, Black))
    }
    "fail on invalid input" in {
      moveNumberIndicator must failOn("1")
      moveNumberIndicator must failOn("2..")
      moveNumberIndicator must failOn("a3.")
      moveNumberIndicator must failOn("4....")
    }
  }

  "sanMove" should {
    "succeed on valid input" in {
      sanMove must succeedOn("d5")
      sanMove must succeedOn("Nf3")
      sanMove must succeedOn("dxc4")
      sanMove must succeedOn("Qc8+")
      sanMove must succeedOn("e8=Q#")
      sanMove must succeedOn("exf3")
      sanMove must succeedOn("Qa6xb7#")
      sanMove must succeedOn("fxg1=Q+")
      sanMove must succeedOn("O-O")
      sanMove must succeedOn("O-O-O")
    }
  }

  "moveAnnotation" should {
    "succeed on all valid possibilities" in {
      moveAnnotation must succeedOn("!").withResult(1)
      moveAnnotation must succeedOn("?").withResult(2)
      moveAnnotation must succeedOn("!!").withResult(3)
      moveAnnotation must succeedOn("??").withResult(4)
      moveAnnotation must succeedOn("!?").withResult(5)
      moveAnnotation must succeedOn("?!").withResult(6)
    }
  }

  "numericAnnotationGlyph" should {
    "succeed on valid input" in {
      numericAnnotationGlyph must succeedOn("$1").withResult(1)
      numericAnnotationGlyph must succeedOn("$123").withResult(123)
    }
  }

  "terminationMarker" should {
    "succeed on all possible termination markers" in {
      terminationMarker must succeedOn("1-0")
      terminationMarker must succeedOn("0-1")
      terminationMarker must succeedOn("1/2-1/2")
      terminationMarker must succeedOn("*")
    }
  }

  "moveTextSeq" should {
    "succeed on valid input" in {
      val text = "1. e4 e5 2. Nf3 Nc6 3. Bb5 " +
                 "{This opening is called the Ruy Lopez.} 3... a6"
      val result = List(
        (1, White), "e4", "e5",
        (2, White), "Nf3", "Nc6",
        (3, White), "Bb5", Comment("This opening is called the Ruy Lopez."),
        (3, Black), "a6")

      moveTextSeq must succeedOn(text).withResult(result)
    }
  }

  "moveText" should {
    "succeed on valid input" in {
      val text = """
        1. e4 e5 2. Nf3 Nc6 3. Bb5 {This opening is called the Ruy Lopez.}
        3... a6 4. Ba4 Nf6 5. O-O Be7 6. Re1 b5 7. Bb3 d6 8. c3 O-O 9. h3 Nb8
        10. d4 Nbd7 11. c4 c6 12. cxb5 axb5 13. Nc3 Bb7 14. Bg5 b4 15. Nb1 h6
        16. Bh4 c5 17. dxe5 Nxe4 18. Bxe7 Qxe7 19. exd6 Qf6 20. Nbd2 Nxd6
        21. Nc4 Nxc4 22. Bxc4 Nb6 23. Ne5 Rae8 24. Bxf7+ Rxf7 25. Nxf7 Rxe1+
        26. Qxe1 Kxf7 27. Qe3 Qg5 28. Qxg5 hxg5 29. b3 Ke6 30. a3 Kd6
        31. axb4 cxb4 32. Ra5 Nd5 33. f3 Bc8 34. Kf2 Bf5 35. Ra7 g6
        36. Ra6+ Kc5 37. Ke1 Nf4 38. g3 Nxh3 39. Kd2 Kb5 40. Rd6 Kc5
        41. Ra6 Nf2 42. g4 Bd3 43. Re6 1/2-1/2"""
      moveText must succeedOn(text)
    }
  }
}

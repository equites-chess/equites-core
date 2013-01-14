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
      sanMove must succeedOn("f4exf3")
      sanMove must succeedOn("Qa6xb7#")
      sanMove must succeedOn("fxg1=Q+")
      sanMove must succeedOn("O-O")
      sanMove must succeedOn("O-O-O")
    }
  }

  "moveAnnotation" should {
    "succeed on all valid possibilities" in {
      moveAnnotation must succeedOn("!")
      moveAnnotation must succeedOn("?")
      moveAnnotation must succeedOn("!!")
      moveAnnotation must succeedOn("??")
      moveAnnotation must succeedOn("!?")
      moveAnnotation must succeedOn("?!")
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
}

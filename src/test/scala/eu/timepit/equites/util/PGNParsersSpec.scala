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


  "moveNumberIndicator" should {
    "succeed on" in {
      moveNumberIndicator must succeedOn("23.")
      moveNumberIndicator must succeedOn("42...")
    }

    "fail on" in {
      moveNumberIndicator must failOn("1")
      moveNumberIndicator must failOn("2..")
      moveNumberIndicator must failOn("a3.")
      moveNumberIndicator must failOn("4....")
    }
  }

  "blockComment" should {
    "succeed on" in {
      blockComment must succeedOn("{no comment}")
      blockComment must succeedOn("{}")
      blockComment must succeedOn("{{}")
    }

    "fail on" in {
      blockComment must failOn("{unbalanced} parens}")
      blockComment must failOn("{missing paren")
    }
  }

  "lineComment" should {
    "succeed on" in {
      lineComment must succeedOn("; no comment")
      lineComment must succeedOn(";")
    }

    "fail on" in {
      lineComment must failOn("; comment\n")
      lineComment must failOn("; 123\n next line")
    }
  }

  "moveAnnotation" should {
    "succeed on" in {
      moveAnnotation must succeedOn("!")
      moveAnnotation must succeedOn("?")
      moveAnnotation must succeedOn("!!")
      moveAnnotation must succeedOn("??")
      moveAnnotation must succeedOn("!?")
      moveAnnotation must succeedOn("?!")
    }
  }
/*
  "numericAnnotationGlyph" should {
    "succeed on" in {
      numericAnnotationGlyph must succeedOn("$123").withResult("$", 123)
    }
  }
*/

  "terminationMarker" should {
    "succeed on" in {
      terminationMarker must succeedOn("1-0")
      terminationMarker must succeedOn("0-1")
      terminationMarker must succeedOn("1/2-1/2")
      terminationMarker must succeedOn("*")
    }
  }
}

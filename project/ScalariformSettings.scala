import sbt._
import Keys._

import com.typesafe.sbt.SbtScalariform._
import scalariform.formatter.preferences._

object ScalariformSettings {
  lazy val ourScalariformSettings = defaultScalariformSettings ++ Seq(
    // format main sources on compile
    compileInputs in (Compile, compile) <<=
      (compileInputs in (Compile, compile)) dependsOn (ScalariformKeys.format in Compile),

    ScalariformKeys.preferences := FormattingPreferences()
      .setPreference(AlignParameters, true)
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(DoubleIndentClassDeclaration, true)
  )
}

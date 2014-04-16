import com.typesafe.sbt.SbtScalariform._
import scalariform.formatter.preferences._

object ScalariformSettings {
  lazy val ourScalariformSettings = scalariformSettings ++ Seq(
    ScalariformKeys.preferences := FormattingPreferences()
      .setPreference(AlignParameters, true)
      .setPreference(AlignSingleLineCaseStatements, true)
      .setPreference(DoubleIndentClassDeclaration, true)
  )
}

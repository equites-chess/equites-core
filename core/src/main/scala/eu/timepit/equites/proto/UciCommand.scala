package eu.timepit.equites
package proto

sealed trait UciCommand {
  def asString: String
}

case class UciDebug(bool: Boolean) extends UciCommand {
  def asString: String = "debug " + util.toStringOnOff(bool)
}

case object UciInit extends UciCommand {
  def asString: String = "uci"
}

case object UciIsReady extends UciCommand {
  def asString: String = "isready"
}

case object UciNewGame extends UciCommand {
  def asString: String = "ucinewgame"
}

case object UciPosition extends UciCommand {
  def asString: String = "position startpos"
}

case object UciGo extends UciCommand {
  def asString: String = "go movetime 1000"
}

case object UciStop extends UciCommand {
  def asString: String = "stop"
}

case object UciQuit extends UciCommand {
  def asString: String = "quit"
}

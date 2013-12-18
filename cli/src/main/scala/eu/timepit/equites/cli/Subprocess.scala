package eu.timepit.equites
package cli

import java.io._
import java.lang.ProcessBuilder
import scalaz.concurrent.Task
import scalaz.stream._

object Subprocess {
  def popen(args: String*): Process[Task, Subprocess] = {
    io.resource(Task.delay(new Subprocess(args: _*)))(
        sub => Task.delay(sub.close))(sub => Task.delay(sub)).once
  }
}

class Subprocess(args: String*) {
  private[this] val proc = new ProcessBuilder(args: _*).start

  private[this] val procIn = proc.getOutputStream
  private[this] val procOut = proc.getInputStream
  private[this] val procErr = proc.getErrorStream

  val input: Sink[Task, Array[Byte]] = inputFrom(procIn)
  val output: Process[Task, String] = outputFrom(procOut)
  val error: Process[Task, String] = outputFrom(procErr)

  def close: Int = {
    procIn.close
    procOut.close
    procErr.close
    proc.waitFor
  }

  private[this] def inputFrom(out: OutputStream) =
    io.channel {
      (bytes: Array[Byte]) => Task.delay {
        out.write(bytes)
        out.flush
      }
    }

  private[this] def outputFrom(in: InputStream) = {
    val streamReader = new InputStreamReader(in)
    val bufferedReader = new BufferedReader(streamReader)

    val delayUnit = Task.delay(())
    val readLine = Task.delay {
      Option(bufferedReader.readLine).getOrElse(throw Process.End)
    }

    io.resource(delayUnit)(_ => delayUnit)(_ => readLine)
  }
}

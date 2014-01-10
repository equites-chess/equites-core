package eu.timepit.equites
package cli

import java.io.{ InputStream, OutputStream }
import java.lang.ProcessBuilder
import scala.io.{ Codec, Source }
import scalaz.concurrent.Task
import scalaz.stream._

// how do we handle the return value of proc.waitFor?
// what should happen if reading or writing throws an exception
// - which streams should be closed; should the whole process be terminated?
// if an exception crosses popen, the process should be closed

object Subprocess {
  def popen(args: String*)(implicit codec: Codec): Process[Task, Subprocess] = {

    // TODO: io.resource().once is not so nice
    io.resource(Task.delay(new Subprocess(args: _*)))(
        sub => Task.delay(sub.close))(sub => Task.delay(sub)).once

  }
}

class Subprocess private (args: String*)(implicit codec: Codec) {
  private[this] val proc = new ProcessBuilder(args: _*).start
  private[this] val procIn = proc.getOutputStream
  private[this] val procOut = proc.getInputStream
  private[this] val procErr = proc.getErrorStream

  val input: Sink[Task, String] = sinkFrom(procIn)
  val output: Process[Task, String] = sourceFrom(procOut)
  val error: Process[Task, String] = sourceFrom(procErr)

  private def close: Int = {
    procIn.close
    procOut.close
    procErr.close
    proc.waitFor
  }

  private[this] def sinkFrom(os: OutputStream): Sink[Task,String] =
    io.channel {
      (s: String) => Task.delay {
        os.write(s.getBytes(codec.charSet))
        os.flush
      }
    }

  private[this] def sourceFrom(is: InputStream): Process[Task,String] = {
    val lines = Source.fromInputStream(is).getLines
    val nextLine = Task.delay { if (lines.hasNext) lines.next else throw Process.End }
    Process.repeatEval(nextLine)
  }
}

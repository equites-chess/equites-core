package eu.timepit.equites
package cli

import java.io.{ InputStream, OutputStream }
import java.lang.ProcessBuilder

import scala.io.{ Codec, Source }
import scalaz.concurrent.Task
import scalaz.stream._
import Process._

// naming Sysprocess, Popen, ExtProcess, childprocess
// how do we handle the return value of proc.waitFor?
// what should happen if reading or writing throws an exception
// - which streams should be closed; should the whole process be terminated?
// if an exception crosses popen, the process should be closed

case class Subprocess[R, W](
  input: Sink[Task, W],
  output: Process[Task, R],
  error: Process[Task, R])

object Subprocess {
  def popen(args: String*)(implicit codec: Codec): Process[Task, Subprocess[String, String]] = {
    io.resource(Task.delay(new ProcessBuilder(args: _*).start))(
      proc => Task.now(close(proc))) {
        proc =>
          {
            val in = sink(proc.getOutputStream)
            val out = source(proc.getInputStream)
            val err = source(proc.getErrorStream)
            Task.now(Subprocess(in, out, err))
          }
      }.once
  }

  private def mkSubprocess = ???

  private def sink(os: OutputStream)(implicit codec: Codec): Sink[Task, String] =
    io.channel {
      (s: String) =>
        Task.delay {
          os.write(s.getBytes(codec.charSet))
          os.flush()
        }
    }

  private def source(is: InputStream)(implicit codec: Codec): Process[Task, String] = {
    val lines = Source.fromInputStream(is)(codec).getLines()
    val nextLine = Task.delay { if (lines.hasNext) lines.next() else throw Cause.Terminated(Cause.End) }
    Process.repeatEval(nextLine)
  }

  private def close(proc: java.lang.Process): Int = {
    proc.getOutputStream.close()
    proc.getInputStream.close()
    proc.getErrorStream.close()
    proc.waitFor
  }
}

package eu.timepit.equites
package cli

import scalaz.concurrent.Task
import scalaz.stream._

object Subprocess {
  def popen(args: String*): Process[Task, Subprocess] = {
    Task.delay(new Subprocess(args: _*))
    ???
  }
}

class Subprocess(args: String*) {
  val input: Sink[Task, Array[Byte]] = inputFrom(procIn)

  def close: Int = {
    procIn.close
    procOut.close
    procErr.close
    proc.waitFor
  }

  private[this] def inputFrom(out: java.io.OutputStream) =
    io.channel {
      (bytes: Array[Byte]) => Task.delay {
        out.write(bytes)
        out.flush
      }
    }

  private[this] val proc = new java.lang.ProcessBuilder(args: _*).start

  private[this] val procIn = proc.getOutputStream
  private[this] val procOut = proc.getInputStream
  private[this] val procErr = proc.getErrorStream
}




object ProgramProcesses {

  def system(args: String*) = {
    val proc = new java.lang.ProcessBuilder(args: _*).start
    val procIn = proc.getOutputStream()
    val procOut = proc.getInputStream()

    def write(out: java.io.OutputStream) = {
      io.channel {
        (bytes: Array[Byte]) => Task.delay {
          //println(new String(bytes))
          out.write(bytes)
          out.flush
        }
      }
    }

    def read(in: java.io.InputStream): Process[Task, String] = {
      val streamReader = new java.io.InputStreamReader(in)
      val bufferedReader = new java.io.BufferedReader(streamReader)

      io.resource {
        Task.delay {
          (streamReader, bufferedReader)
        }
      } {
        case (_ , br) => Task.delay { println("release InputStream") }
      } {
        case (sr , br) => {
          Task.delay {
            val line = br.readLine
            if (line != null) {
              //println(line)
              line
            } else throw Process.End
          }
        }
      }
    }

    (proc, write(procIn), read(procOut))
  }
}

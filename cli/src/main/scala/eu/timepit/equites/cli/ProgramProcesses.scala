package eu.timepit.equites.cli

import scalaz.concurrent.Task
import scalaz.stream._

object ProgramProcesses {

  def system(args: String*) = {
    val proc = new java.lang.ProcessBuilder(args: _*).start
    val procIn = proc.getOutputStream()
    val procOut = proc.getInputStream()

    def write(out: java.io.OutputStream) = {
      io.channel {
        (bytes: Array[Byte]) => Task.delay {
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
        case (_ , br) => Task.delay { () }
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

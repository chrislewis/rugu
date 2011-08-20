package com.novus.rugu

import com.jcraft.jsch._
import scala.util.control.Exception.allCatch

class Ssh(user: String, password: String, host: String, port: Int = 22, knownHosts: Option[String] = None) {
  
  val jsch = new JSch()
  knownHosts.foreach(jsch.setKnownHosts)
  // jsch.addIdentity("/path/to/private_key")
  
  def apply[I, O](c: Command[I, O])(implicit evp: StreamProcessor[I]) =
    remote(c.command, evp).fold(Left(_), x => allCatch.either(c(x)))
  
  def authorize(s: Session) = {
    s.setPassword(password)
    s
  }
  
  def remote[I](command: String, p: StreamProcessor[I]): Either[Throwable, I] = {
    // Get an authorized session.
    val session = authorize(jsch.getSession(user, host, port))
    session.connect() // boom
    // Prepare a channel for invoking shell commands.
    val channel = session.openChannel("exec").asInstanceOf[ChannelExec]
    channel.setCommand(command)
    channel.setInputStream(null)
    channel.setErrStream(System.err)
    channel.connect() // boom
    // Consume its input and cleanup.
    val procd = p(channel.getInputStream() /* boom */ )
    
    // TODO channel.getExitStatus
    val close = () => { channel.disconnect(); session.disconnect() }
    //close() // boom x2
    procd.fold(
      f => null,//Right(() => { val output = f(); close(); output }),
      output => { close(); Right(output) })
  }
    
}

object IO {
  type Resource = { def close(): Unit }
  def apply[A, R <: Resource](r: R)(op: R => A): Either[Throwable, A] =
    allCatch.andFinally(r.close()).either(op(r))
}

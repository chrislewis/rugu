package com.novus.rugu

import java.io.{
  BufferedReader,
  InputStream,
  InputStreamReader
}

import scala.io.Source

/** A type class for transforming the result stream of a command. */
trait StreamProcessor[O] extends (InputStream => O) {
  def apply(in: InputStream): O
}

object SP {
  def sp[I](p: InputStream => I) = new StreamProcessor[Either[I, I]] {
    def apply(in: InputStream) = Right(p(in))
  }
  
  def unmanaged[I](p: InputStream => I) = new StreamProcessor[Either[I, I]] {
    def apply(in: InputStream) = Left(p(in))
  }
  
  //implicit val AsBufferedReader = sp(in => new BufferedReader(new InputStreamReader(in)))
  
  /*implicit val AsSource         = sp(in => Source.fromInputStream(in))
  implicit val AsListString     = sp(in => AsSource(in).getLines.toList)
  implicit val AsUnit           = sp(_ => ())
  // TODO this is stupid.
  implicit val AsInt            = sp(in => AsListString(in).headOption.map(_ toInt))
  implicit val AsString         = sp(in => AsSource(in).mkString)*/
  
  implicit val AsListString     = sp(in => Source.fromInputStream(in).getLines.toList)
  implicit val AsInputStream    = unmanaged(in => () => in)
}

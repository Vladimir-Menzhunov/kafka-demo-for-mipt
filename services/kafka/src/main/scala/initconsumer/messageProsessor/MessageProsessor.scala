package initconsumer.messageProsessor

import initconsumer.commonDomain.Answer
import zio.{Promise, Task, ZIO}

trait MessageProsessor[M] {
  def handle(a: M, initMarker: Promise[Nothing, Unit]): Task[Unit]
}

object MessageProsessor {
  def handle[M](a: M, initMarker: Promise[Nothing, Unit]): ZIO[MessageProsessor[M], Throwable, Unit] =
    ZIO.serviceWithZIO[MessageProsessor[M]](_.handle(a, initMarker))
}

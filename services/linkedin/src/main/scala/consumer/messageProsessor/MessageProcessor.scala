package consumer.messageProsessor

import domain.domain.{Answer, Resume}
import producer.LinkedinProducer
import zio.{Promise, URIO, ZIO}

trait MessageProcessor {
  def handle(
      r: Resume,
      initMarker: Promise[Nothing, Unit]
  ): URIO[LinkedinProducer, Unit]
}

object MessageProcessor {
  def handle(
      r: Resume,
      initMarker: Promise[Nothing, Unit]
  ): URIO[MessageProcessor with LinkedinProducer, Unit] =
    ZIO.serviceWithZIO[MessageProcessor](_.handle(r, initMarker))
}

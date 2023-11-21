package consumer.messageProsessor

import domain.domain.{Answer, Resume}
import producer.GoogleProducer
import zio.{Promise, URIO, ZIO}

trait MessageProcessor {
  def handle(
      r: Resume,
      initMarker: Promise[Nothing, Unit]
  ): URIO[GoogleProducer, Unit]
}

object MessageProcessor {
  def handle(
      r: Resume,
      initMarker: Promise[Nothing, Unit]
  ): URIO[MessageProcessor with GoogleProducer, Unit] =
    ZIO.serviceWithZIO[MessageProcessor](_.handle(r, initMarker))
}

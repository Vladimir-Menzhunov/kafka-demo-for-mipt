package consumer.messageProsessor

import domain.domain.{Answer, Resume}
import producer.TinkoffProducer
import zio.{Promise, URIO, ZIO}

trait MessageProcessor {
  def handle(
      r: Resume,
      initMarker: Promise[Nothing, Unit]
  ): URIO[TinkoffProducer, Unit]
}

object MessageProcessor {
  def handle(
      r: Resume,
      initMarker: Promise[Nothing, Unit]
  ): URIO[MessageProcessor with TinkoffProducer, Unit] =
    ZIO.serviceWithZIO[MessageProcessor](_.handle(r, initMarker))
}

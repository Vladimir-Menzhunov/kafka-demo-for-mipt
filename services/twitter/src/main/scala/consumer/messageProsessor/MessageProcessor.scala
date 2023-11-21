package consumer.messageProsessor

import domain.domain.{Answer, Resume}
import producer.TwitterProducer
import zio.{Promise, URIO, ZIO}

trait MessageProcessor {
  def handle(
      r: Resume,
      initMarker: Promise[Nothing, Unit]
  ): URIO[TwitterProducer, Unit]
}

object MessageProcessor {
  def handle(
      r: Resume,
      initMarker: Promise[Nothing, Unit]
  ): URIO[MessageProcessor with TwitterProducer, Unit] =
    ZIO.serviceWithZIO[MessageProcessor](_.handle(r, initMarker))
}

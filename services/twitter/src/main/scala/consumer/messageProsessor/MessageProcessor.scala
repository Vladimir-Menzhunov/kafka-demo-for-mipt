package consumer.messageProsessor

import domain.Domain.Resume
import initconsumer.helper.Domain.Event
import producer.TwitterProducer
import zio.{URIO, ZIO}

trait MessageProcessor {
  def handle(
      event: Event[Resume]
  ): URIO[TwitterProducer, Unit]
}

object MessageProcessor {
  def handle(
      event: Event[Resume]
  ): URIO[MessageProcessor with TwitterProducer, Unit] =
    ZIO.serviceWithZIO[MessageProcessor](_.handle(event))
}

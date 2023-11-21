package consumer.messageProsessor

import domain.Domain.Resume
import initconsumer.helper.Domain.Event
import producer.TinkoffProducer
import zio.{URIO, ZIO}

trait MessageProcessor {
  def handle(
      event: Event[Resume]
  ): URIO[TinkoffProducer, Unit]
}

object MessageProcessor {
  def handle(
      event: Event[Resume]
  ): URIO[MessageProcessor with TinkoffProducer, Unit] =
    ZIO.serviceWithZIO[MessageProcessor](_.handle(event))
}

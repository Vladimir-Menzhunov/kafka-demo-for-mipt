package tinkoff.consumer.messageProsessor

import initconsumer.helper.Domain.Event
import tinkoff.domain.Domain.Resume
import tinkoff.producer.TinkoffProducer
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

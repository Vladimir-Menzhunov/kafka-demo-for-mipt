package consumer.messageProsessor

import domain.domain.{Answer, Resume}
import initconsumer.helper.domain.Event
import producer.TinkoffProducer
import zio.{Promise, URIO, ZIO}

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

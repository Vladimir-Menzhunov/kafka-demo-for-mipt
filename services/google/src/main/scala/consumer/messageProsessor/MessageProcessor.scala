package consumer.messageProsessor

import domain.Domain.Resume
import initconsumer.helper.Domain.Event
import producer.GoogleProducer
import zio.{URIO, ZIO}

trait MessageProcessor {
  def handle(
      event: Event[Resume]
  ): URIO[GoogleProducer, Unit]
}

object MessageProcessor {
  def handle(
      event: Event[Resume]
  ): URIO[MessageProcessor with GoogleProducer, Unit] =
    ZIO.serviceWithZIO[MessageProcessor](_.handle(event))
}

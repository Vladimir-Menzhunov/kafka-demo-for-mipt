package google.consumer.messageProsessor

import google.domain.Domain.Resume
import google.producer.GoogleProducer
import initconsumer.helper.Domain.Event
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

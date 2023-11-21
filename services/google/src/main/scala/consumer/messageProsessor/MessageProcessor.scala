package consumer.messageProsessor

import domain.domain.{Answer, Resume}
import initconsumer.helper.domain.Event
import producer.GoogleProducer
import zio.{Promise, URIO, ZIO}

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

package consumer.messageProsessor

import domain.domain.{Answer, Resume}
import initconsumer.helper.domain.Event
import producer.TwitterProducer
import zio.{Promise, URIO, ZIO}

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

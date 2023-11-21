package consumer.messageProsessor

import domain.domain.{Answer, Resume}
import initconsumer.helper.domain.Event
import producer.LinkedinProducer
import zio.{Promise, URIO, ZIO}

trait MessageProcessor {
  def handle(r: Event[Resume]): URIO[LinkedinProducer, Unit]
}

object MessageProcessor {
  def handle(r: Event[Resume]): URIO[MessageProcessor with LinkedinProducer, Unit] =
    ZIO.serviceWithZIO[MessageProcessor](_.handle(r))
}

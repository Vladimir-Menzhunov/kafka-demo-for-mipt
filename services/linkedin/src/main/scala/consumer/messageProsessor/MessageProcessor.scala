package consumer.messageProsessor

import domain.Domain.Resume
import initconsumer.helper.Domain.Event
import producer.LinkedinProducer
import zio.{URIO, ZIO}

trait MessageProcessor {
  def handle(r: Event[Resume]): URIO[LinkedinProducer, Unit]
}

object MessageProcessor {
  def handle(r: Event[Resume]): URIO[MessageProcessor with LinkedinProducer, Unit] =
    ZIO.serviceWithZIO[MessageProcessor](_.handle(r))
}

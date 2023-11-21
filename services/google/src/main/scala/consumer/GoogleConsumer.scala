package consumer

import consumer.messageProsessor.MessageProcessor
import producer.GoogleProducer
import zio.{Fiber, URIO, ZIO}

trait GoogleConsumer {
  def run: URIO[MessageProcessor with GoogleProducer, Fiber.Runtime[Throwable, Unit]]
}

object GoogleConsumer {
  def run: URIO[MessageProcessor with GoogleProducer with GoogleConsumer, Fiber.Runtime[Throwable, Unit]] =
    ZIO.serviceWithZIO[GoogleConsumer](_.run)
}

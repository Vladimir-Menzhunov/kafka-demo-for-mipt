package consumer

import consumer.messageProsessor.MessageProcessor
import producer.TwitterProducer
import zio.{Fiber, UIO, URIO, ZIO}

trait TwitterConsumer {
  def run: URIO[MessageProcessor with TwitterProducer, Fiber.Runtime[Throwable, Unit]]
}

object TwitterConsumer {
  def run: URIO[MessageProcessor with TwitterProducer with TwitterConsumer, Fiber.Runtime[Throwable, Unit]] =
    ZIO.serviceWithZIO[TwitterConsumer](_.run)
}

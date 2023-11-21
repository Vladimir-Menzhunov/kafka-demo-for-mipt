package consumer

import consumer.messageProsessor.MessageProcessor
import producer.TinkoffProducer
import zio.{Fiber, UIO, URIO, ZIO}

trait TinkoffConsumer {
  def run: URIO[MessageProcessor with TinkoffProducer, Fiber.Runtime[Throwable, Unit]]
}

object TinkoffConsumer {
  def run: URIO[MessageProcessor with TinkoffProducer with TinkoffConsumer, Fiber.Runtime[Throwable, Unit]] =
    ZIO.serviceWithZIO[TinkoffConsumer](_.run)
}

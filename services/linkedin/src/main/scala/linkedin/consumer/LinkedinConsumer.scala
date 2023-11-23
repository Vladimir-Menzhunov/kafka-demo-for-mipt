package linkedin.consumer

import linkedin.consumer.messageProsessor.MessageProcessor
import linkedin.producer.LinkedinProducer
import zio.{Fiber, URIO, ZIO}

trait LinkedinConsumer {
  def run: URIO[MessageProcessor with LinkedinProducer, Fiber.Runtime[Throwable, Unit]]
}

object LinkedinConsumer {
  def run: URIO[MessageProcessor with LinkedinProducer with LinkedinConsumer, Fiber.Runtime[Throwable, Unit]] =
    ZIO.serviceWithZIO[LinkedinConsumer](_.run)
}

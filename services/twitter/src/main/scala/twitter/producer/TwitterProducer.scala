package twitter.producer

import twitter.domain.Domain.Answer
import zio.{RIO, ZIO}

trait TwitterProducer {
  def send(message: Answer, keyPartition: String): RIO[TwitterProducer, Unit]
}

object TwitterProducer {
  def send(
      message: Answer,
      keyPartition: String
  ): ZIO[TwitterProducer, Throwable, Unit] =
    ZIO.serviceWithZIO[TwitterProducer](_.send(message, keyPartition))
}

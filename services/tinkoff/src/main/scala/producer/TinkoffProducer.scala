package producer

import domain.Domain.Answer
import zio.{RIO, ZIO}

trait TinkoffProducer {
  def send(message: Answer, keyPartition: String): RIO[TinkoffProducer, Unit]
}

object TinkoffProducer {
  def send(
      message: Answer,
      keyPartition: String
  ): ZIO[TinkoffProducer, Throwable, Unit] =
    ZIO.serviceWithZIO[TinkoffProducer](_.send(message, keyPartition))
}

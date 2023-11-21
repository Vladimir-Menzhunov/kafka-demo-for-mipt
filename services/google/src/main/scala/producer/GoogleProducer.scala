package producer

import domain.Domain.Answer
import zio.{RIO, ZIO}

trait GoogleProducer {
  def send(message: Answer, keyPartition: String): RIO[GoogleProducer, Unit]
}

object GoogleProducer {
  def send(
      message: Answer,
      keyPartition: String
  ): ZIO[GoogleProducer, Throwable, Unit] =
    ZIO.serviceWithZIO[GoogleProducer](_.send(message, keyPartition))
}

package linkedin.producer

import linkedin.domain.Domain.Answer
import zio.{RIO, ZIO}

trait LinkedinProducer {
  def send(message: Answer, keyPartition: String): RIO[LinkedinProducer, Unit]
}

object LinkedinProducer {
  def send(
      message: Answer,
      keyPartition: String
  ): ZIO[LinkedinProducer, Throwable, Unit] =
    ZIO.serviceWithZIO[LinkedinProducer](_.send(message, keyPartition))
}

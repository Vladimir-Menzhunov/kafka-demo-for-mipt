package pashaservice.producer

import pashaservice.domain.Domain.Resume
import zio.{RIO, ZIO}

trait PashaProducer {
  def send(message: Resume, keyPartition: String): RIO[PashaProducer, Unit]
}

object PashaProducer {
  def send(
      message: Resume,
      keyPartition: String
  ): ZIO[PashaProducer, Throwable, Unit] =
    ZIO.serviceWithZIO[PashaProducer](_.send(message, keyPartition))
}

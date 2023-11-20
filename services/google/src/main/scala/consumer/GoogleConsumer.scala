package consumer

import zio.{Fiber, UIO, ZIO}

trait GoogleConsumer {
  def run: UIO[Fiber.Runtime[Throwable, Unit]]
}

object GoogleConsumer {
  def run: ZIO[GoogleConsumer, Nothing, Fiber.Runtime[Throwable, Unit]] =
    ZIO.serviceWithZIO[GoogleConsumer](_.run)
}

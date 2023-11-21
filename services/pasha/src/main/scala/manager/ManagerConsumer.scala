package manager

import state.StateService
import zio.{Fiber, URIO, ZIO}

trait ManagerConsumer {
  def run: URIO[StateService with ManagerConsumer, Fiber.Runtime[Throwable, Unit]]
}

object ManagerConsumer {
  def run: URIO[ManagerConsumer with StateService, Fiber.Runtime[Throwable, Unit]] =
    ZIO.serviceWithZIO[ManagerConsumer](_.run)
}

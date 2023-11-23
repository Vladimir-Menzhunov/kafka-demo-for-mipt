package pashaservice.pasha

import pashaservice.domain.Domain.serviceName
import pashaservice.manager.{ManagerConsumer, ManagerConsumerImpl}
import pashaservice.pasha.api.HttpRoutes
import pashaservice.producer.PashaProducerImpl
import pashaservice.state.StateServiceImpl
import zio.http.Server
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault, http}

object PashaApp extends ZIOAppDefault {

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    for {
      _ <- ZIO.logInfo(s"Start $serviceName service")
      server <-
        ManagerConsumer.run
          .zipParRight(
            zio.http.Server
              .serve(HttpRoutes.app)
          )
          .provide(
            Server.live,
            serverConfigLive,
            Scope.default,
            ManagerConsumerImpl.live,
            StateServiceImpl.live,
            PashaProducerImpl.live,
          )
    } yield server
  }

  private val serverConfigLive =
    zio.http.ServerConfig.live {
      http.ServerConfig.default
        .binding("localhost", 9005)
    }
}

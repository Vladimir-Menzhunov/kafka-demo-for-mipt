package linkedin.linkedinapp

import linkedin.consumer.messageProsessor.MessageProcessorImpl
import linkedin.consumer.{LinkedinConsumer, LinkedinConsumerImpl}
import linkedin.domain.Domain.serviceName
import linkedin.linkedinapp.api.HttpRoutes
import linkedin.producer.LinkedinProducerImpl
import zio.http.Server
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault, http}

object LinkedinApp extends ZIOAppDefault {
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    for {
      _ <- ZIO.logInfo(s"Start $serviceName service")
      server <-
        LinkedinConsumer.run
          .zipParRight(
            zio.http.Server
              .serve(HttpRoutes.app)
          ).provide(
            Server.live,
            serverConfigLive,
            Scope.default,
            LinkedinConsumerImpl.live,
            MessageProcessorImpl.live,
            LinkedinProducerImpl.live
          )
    } yield server
  }

  private val serverConfigLive =
    zio.http.ServerConfig.live {
      http.ServerConfig.default
        .binding("localhost", 9002)
    }
}

package linkedinapp

import consumer.messageProsessor.MessageProcessorImpl
import consumer.{LinkedinConsumer, LinkedinConsumerImpl}
import domain.domain.{port, serviceName}
import linkedinapp.api.HttpRoutes
import producer.LinkedinProducerImpl
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
        .binding("localhost", port)
    }
}

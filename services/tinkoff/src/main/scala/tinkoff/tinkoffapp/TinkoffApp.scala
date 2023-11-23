package tinkoff.tinkoffapp

import tinkoff.consumer.messageProsessor.MessageProcessorImpl
import tinkoff.consumer.{TinkoffConsumer, TinkoffConsumerImpl}
import tinkoff.domain.Domain.serviceName
import tinkoff.producer.TinkoffProducerImpl
import tinkoff.tinkoffapp.api.HttpRoutes
import zio.http.Server
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault, http}

object TinkoffApp extends ZIOAppDefault {
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    for {
      _ <- ZIO.logInfo(s"Start $serviceName service")
      server <-
        TinkoffConsumer.run
          .zipParRight(
            zio.http.Server
              .serve(HttpRoutes.app)
          ).provide(
            Server.live,
            serverConfigLive,
            Scope.default,
            TinkoffConsumerImpl.live,
            MessageProcessorImpl.live,
            TinkoffProducerImpl.live
          )
    } yield server
  }

  private val serverConfigLive =
    zio.http.ServerConfig.live {
      http.ServerConfig.default
        .binding("localhost", 9003)
    }
}

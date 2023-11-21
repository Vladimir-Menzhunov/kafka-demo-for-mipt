package twitterapp

import consumer.messageProsessor.MessageProcessorImpl
import consumer.{TwitterConsumer, TwitterConsumerImpl}
import domain.domain.{port, serviceName}
import producer.TwitterProducerImpl
import twitterapp.api.HttpRoutes
import zio.http.Server
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault, http}

object TwitterApp extends ZIOAppDefault {
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    for {
      _ <- ZIO.logInfo(s"Start $serviceName service")
      server <-
        TwitterConsumer.run
          .zipParRight(
            zio.http.Server
              .serve(HttpRoutes.app)
          ).provide(
            Server.live,
            serverConfigLive,
            Scope.default,
            TwitterConsumerImpl.live,
            MessageProcessorImpl.live,
            TwitterProducerImpl.live
          )
    } yield server
  }

  private val serverConfigLive =
    zio.http.ServerConfig.live {
      http.ServerConfig.default
        .binding("localhost", port)
    }
}

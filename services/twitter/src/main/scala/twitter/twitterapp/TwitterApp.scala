package twitter.twitterapp

import twitter.consumer.messageProsessor.MessageProcessorImpl
import twitter.consumer.{TwitterConsumer, TwitterConsumerImpl}
import twitter.domain.Domain.serviceName
import twitter.producer.TwitterProducerImpl
import twitter.twitterapp.api.HttpRoutes
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
        .binding("localhost", 9004)
    }
}

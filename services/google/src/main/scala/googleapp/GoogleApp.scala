package googleapp

import consumer.messageProsessor.MessageProcessorImpl
import consumer.{GoogleConsumer, GoogleConsumerImpl}
import googleapp.api.HttpRoutes
import producer.GoogleProducerImpl
import zio.http.Server
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault, http}

object GoogleApp extends ZIOAppDefault {
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = {
    for {
      _ <- ZIO.logInfo("Start GoogleApp service")
      server <-
        GoogleConsumer.run
          .zipParRight(
            zio.http.Server
              .serve(HttpRoutes.app)
          ).provide(
            Server.live,
            serverConfigLive,
            Scope.default,
            GoogleConsumerImpl.live,
            MessageProcessorImpl.live,
            GoogleProducerImpl.live
          )
    } yield server
  }

  private val serverConfigLive =
    zio.http.ServerConfig.live {
      http.ServerConfig.default
        .binding("localhost", 9001)
    }
}

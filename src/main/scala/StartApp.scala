import googleapp.GoogleApp
import linkedinapp.LinkedinApp
import pasha.PashaApp
import tinkoffapp.TinkoffApp
import twitterapp.TwitterApp
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

object StartApp extends ZIOAppDefault {
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    for {
      _ <- GoogleApp.run.forkDaemon
      _ <- LinkedinApp.run.forkDaemon
      _ <- TinkoffApp.run.forkDaemon
      _ <- TwitterApp.run.forkDaemon
      _ <- PashaApp.run
    } yield ()
}

package linkedinapp.api

import domain.Domain.serviceName
import zio.ZIO
import zio.http._
import zio.http.model.Method

object HttpRoutes {
  val app: HttpApp[
    Any,
    Response
  ] =
    Http.collectZIO[Request] { case Method.GET -> !! / "hello" =>
      ZIO.succeed(Response.text(s"Hello $serviceName service service"))
    }
}

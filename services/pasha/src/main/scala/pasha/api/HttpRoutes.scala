package pasha.api

import domain.Domain.{Answer, Resume, serviceName}
import io.circe.parser
import io.circe.syntax.EncoderOps
import state.StateService
import zio.ZIO
import zio.http._
import zio.http.model.Method

import scala.collection.mutable

object HttpRoutes {

  val app: HttpApp[StateService, Response] =
    Http.collectZIO[Request] {
      case Method.GET -> !! / "hello" =>
        ZIO.succeed(Response.text(s"Hello $serviceName service service"))

      case req @ Method.POST -> !! / "create" / "resume" =>
        val response = for {
          resume <- req.body.asString.flatMap(b =>
            ZIO.fromEither(parser.parse(b).flatMap(_.as[Resume]))
          )
          _ <- StateService.addResume(resume)
        } yield Response.text(s"Резюме успешно создано $resume")

        response.orElseFail(Response.text(s"Не удалось сохранить резюме"))

      case Method.GET -> !! / "get" / "resumes" =>
        StateService.getAllResumes.map(resumes =>
          Response.json(resumes.asJson.toString())
        )

      case Method.GET -> !! / "get" / "answers" =>
        StateService.getAllAnswers.map(answer =>
          Response.json(answer.asJson.toString())
        )

      case req @ Method.POST -> !! / "send" / "resume" =>
        val response = for {
          numResume <- ZIO.fromOption(
            req.url.queryParams.get("number").flatMap(_.headOption.map(_.toInt))
          )
          resume <- StateService.getResume(numResume)

        } yield Response.text("Ok")

        response.orElseFail(Response.text("not ok"))
    }
}

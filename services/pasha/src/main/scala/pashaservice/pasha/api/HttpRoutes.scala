package pashaservice.pasha.api

import io.circe.parser
import io.circe.syntax.EncoderOps
import pashaservice.domain.Domain.{RequestResume, serviceName, toResume}
import pashaservice.producer.PashaProducer
import pashaservice.state.StateService
import zio.ZIO
import zio.http._
import zio.http.model.Method

object HttpRoutes {

  val app: HttpApp[PashaProducer with StateService, Response] =
    Http.collectZIO[Request] {
      case Method.GET -> !! / "hello" =>
        ZIO.succeed(Response.text(s"Hello $serviceName service service"))

      case req @ Method.POST -> !! / "create" / "resume" =>
        val response = for {
          requestResume <- req.body.asString.flatMap(b =>
            ZIO.fromEither(parser.parse(b).flatMap(_.as[RequestResume]))
          )
          _ <- StateService.addResume(toResume(requestResume))
        } yield Response.text(s"Резюме успешно создано $requestResume")

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
          resumeOpt <- StateService.getResume(numResume)
          resume <- ZIO.fromOption(resumeOpt)
          _ <- PashaProducer.send(resume, resume.keyPartition)
        } yield Response.text("Резюме успешно отправлено!")

        response.orElseFail(Response.text("Не удалось отправить резюме!"))
    }
}

package consumer.messageProsessor

import domain.domain.{Answer, Resume, serviceName}
import producer.GoogleProducer
import zio.{Promise, UIO, ULayer, URIO, ZIO, ZLayer}

class MessageProcessorImpl(
    companyName: String
) extends MessageProcessor {

  override def handle(
      r: Resume,
      initMarker: Promise[Nothing, Unit]
  ): ZIO[GoogleProducer, Nothing, Unit] =
    for {
      isDone <- initMarker.isDone
      _ <- initMarker.succeed(()).unless(isDone)
      isReadyScheduleInterview <- checkExperiences(r)
      answer <- createAnswer(isReadyScheduleInterview)
      _ <- GoogleProducer.send(answer, r.keyPartition).orElse(ZIO.unit)
    } yield ()

  private def checkExperiences(r: Resume): UIO[Boolean] = {
    if (r.workExperienceYears > 3)
      ZIO.succeed(true)
    else
      ZIO.succeed(false)
  }

  private def createAnswer(
      isReadyScheduleInterview: Boolean
  ): UIO[Answer] = {
    val message =
      if (isReadyScheduleInterview)
        "Мы готовы позвать вас на собеседование"
      else
        "К сожалению, мы не можем вас пригласить на собеседование"
    ZIO.succeed(Answer(isReadyScheduleInterview, message, companyName))
  }
}

object MessageProcessorImpl {
  val live: ULayer[MessageProcessor] = ZLayer.succeed(new MessageProcessorImpl(serviceName))
}

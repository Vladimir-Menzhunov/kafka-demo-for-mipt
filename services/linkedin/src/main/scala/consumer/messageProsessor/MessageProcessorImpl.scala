package consumer.messageProsessor

import domain.Domain.{Answer, Resume, serviceName}
import initconsumer.helper.Domain.Event
import producer.LinkedinProducer
import zio.{UIO, ULayer, ZIO, ZLayer}

class MessageProcessorImpl(
    companyName: String
) extends MessageProcessor {

  override def handle(
      r: Event[Resume]
  ): ZIO[LinkedinProducer, Nothing, Unit] =
    for {
      isReadyScheduleInterview <- checkExperiences(r)
      answer <- createAnswer(isReadyScheduleInterview)
      _ <- LinkedinProducer.send(answer, r.keyPartition).orElse(ZIO.unit)
    } yield ()

  private def checkExperiences(event: Event[Resume]): UIO[Boolean] = {
    if (event.message.workExperienceYears > 3)
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
  val live: ULayer[MessageProcessor] =
    ZLayer.succeed(new MessageProcessorImpl(serviceName))
}

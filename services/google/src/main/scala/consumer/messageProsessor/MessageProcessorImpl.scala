package consumer.messageProsessor

import domain.domain.{Answer, Resume, serviceName}
import initconsumer.helper.domain.Event
import producer.GoogleProducer
import zio.{UIO, ULayer, ZIO, ZLayer}

class MessageProcessorImpl(
    companyName: String
) extends MessageProcessor {

  override def handle(
      event: Event[Resume]
  ): ZIO[GoogleProducer, Nothing, Unit] =
    for {
      isReadyScheduleInterview <- checkExperiences(event)
      answer <- createAnswer(isReadyScheduleInterview)
      _ <- GoogleProducer.send(answer, event.keyPartition).orElse(ZIO.unit)
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
  val live: ULayer[MessageProcessor] = ZLayer.succeed(new MessageProcessorImpl(serviceName))
}

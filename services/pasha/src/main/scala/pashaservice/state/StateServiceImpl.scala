package pashaservice.state

import pashaservice.domain.Domain.{Answer, AnswerResponse, Resume, ResumeResponse}
import zio.{Task, UIO, ULayer, ZIO, ZLayer}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.Try

class StateServiceImpl extends StateService {
  private val resumes = mutable.ArrayBuffer[Resume]()
  private val answers = mutable.ArrayBuffer[Answer]()

  override def getResume(id: Int): UIO[Option[Resume]] =
    ZIO.succeed(Try(resumes(id)).toOption)

  override def getAllResumes: UIO[ArrayBuffer[ResumeResponse]] =
    ZIO.succeed(
      resumes.zipWithIndex.map { case (resume, number) =>
        ResumeResponse(resume, number)
      }
    )

  override def getAllAnswers: UIO[ArrayBuffer[AnswerResponse]] =
    ZIO.succeed(
      answers.zipWithIndex.map { case (answer, number) =>
        AnswerResponse(answer, number)
      }
    )

  override def addResume(resume: Resume): Task[Unit] =
    ZIO.attempt(resumes.addOne(resume))

  override def addAnswer(answer: Answer): Task[Unit] =
    ZIO.attempt(answers.addOne(answer))
}

object StateServiceImpl {
  val live: ULayer[StateService] = ZLayer.succeed(new StateServiceImpl)
}

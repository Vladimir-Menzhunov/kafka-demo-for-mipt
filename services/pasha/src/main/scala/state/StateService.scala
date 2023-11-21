package state

import domain.Domain
import domain.Domain.{Answer, AnswerResponse, Resume, ResumeResponse}
import zio._

import scala.collection.mutable.ArrayBuffer

trait StateService {
  def getResume(id: Int): UIO[Option[Domain.Resume]]
  def getAllResumes: UIO[ArrayBuffer[ResumeResponse]]
  def getAllAnswers: UIO[ArrayBuffer[AnswerResponse]]

  def addResume(resume: Resume): Task[Unit]
  def addAnswer(answer: Answer): Task[Unit]
}

object StateService {
  def getResume(id: Int): URIO[StateService, Option[Domain.Resume]] =
    ZIO.serviceWithZIO[StateService](_.getResume(id))

  def getAllResumes: URIO[StateService, ArrayBuffer[ResumeResponse]] =
    ZIO.serviceWithZIO[StateService](_.getAllResumes)

  def getAllAnswers: URIO[StateService, ArrayBuffer[AnswerResponse]] =
    ZIO.serviceWithZIO[StateService](_.getAllAnswers)

  def addResume(resume: Resume): RIO[StateService, Unit] =
    ZIO.serviceWithZIO[StateService](_.addResume(resume))

  def addAnswer(answer: Answer): RIO[StateService, Unit] =
    ZIO.serviceWithZIO[StateService](_.addAnswer(answer))
}

package domain

import io.circe.generic.JsonCodec

object Domain {
  val topicAnswer = "topic.answer"
  val topicResume = "topic.resume"
  val serviceName = "pasha"
  val port = 9005

  @JsonCodec
  case class Answer(isPositive: Boolean, message: String, company: String)

  case class AnswerWithKeyPartion(answer: Answer, keyPartition: String)

  @JsonCodec
  case class Resume(
      keyPartition: String,
      otherInfo: String,
      workExperienceYears: Int
  )

  @JsonCodec
  case class ResumeResponse(
      resume: Resume,
      number: Int
  )

  @JsonCodec
  case class AnswerResponse(
      answer: Answer,
      number: Int
  )
}

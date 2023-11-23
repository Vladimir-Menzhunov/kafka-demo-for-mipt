package pashaservice.domain

import io.circe.generic.JsonCodec

object Domain {
  val topicAnswer = "topic.answer"
  val topicResume = "topic.resume"
  val serviceName = "pasha"

  @JsonCodec
  case class Answer(isPositive: Boolean, message: String, company: String)

  @JsonCodec
  case class RequestResume(
      otherInfo: String,
      workExperienceYears: Int
  )

  @JsonCodec
  case class Resume(
      keyPartition: String,
      otherInfo: String,
      workExperienceYears: Int
  )

  def toResume(r: RequestResume): Resume =
    Resume(
      keyPartition = serviceName,
      otherInfo = r.otherInfo,
      workExperienceYears = r.workExperienceYears
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

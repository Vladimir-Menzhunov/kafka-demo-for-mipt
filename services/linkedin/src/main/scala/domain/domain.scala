package domain

import io.circe.generic.JsonCodec

object domain {
  val topicAnswer = "topic.answer"
  val topicResume = "topic.resume"
  val serviceName = "linkedin"
  val port = 9002

  @JsonCodec
  case class Answer(isPositive: Boolean, message: String, company: String)

  @JsonCodec
  case class Resume(
      keyPartition: String,
      otherInfo: String,
      workExperienceYears: Int
  )
}

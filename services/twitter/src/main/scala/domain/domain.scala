package domain

import io.circe.generic.JsonCodec

object domain {
  val topicAnswer = "topic.answer"
  val topicResume = "topic.resume"
  val serviceName = "twitter"
  val port = 9004

  @JsonCodec
  case class Answer(isPositive: Boolean, message: String, company: String)

  @JsonCodec
  case class Resume(
      keyPartition: String,
      otherInfo: String,
      workExperienceYears: Int
  )
}

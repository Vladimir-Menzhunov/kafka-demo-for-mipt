package twitter.domain

import io.circe.generic.JsonCodec

object Domain {
  val topicAnswer = "topic.answer"
  val topicResume = "topic.resume"
  val serviceName = "twitter"

  @JsonCodec
  case class Answer(isPositive: Boolean, message: String, company: String)

  @JsonCodec
  case class Resume(
      keyPartition: String,
      otherInfo: String,
      workExperienceYears: Int
  )
}

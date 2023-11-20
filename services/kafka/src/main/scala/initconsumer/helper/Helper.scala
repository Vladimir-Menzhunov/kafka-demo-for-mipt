package initconsumer.helper

import zio.{Scope, ZIO}
import zio.kafka.consumer.{Consumer, ConsumerSettings}

object Helper {
  val topicAnswer = "topic.answer"
  val topicResume = "topic.resume"

  def createConsumer(groupId: String): ZIO[Scope, Throwable, Consumer] =
    Consumer.make(consumerSettings(groupId))

  private def consumerSettings(groupId: String): ConsumerSettings = {
    ConsumerSettings(properties = Map("enable.auto.commit" -> "true"))
      .withBootstrapServers(List("localhost:9092", "localhost:9093"))
      .withGroupId(groupId)
  }
}

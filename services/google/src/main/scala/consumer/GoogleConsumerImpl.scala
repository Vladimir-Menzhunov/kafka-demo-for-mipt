package consumer

import initconsumer.InitializableConsumer
import initconsumer.commonDomain.Answer
import initconsumer.helper.Helper.createConsumer
import initconsumer.messageProsessor.MessageProsessor
import zio.{Promise, Task, ZLayer}
import zio.kafka.consumer.Consumer
import zio.kafka.serde.Deserializer

import scala.util.Try

class GoogleConsumerImpl(
    consumer: Consumer,
    topic: String,
    process: MessageProsessor,
    streamName: String,
    deserializer: Deserializer[Any, Try[Answer]]
) extends InitializableConsumer[Answer](
      consumer,
      topic,
      process,
      streamName,
      deserializer
    )

object GoogleConsumerImpl {
  val serviceName = "google"

  val live = ZLayer.fromZIO {
    for {
      consumer <- createConsumer(serviceName)
    } yield ()
  }
}

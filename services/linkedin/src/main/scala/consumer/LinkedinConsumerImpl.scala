package consumer

import consumer.LinkedinConsumerImpl.ProcessMessageType
import consumer.messageProsessor.MessageProcessor
import domain.domain._
import initconsumer.InitializableConsumer
import initconsumer.helper.Helper.createConsumer
import initconsumer.helper.domain.Event
import io.circe.parser.parse
import producer.LinkedinProducer
import zio.kafka.consumer.Consumer
import zio.kafka.serde.{Deserializer, Serde}
import zio.{Promise, RIO, Scope, ZLayer}

import scala.util.Try

class LinkedinConsumerImpl(
    consumer: Consumer,
    topic: String,
    streamName: String,
    deserializer: Deserializer[Any, Try[Resume]],
    processMessage: ProcessMessageType
) extends InitializableConsumer[Resume, MessageProcessor with LinkedinProducer](
      consumer,
      topic,
      streamName,
      deserializer,
      processMessage
    )
    with LinkedinConsumer

object LinkedinConsumerImpl {
  private val deserializer: Deserializer[Any, Try[Resume]] =
    Serde.string.map { message => parse(message).flatMap(_.as[Resume]).toTry }

  val live: ZLayer[Scope, Throwable, LinkedinConsumer] = ZLayer.fromZIO {
    for {
      consumer <- createConsumer(serviceName)
    } yield new LinkedinConsumerImpl(
      consumer,
      topicResume,
      serviceName,
      deserializer,
      MessageProcessor.handle
    )
  }

  type ProcessMessageType = Event[Resume] => RIO[MessageProcessor with LinkedinProducer, Unit]
}

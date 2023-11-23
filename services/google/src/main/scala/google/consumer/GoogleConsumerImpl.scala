package google.consumer

import google.consumer.GoogleConsumerImpl.ProcessMessageType
import google.consumer.messageProsessor.MessageProcessor
import google.domain.Domain.{Resume, serviceName, topicResume}
import google.producer.GoogleProducer
import initconsumer.CommonConsumer
import initconsumer.helper.Domain.Event
import initconsumer.helper.Helper.createConsumer
import io.circe.parser.parse
import zio.kafka.consumer.Consumer
import zio.kafka.serde.{Deserializer, Serde}
import zio.{RIO, Scope, ZLayer}

import scala.util.Try

class GoogleConsumerImpl(
    consumer: Consumer,
    topic: String,
    streamName: String,
    deserializer: Deserializer[Any, Try[Resume]],
    processMessage: ProcessMessageType
) extends CommonConsumer[Resume, MessageProcessor with GoogleProducer](
      consumer,
      topic,
      streamName,
      deserializer,
      processMessage
    )
    with GoogleConsumer

object GoogleConsumerImpl {
  private val deserializer: Deserializer[Any, Try[Resume]] =
    Serde.string.map { message => parse(message).flatMap(_.as[Resume]).toTry }

  val live: ZLayer[Scope, Throwable, GoogleConsumer] = ZLayer.fromZIO {
    for {
      consumer <- createConsumer(serviceName)
    } yield new GoogleConsumerImpl(
      consumer,
      topicResume,
      serviceName,
      deserializer,
      MessageProcessor.handle
    )
  }

  type ProcessMessageType = Event[Resume] => RIO[MessageProcessor with GoogleProducer, Unit]
}

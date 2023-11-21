package consumer

import consumer.GoogleConsumerImpl.ProcessMessageType
import consumer.messageProsessor.MessageProcessor
import domain.domain._
import initconsumer.InitializableConsumer
import initconsumer.helper.Helper.createConsumer
import io.circe.parser.parse
import producer.GoogleProducer
import zio.kafka.consumer.Consumer
import zio.kafka.serde.{Deserializer, Serde}
import zio.{Promise, RIO, Scope, ZLayer}

import scala.util.Try

class GoogleConsumerImpl(
    consumer: Consumer,
    topic: String,
    streamName: String,
    deserializer: Deserializer[Any, Try[Resume]],
    processMessage: ProcessMessageType
) extends InitializableConsumer[Resume, MessageProcessor with GoogleProducer](
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

  type ProcessMessageType = (
      Resume,
      Promise[Nothing, Unit]
  ) => RIO[MessageProcessor with GoogleProducer, Unit]
}

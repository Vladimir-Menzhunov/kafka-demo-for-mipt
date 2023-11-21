package consumer

import consumer.TwitterConsumerImpl.ProcessMessageType
import consumer.messageProsessor.MessageProcessor
import domain.Domain._
import initconsumer.InitializableConsumer
import initconsumer.helper.Domain.Event
import initconsumer.helper.Helper.createConsumer
import io.circe.parser.parse
import producer.TwitterProducer
import zio.kafka.consumer.Consumer
import zio.kafka.serde.{Deserializer, Serde}
import zio.{RIO, Scope, ZLayer}

import scala.util.Try

class TwitterConsumerImpl(
    consumer: Consumer,
    topic: String,
    streamName: String,
    deserializer: Deserializer[Any, Try[Resume]],
    processMessage: ProcessMessageType
) extends InitializableConsumer[Resume, MessageProcessor with TwitterProducer](
      consumer,
      topic,
      streamName,
      deserializer,
      processMessage
    )
    with TwitterConsumer

object TwitterConsumerImpl {
  private val deserializer: Deserializer[Any, Try[Resume]] =
    Serde.string.map { message => parse(message).flatMap(_.as[Resume]).toTry }

  val live: ZLayer[Scope, Throwable, TwitterConsumer] = ZLayer.fromZIO {
    for {
      consumer <- createConsumer(serviceName)
    } yield new TwitterConsumerImpl(
      consumer,
      topicResume,
      serviceName,
      deserializer,
      MessageProcessor.handle
    )
  }

  type ProcessMessageType = Event[Resume] => RIO[MessageProcessor with TwitterProducer, Unit]
}

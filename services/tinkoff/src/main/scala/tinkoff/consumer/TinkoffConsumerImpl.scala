package tinkoff.consumer

import initconsumer.CommonConsumer
import initconsumer.helper.Domain.Event
import initconsumer.helper.Helper.createConsumer
import io.circe.parser.parse
import tinkoff.consumer.TinkoffConsumerImpl.ProcessMessageType
import tinkoff.consumer.messageProsessor.MessageProcessor
import tinkoff.domain.Domain.{Resume, serviceName, topicResume}
import tinkoff.producer.TinkoffProducer
import zio.kafka.consumer.Consumer
import zio.kafka.serde.{Deserializer, Serde}
import zio.{RIO, Scope, ZLayer}

import scala.util.Try

class TinkoffConsumerImpl(
    consumer: Consumer,
    topic: String,
    streamName: String,
    deserializer: Deserializer[Any, Try[Resume]],
    processMessage: ProcessMessageType
) extends CommonConsumer[Resume, MessageProcessor with TinkoffProducer](
      consumer,
      topic,
      streamName,
      deserializer,
      processMessage
    )
    with TinkoffConsumer

object TinkoffConsumerImpl {
  private val deserializer: Deserializer[Any, Try[Resume]] =
    Serde.string.map { message => parse(message).flatMap(_.as[Resume]).toTry }

  val live: ZLayer[Scope, Throwable, TinkoffConsumer] = ZLayer.fromZIO {
    for {
      consumer <- createConsumer(serviceName)
    } yield new TinkoffConsumerImpl(
      consumer,
      topicResume,
      serviceName,
      deserializer,
      MessageProcessor.handle
    )
  }

  type ProcessMessageType = Event[Resume] => RIO[MessageProcessor with TinkoffProducer, Unit]
}

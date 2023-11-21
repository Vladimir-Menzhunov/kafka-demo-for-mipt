package manager
import domain.Domain.{Answer, serviceName, topicAnswer}
import initconsumer.InitializableConsumer
import initconsumer.helper.Domain.Event
import initconsumer.helper.Helper.createConsumer
import io.circe.parser.parse
import state.StateService
import zio.kafka.consumer.Consumer
import zio.kafka.serde.{Deserializer, Serde}
import zio.{RIO, Scope, ZLayer}

import scala.util.Try

class ManagerConsumerImpl(
    consumer: Consumer,
    topic: String,
    streamName: String,
    deserializer: Deserializer[Any, Try[Answer]],
    process: Event[Answer] => RIO[StateService, Unit]
) extends InitializableConsumer[Answer, StateService](
      consumer,
      topic,
      streamName,
      deserializer,
      process
    )
    with ManagerConsumer

object ManagerConsumerImpl {
  private val deserializer: Deserializer[Any, Try[Answer]] =
    Serde.string.map { message => parse(message).flatMap(_.as[Answer]).toTry }

  private def messageHandler(event: Event[Answer]): RIO[StateService, Unit] =
    StateService
      .addAnswer(event.message)
      .when(event.keyPartition == serviceName)
      .unit

  val live: ZLayer[Scope, Throwable, ManagerConsumer] = ZLayer.fromZIO {
    for {
      consumer <- createConsumer(serviceName)
    } yield new ManagerConsumerImpl(
      consumer,
      topicAnswer,
      serviceName,
      deserializer,
      messageHandler
    )
  }
}

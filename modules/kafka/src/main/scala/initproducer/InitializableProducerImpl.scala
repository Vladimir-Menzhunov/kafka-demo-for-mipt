package initproducer

import zio.kafka.producer.Producer
import zio.kafka.serde.{Serde, Serializer}
import zio.{RIO, ZIO}

abstract class InitializableProducerImpl[M, R](
    producer: Producer,
    topic: String,
    valueSerializer: Serializer[Any, M]
) {

  def send(message: M, keyPartition: String): RIO[R, Unit] = {
    ZIO.logInfo(s"The producer writes a message to the topic -> $topic") *>
      producer
        .produce(
          topic = topic,
          key = keyPartition,
          value = message,
          keySerializer = Serde.string,
          valueSerializer = valueSerializer
        )
        .mapBoth(
          error => ErrorSendingMessageKafka(topic, error),
          _ => ()
        )
        .tapError(error =>
          ZIO.logError(
            s"Producer failed to send a message ${error.getMessage} topic -> $topic"
          )
        ) *>
      ZIO.logInfo(
        s"The producer finished recording the message in the topic, topic -> $topic"
      )
  }
}

case class ErrorSendingMessageKafka(topic: String, error: Throwable)
    extends Exception(
      s"Error sending message to kafka topic: $topic, error: ${error.getMessage}"
    )

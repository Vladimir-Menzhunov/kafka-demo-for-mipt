package initconsumer

import initconsumer.helper.domain.Event
import zio._
import zio.kafka.consumer.{CommittableRecord, Consumer, Subscription}
import zio.kafka.serde.{Deserializer, Serde}
import zio.stream.{ZSink, ZStream}

import scala.util.{Failure, Success, Try}

abstract class InitializableConsumer[M, R](
    consumer: Consumer,
    topic: String,
    streamName: String,
    deserializer: Deserializer[Any, Try[M]],
    process: Event[M] => RIO[R, Unit]
) {
  def run: URIO[R, Fiber.Runtime[Throwable, Unit]] = {
    for {
      _ <- ZIO.logInfo("Starting consumer")
      fiber <- createStream
        .mapZIO { record =>
          handleMessage(record).map(r => (r, record.key))
        }
        .collect { case (Some(message), keyPartition) =>
          Event(message, keyPartition)
        }
        .run(
          ZSink.foreachChunk { chunk =>
            chunk.foldZIO(()) { (_, message) =>
              process(message).catchAll { e =>
                ZIO.logError(s"Failed to process message ${e.getMessage}")
              }
            }
          }
        )
        .fork
    } yield fiber
  }

  private def createStream
      : ZStream[Any, Throwable, CommittableRecord[String, Try[M]]] =
    ZStream
      .fromZIO(ZIO.logInfo(s"Creating stream $streamName")) *>
      consumer.plainStream(
        Subscription.topics(topic),
        Serde.string,
        deserializer
      )

  private def handleMessage(
      record: CommittableRecord[String, Try[M]]
  ): UIO[Option[M]] = {
    record.value match {
      case Failure(exception) =>
        ZIO
          .logError(
            s"Failed to deserialize message, $exception, record=$record"
          )
          .as(None)
      case Success(value) => ZIO.some(value)
    }
  }
}

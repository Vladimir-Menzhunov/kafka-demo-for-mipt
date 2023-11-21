package initconsumer

import zio.kafka.consumer.{CommittableRecord, Consumer, Subscription}
import zio.kafka.serde.{Deserializer, Serde}
import zio.stream.{ZSink, ZStream}
import zio._

import scala.util.{Failure, Success, Try}

abstract class InitializableConsumer[M, R](
    consumer: Consumer,
    topic: String,
    streamName: String,
    deserializer: Deserializer[Any, Try[M]],
    process: (M, Promise[Nothing, Unit]) => RIO[R, Unit]
) {
  def run: URIO[R, Fiber.Runtime[Throwable, Unit]] = {
    for {
      _ <- ZIO.logInfo("Starting consumer")
      initMarker <- Promise.make[Nothing, Unit]
      fiber <- createStream
        .mapZIO { record =>
          handleMessage(record)
        }
        .collect { case Some(message) =>
          message
        }
        .run(
          ZSink.foreachChunk { chunk =>
            chunk.foldZIO(()) { (_, message) =>
              process(message, initMarker).catchAll { e =>
                ZIO.logError(s"Failed to process message ${e.getMessage}")
              }
            }
          }
        )
        .fork
      _ <- initMarker.await
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

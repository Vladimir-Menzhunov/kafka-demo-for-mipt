package initconsumer

import initconsumer.messageProsessor.MessageProsessor
import zio.kafka.consumer.{CommittableRecord, Consumer, Subscription}
import zio.kafka.serde.{Deserializer, Serde}
import zio.stream.{ZSink, ZStream}
import zio.{Fiber, Promise, Task, UIO, ZIO}

import scala.util.{Failure, Success, Try}

abstract class InitializableConsumer[M](
    consumer: Consumer,
    topic: String,
    process: MessageProsessor[M],
    streamName: String,
    deserializer: Deserializer[Any, Try[M]]
) {
  def run: UIO[Fiber.Runtime[Throwable, Unit]] = {
    for {
      _ <- ZIO.logInfo("Starting consumer")
      initMarker <- Promise.make[Nothing, Unit]
      fiber <- createStream
        .mapZIO { record =>
          handleMessage(record)
        }
        .collect { case Some(message) =>
          process.handle(message, initMarker)
        }
        .run(ZSink.drain)
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

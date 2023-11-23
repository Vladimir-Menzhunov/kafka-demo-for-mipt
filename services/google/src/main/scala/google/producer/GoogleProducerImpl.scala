package google.producer

import google.domain.Domain.{Answer, topicAnswer}
import initproducer.InitializableProducerImpl
import initproducer.helper.Helper.createProducer
import io.circe.syntax.EncoderOps
import zio.{Scope, ZLayer}
import zio.kafka.producer.Producer
import zio.kafka.serde.{Serde, Serializer}

class GoogleProducerImpl(
    producer: Producer,
    topic: String,
    valueSerializer: Serializer[Any, Answer]
) extends InitializableProducerImpl[Answer, GoogleProducer](producer, topic, valueSerializer) with GoogleProducer

object GoogleProducerImpl {

  private val valueSerializer = Serde.string.contramap[Answer] { value =>
    value.asJson.noSpaces
  }

  val live: ZLayer[Scope, Throwable, GoogleProducer] = ZLayer.fromZIO {
    for {
      producer <- createProducer
    } yield new GoogleProducerImpl(producer, topicAnswer, valueSerializer)
  }
}

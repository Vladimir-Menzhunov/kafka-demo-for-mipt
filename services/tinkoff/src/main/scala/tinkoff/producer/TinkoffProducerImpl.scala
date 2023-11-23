package tinkoff.producer

import initproducer.InitializableProducerImpl
import initproducer.helper.Helper.createProducer
import io.circe.syntax.EncoderOps
import tinkoff.domain.Domain.{Answer, topicAnswer}
import zio.kafka.producer.Producer
import zio.kafka.serde.{Serde, Serializer}
import zio.{Scope, ZLayer}

class TinkoffProducerImpl(
    producer: Producer,
    topic: String,
    valueSerializer: Serializer[Any, Answer]
) extends InitializableProducerImpl[Answer, TinkoffProducer](producer, topic, valueSerializer) with TinkoffProducer

object TinkoffProducerImpl {

  private val valueSerializer = Serde.string.contramap[Answer] { value =>
    value.asJson.noSpaces
  }

  val live: ZLayer[Scope, Throwable, TinkoffProducer] = ZLayer.fromZIO {
    for {
      producer <- createProducer
    } yield new TinkoffProducerImpl(producer, topicAnswer, valueSerializer)
  }
}

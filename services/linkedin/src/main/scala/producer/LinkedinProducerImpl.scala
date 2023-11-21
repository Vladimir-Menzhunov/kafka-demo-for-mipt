package producer

import domain.domain.{Answer, topicAnswer}
import initproducer.InitializableProducerImpl
import initproducer.helper.Helper.createProducer
import io.circe.syntax.EncoderOps
import zio.{Scope, ZLayer}
import zio.kafka.producer.Producer
import zio.kafka.serde.{Serde, Serializer}

class LinkedinProducerImpl(
    producer: Producer,
    topic: String,
    valueSerializer: Serializer[Any, Answer]
) extends InitializableProducerImpl[Answer, LinkedinProducer](producer, topic, valueSerializer) with LinkedinProducer

object LinkedinProducerImpl {

  private val valueSerializer = Serde.string.contramap[Answer] { value =>
    value.asJson.noSpaces
  }

  val live: ZLayer[Scope, Throwable, LinkedinProducer] = ZLayer.fromZIO {
    for {
      producer <- createProducer
    } yield new LinkedinProducerImpl(producer, topicAnswer, valueSerializer)
  }
}
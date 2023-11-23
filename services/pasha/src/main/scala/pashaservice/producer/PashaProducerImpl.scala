package pashaservice.producer

import initproducer.InitializableProducerImpl
import initproducer.helper.Helper.createProducer
import io.circe.syntax.EncoderOps
import pashaservice.domain.Domain.{Resume, topicAnswer, topicResume}
import zio.kafka.producer.Producer
import zio.kafka.serde.{Serde, Serializer}
import zio.{Scope, ZLayer}

class PashaProducerImpl(
    producer: Producer,
    topic: String,
    valueSerializer: Serializer[Any, Resume]
) extends InitializableProducerImpl[Resume, PashaProducer](producer, topic, valueSerializer) with PashaProducer

object PashaProducerImpl {

  private val valueSerializer = Serde.string.contramap[Resume] { value =>
    value.asJson.noSpaces
  }

  val live: ZLayer[Scope, Throwable, PashaProducer] = ZLayer.fromZIO {
    for {
      producer <- createProducer
    } yield new PashaProducerImpl(producer, topicResume, valueSerializer)
  }
}

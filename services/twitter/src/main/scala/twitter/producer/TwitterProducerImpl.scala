package twitter.producer

import initproducer.InitializableProducerImpl
import initproducer.helper.Helper.createProducer
import io.circe.syntax.EncoderOps
import twitter.domain.Domain.{Answer, topicAnswer}
import zio.kafka.producer.Producer
import zio.kafka.serde.{Serde, Serializer}
import zio.{Scope, ZLayer}

class TwitterProducerImpl(
    producer: Producer,
    topic: String,
    valueSerializer: Serializer[Any, Answer]
) extends InitializableProducerImpl[Answer, TwitterProducer](producer, topic, valueSerializer) with TwitterProducer

object TwitterProducerImpl {

  private val valueSerializer = Serde.string.contramap[Answer] { value =>
    value.asJson.noSpaces
  }

  val live: ZLayer[Scope, Throwable, TwitterProducer] = ZLayer.fromZIO {
    for {
      producer <- createProducer
    } yield new TwitterProducerImpl(producer, topicAnswer, valueSerializer)
  }
}

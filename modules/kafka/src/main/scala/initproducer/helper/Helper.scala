package initproducer.helper

import zio.kafka.producer.{Producer, ProducerSettings}
import zio.{Scope, ZIO}

object Helper {
  def createProducer: ZIO[Scope, Throwable, Producer] =
    Producer.make(producerSettings)

  private def producerSettings: ProducerSettings = {
    ProducerSettings()
      .withBootstrapServers(List("localhost:9092","localhost:9093"))
      .withProperties("acks" -> "all")
  }
}

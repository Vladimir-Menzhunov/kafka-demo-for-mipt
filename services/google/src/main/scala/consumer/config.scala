package consumer

import initconsumer.config.KafkaConsumerConfig
import pureconfig.generic.auto.exportReader
import pureconfig.{ConfigReader, ConfigSource}
import zio.{ZLayer, http}

import scala.concurrent.duration.FiniteDuration

case class GoogleConsumerConfig(
    override val topic: String,
    override val servers: List[String],
    override val properties: Map[String, String]
) extends KafkaConsumerConfig

object GoogleConsumerConfig {

  private val coogleConsumerConfig: GoogleConsumerConfig = source.loadOrThrow[GoogleConsumerConfig]

  val live: ZLayer[Any, Nothing, GoogleConsumerConfig] = zio.http.ServerConfig.live {
    http
      .ServerConfig
      .default
      .binding(serviceConfig.host, serviceConfig.port)
  }
}

object GoogleConsumerConfig
    extends Configs.Builder[GoogleConsumerConfig] {
  import pureconfig.generic.semiauto._

  implicit val configReader: ConfigReader[DerivativeConsumerConfig] =
    deriveReader[DerivativeConsumerConfig]
  override val defaultNamespace = "fireg-derivatives-consumer"
}

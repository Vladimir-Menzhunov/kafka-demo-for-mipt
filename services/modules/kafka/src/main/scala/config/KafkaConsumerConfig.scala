package config

trait KafkaConsumerConfig {
  def topic: String
  def servers: List[String]
  def properties: Map[String, String]
}

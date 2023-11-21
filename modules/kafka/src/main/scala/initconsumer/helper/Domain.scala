package initconsumer.helper

object Domain {
  case class Event[M](message: M, keyPartition: String)
}

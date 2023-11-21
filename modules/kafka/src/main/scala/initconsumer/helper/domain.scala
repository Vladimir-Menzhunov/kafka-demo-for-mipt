package initconsumer.helper

object domain {
  case class Event[M](message: M, keyPartition: String)
}

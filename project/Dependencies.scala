import Libs._
import sbt._

trait Dependencies {
  def dependencies: Seq[ModuleID]
}

object Dependencies {

  object Pasha extends Dependencies {
    override def dependencies: Seq[ModuleID] = Seq(zio, pureconfig, circe).flatten
  }

  object Google extends Dependencies {
    override def dependencies: Seq[ModuleID] = Seq(zio, pureconfig, circe).flatten
  }

  object Linkedin extends Dependencies {
    override def dependencies: Seq[ModuleID] = Seq(zio, pureconfig, circe).flatten
  }

  object Tinkoff extends Dependencies {
    override def dependencies: Seq[ModuleID] = Seq(zio, pureconfig, circe).flatten
  }

  object Twitter extends Dependencies {
    override def dependencies: Seq[ModuleID] = Seq(zio, pureconfig, circe).flatten
  }

  object Kafka extends Dependencies {
    override def dependencies: Seq[ModuleID] = Seq(zio, pureconfig, circe).flatten
  }
}
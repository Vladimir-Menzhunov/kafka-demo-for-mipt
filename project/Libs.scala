import sbt._

object V {
  val zio = "2.0.13"
  val zioHttp = "0.0.5"
  val zioKafka = "2.6.0"

  val pureconfig = "0.17.4"

  val circe = "0.14.5"
}

object Libs {

  val zio: List[ModuleID] = List(
    "dev.zio" %% "zio" % V.zio,
    "dev.zio" %% "zio-http" % V.zioHttp,
    "dev.zio" %% "zio-kafka" % V.zioKafka
  )

  val pureconfig: List[ModuleID] = List(
    "com.github.pureconfig" %% "pureconfig" % V.pureconfig
  )

  val circe: List[ModuleID] = List(
    "io.circe" %% "circe-parser" % V.circe,
    "io.circe" %% "circe-core" % V.circe,
    "io.circe" %% "circe-generic" % V.circe
  )
}

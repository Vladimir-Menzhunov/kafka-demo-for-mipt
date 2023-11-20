package consumer.messageProsessor

import initconsumer.commonDomain.Answer
import initconsumer.messageProsessor.MessageProsessor
import zio.kafka.producer.Producer
import zio.{Promise, Task}

class MessageProsessorImpl(producer: Producer) extends MessageProsessor[Answer] {
  override def handle(a: Answer, initMarker: Promise[Nothing, Unit]): Task[Unit] =
   // producer.produce()

}

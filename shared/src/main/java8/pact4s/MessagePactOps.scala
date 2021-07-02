/*
 * Copyright 2021-2021 io.github.jbwheatley
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pact4s

import au.com.dius.pact.consumer.{MessagePactBuilder, PactTestExecutionContext}
import au.com.dius.pact.core.model.PactSpecVersion
import au.com.dius.pact.core.model.messaging.{Message, MessagePact}
import pact4s.MessagePactOps.{MessageOps, MessagePactBuilderOps}

import scala.jdk.CollectionConverters._
import scala.util.control.NonFatal

object MessagePactOps {
  class MessagePactBuilderOps(val builder: MessagePactBuilder) extends AnyVal {
    def withMetadata(metadata: Map[String, Any]): MessagePactBuilder = builder.withMetadata(metadata.asJava)

    def withContent[A](content: A)(implicit ev: PactDslJsonBodyEncoder[A]): MessagePactBuilder =
      builder.withContent(ev.toPactDslJsonBody(content))

    def toMessagePact: MessagePact = builder.toPact
  }

  class MessageOps(val message: Message) extends AnyVal {
    def as[A](implicit decoder: MessagePactDecoder[A]): Either[Throwable, A] = decoder.decode(message)
    def metadata: Map[String, Any]                                           = message.getMetaData.asScala.toMap
  }
}

trait MessagePactOps {
  implicit def toMessagePactBuilderOps(builder: MessagePactBuilder): MessagePactBuilderOps = new MessagePactBuilderOps(
    builder
  )

  implicit def toMessageOps(message: Message): MessageOps = new MessageOps(message)

  private[pact4s] def writeMessagePactToFile(
      pact: MessagePact,
      executionContext: PactTestExecutionContext,
      version: PactSpecVersion
  ): Either[Throwable, Unit] =
    try Right(pact.write(executionContext.getPactFolder, version))
    catch {
      case NonFatal(err) => Left(err)
    }

  sealed abstract class Pact4sMessagePactBuilder() {
    def consumer(consumer: String): MessagePactBuilder = MessagePactBuilder.consumer(consumer)
  }

  object Pact4sMessagePactBuilder {
    def apply(): Pact4sMessagePactBuilder = new Pact4sMessagePactBuilder() {}
  }
}
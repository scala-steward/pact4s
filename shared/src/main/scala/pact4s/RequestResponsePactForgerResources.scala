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

import au.com.dius.pact.consumer.{BaseMockServer, MockHttpServerKt, PactTestExecutionContext}
import au.com.dius.pact.consumer.model.MockProviderConfig
import au.com.dius.pact.core.model.{PactSpecVersion, RequestResponsePact}
import au.com.dius.pact.core.support.V4PactFeaturesException

import scala.jdk.CollectionConverters.ListHasAsScala

trait RequestResponsePactForgerResources
    extends BasePactForgerResources[RequestResponsePact]
    with RequestResponsePactOps {
  val mockProviderConfig: MockProviderConfig = MockProviderConfig.createDefault()

  private[pact4s] val server: BaseMockServer = MockHttpServerKt.mockServer(pact, mockProviderConfig)
}
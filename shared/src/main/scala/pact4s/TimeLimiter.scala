/*
 * Copyright 2021 io.github.jbwheatley
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

import com.google.common.util.concurrent.SimpleTimeLimiter

import java.util.concurrent.{Callable, Executors, TimeUnit}

private[pact4s] object TimeLimiter {
  def callWithTimeout[T](thunk: () => T, timeoutDuration: Long, timeoutUnit: TimeUnit): T = {
    val callable = new Callable[T] {
      def call(): T = thunk()
    }
    SimpleTimeLimiter
      .create(Executors.newSingleThreadExecutor())
      .callWithTimeout(callable, timeoutDuration, timeoutUnit)
  }
}

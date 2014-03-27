/* ===================================================
 * Copyright © 2013 2014 the kamon project <http://kamon.io/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ========================================================== */

import akka.actor.{Props, ActorLogging, Actor}
import kamon.trace.TraceRecorder

case object Begin

class JobSender extends Actor with ActorLogging {
  val upperCaser = context.actorOf(Props[UpperCaser], "upper-caser")
  var counter = 1;

  def receive = {
    case Begin => {
        for(_ <- 1 to 5) {
          upperCaser ! s"Hello without context"
        }

      implicit val system = context.system
        for(_ <- 1 to 5) {
          //TraceRecorder requires an implicit ActorSystem
          TraceRecorder.withNewTraceContext("jobSender") {
            upperCaser ! "Hello World with TraceContext"
          }
        }
    }
    case length: Int => {
        log.info("Length [{}]", length)
        if (counter == 10) {
          context.system.shutdown()
        }
        counter += 1
      }
    }
}


class UpperCaser extends Actor with ActorLogging {
  val lengthCalculator = context.actorOf(Props[LengthCalculator], "length-calculator")

  def receive = {
    case anyString: String => {
      log.info("Upper casing [{}]", anyString)
      lengthCalculator.forward(anyString.toUpperCase)
    }
  }
}

class LengthCalculator extends Actor with ActorLogging  {
  def receive = {
    case anyString: String =>
      log.info("Calculating the length of: [{}]", anyString)
      sender ! anyString.length
  }
}

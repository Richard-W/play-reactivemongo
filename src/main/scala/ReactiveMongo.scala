/* Copyright 2015 Richard Wiedenhöft <richard@wiedenhoeft.xyz>
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
package play.modules.reactivemongo

import play.api._
import play.api.inject._
import javax.inject._
import reactivemongo.api._
import akka.actor.ActorSystem
import scala.concurrent.duration._
import scala.concurrent.Future

trait ReactiveMongo {
  def driver: MongoDriver
  def connection: MongoConnection
  def db: DB
}

final class ReactiveMongoImpl @Inject()(
    conf: Configuration,
    actorSystem: ActorSystem,
    applicationLifecycle: ApplicationLifecycle
) extends ReactiveMongo {

  /* Get an execution context */
  import actorSystem.dispatcher

  val uri: String = conf.getString("mongo.uri").get
  val parsedURI: MongoConnection.ParsedURI = MongoConnection.parseURI(uri).get

  val driver: MongoDriver = new MongoDriver(actorSystem)
  val connection: MongoConnection = driver.connection(parsedURI)
  val db: DB = connection.db(parsedURI.db.get)

  applicationLifecycle.addStopHook { () ⇒
    connection.askClose()(10.seconds) map { _ ⇒
      driver.close
    }
  }
}

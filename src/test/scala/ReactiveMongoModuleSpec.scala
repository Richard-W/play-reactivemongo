package play.modules.reactivemongo

import org.scalatest._
import com.github.simplyscala.{ MongoEmbedDatabase, MongodProps }
import play.api.inject.guice._
import play.api.libs.concurrent.Execution.Implicits._
import reactivemongo.api._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson._
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._

class ReactiveMongoModuleSpec extends FlatSpec with Matchers with BeforeAndAfter with MongoEmbedDatabase {

  val mongoURI = "mongodb://localhost:12345/test"
  val application = new GuiceApplicationBuilder()
    .configure("mongo.uri" -> mongoURI)
    .bindings(new ReactiveMongoModule)
    .build
  val injector = application.injector

  var mongoProps: MongodProps = null

  before {
    mongoProps = mongoStart()
  }

  after {
    Await.result(application.stop, Duration.Inf)
    mongoStop(mongoProps)
  }

  "ReactiveMongoModule" should "supply a working ReactiveMongo component" in {
    val reactiveMongo = injector.instanceOf[ReactiveMongo]
    val testDoc = BSONDocument("field1" -> "value1", "field2" -> "value2")
    val collection = reactiveMongo.db.collection[BSONCollection]("test")

    val futureRetrievedDoc: Future[BSONDocument] = collection.insert(testDoc) map { lastError ⇒
      if(lastError.ok) {
        Unit
      } else {
        throw lastError
      }
    } flatMap { _ ⇒
      val cursor = collection.find(BSONDocument()).cursor[BSONDocument]
      cursor.collect[Seq]() map { seq ⇒
        seq(0)
      }
    }

    val retrievedDoc = Await.result(futureRetrievedDoc, 20.seconds)
    retrievedDoc.getAs[String]("field1").get should be ("value1")
    retrievedDoc.getAs[String]("field2").get should be ("value2")
  }
}

play-reactivemongo
==================

This module enables you to inject instances of the `ReactiveMongo` trait in play 2.4. It
really is just a thin wrapper around the reactivemongo initialization process.

To use this module in a play project you have to add the following line to your
conf/application.conf:

```
play.modules.enabled += "play.modules.reactivemongo.ReactiveMongoModule"
mongo.uri = "mongodb://localhost/somedb"
```

After this you may inject `ReactiveMongo` instances into your controllers:

```
import play.api.mvc._
import javax.inject._
import play.modules.reactivemongo.ReactiveMongo

class Application @Inject()(mongo: ReactiveMongo) extends Controller {
	/* ... */
}
```

package controllers

import java.util.concurrent.TimeUnit

import models._
import play.api.libs.json._
import play.api.mvc._

import play.api.Logger

import play.api.i18n.{MessagesApi, I18nSupport}
import javax.inject.Inject

/**
 * Created by d-takehana on 2015/09/04.
 */
class PersonService @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {
  //class PersonService extends Controller {
  import formatter.Formatter._

  def getPerson = Action { rs =>
    import scala.concurrent._
    import ExecutionContext.Implicits.global
    import scala.util.{Success, Failure}

    val person: Future[Option[Person]] = Future {
      Some(Person(24, Name("first","last"), Some("O"), Seq(1,2,3)))
    }
    val cart: Future[Option[Cart]] = Future {
      Some(Cart(99L, "iphone"))
    }
    //personに紐づくカート明細
    val f = for {Some(p) <- person; Some(c) <- cart} yield PersonCarts(p, Seq(c))
    //同期？？
//    f.onSuccess {
//      case _ =>
//    }
    Ok(Json.toJson(Await.result(f, scala.concurrent.duration.Duration(1000, TimeUnit.MILLISECONDS))))
    //Ok(Json.toJson(f.value.getOrElse("""{"status":"NG"}""")))
//    val f: Future[Seq[Int]] = Future {
//      for (i <- 1 to 10 if i == 9) yield i
//    }
//    f onComplete {
//      case Success(list) => for (num <- list) Logger.info(num.toString)
//      case Failure(t) => println("An error has occured: " + t.getMessage)
//    }
//    Ok(Json.toJson( """{"status":"OK"}"""))
  }

  def savePerson = Action(BodyParsers.parse.json) { rs =>

    Logger.info(rs.body.toString())

    Json.fromJson[Person](rs.body) match {
      case person: JsSuccess[Person] => {
        Logger.info("OK")
        Ok(Json.toJson(person.get))
      }
      case e: JsError => {
        Logger.info("NG")
        BadRequest(Json.obj("status" -> "NG", "message" -> JsError.toJson(e)))
      }
    }

    //    //これはだめだった・・・
    //    Json.fromJson[Person](rs.body) match {
    //      case JsSuccess(Person, person) => {
    //        Logger.info("OK")
    //        Ok(Json.toJson(person.get))
    //      }
    //      case JsError(e) => {
    //        Logger.info("NG")
    //        BadRequest(JsError.toJson(e))
    //      }
    //    }

    //        //playサンプルver
    //        val r = rs.body.validate[Person]
    //        r.fold(
    //          errors => {
    //            BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toFlatJson(errors)))
    //          },
    //          person => {
    //            //Place.save(place)
    //            Ok(Json.obj("status" ->"OK", "message" -> ("Place '"+person.name+"' saved.") ))
    //          }
    //        )

    //        //正解ver
    //        rs.body.validate[Person].map { p =>
    //          //register
    //          Ok(Json.toJson(p))
    //        }.recoverTotal { e =>
    //          BadRequest(JsError.toFlatJson(e))
    //        }
  }
}

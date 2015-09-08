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

  def getFutureTest = Action{ rs =>
    import scala.concurrent._
    import ExecutionContext.Implicits.global
    import scala.util.{Success, Failure}

    val f1: Future[Seq[Int]] = Future {
      for (i <- 1 to 9) yield i
    }
    val f2: Future[Seq[Int]] = Future {
      for (i <- 10 to 10) yield i
    }
    val xf = for (i <- f1; ii <- f2) yield (i, ii)
    xf onComplete {
      case Success(tpl) => for (i <- tpl._1; ii <- tpl._2) Logger.info(i + " " + ii)
      case Failure(t) => println("An error has occured: " + t.getMessage)
    }
    Ok(Json.toJson( """{"status":"OK"}"""))
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

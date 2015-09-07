package controllers

import models.Person
import play.api.libs.json._
import play.api.mvc._

import play.api.Logger

import play.api.i18n.{MessagesApi, I18nSupport}
import javax.inject.Inject

/**
 * Created by d-takehana on 2015/09/04.
 */
class PersonService @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport{
//class PersonService extends Controller {
  def savePerson = Action(BodyParsers.parse.json) { rs =>
    import formatter.Formatter._

    Logger.info(rs.body.toString())

    Json.fromJson[Person](rs.body) match {
      case person: JsSuccess[Person] => {
        Logger.info("OK")
        Ok(Json.toJson(person.get)).withHeaders("Access-Control-Allow-Origin" -> " *")
      }
      case e: JsError => {
        Logger.info("NG")
        BadRequest(Json.obj("status" ->"NG", "message" -> JsError.toJson(e))).withHeaders("Access-Control-Allow-Origin" -> " *")
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

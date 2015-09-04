package formatter

import models._

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._

/**
 * Created by d-takehana on 2015/09/04.
 */
object Formatter {
  //定義の順番が逆だとNameの初期化が行われていない状態となりNullエラーになる（コンパイル時点で警告「Reference to uninitialized value json2Name」でる）
  implicit val json2Name: Format[Name] = (
    (__ \ "first").format[String] and (__ \ "last").format[String]
    )(Name.apply _, unlift(Name.unapply))
  implicit val json2Person: Format[Person] = (
    (__ \ "age").format[Int] and (__ \ "name").format[Name]
    )(Person.apply _, unlift(Person.unapply))

}

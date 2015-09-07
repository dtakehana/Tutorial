package formatter

import models._
import play.api.data.validation.ValidationError

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.json.Reads._

/**
 * Created by d-takehana on 2015/09/04.
 */
object Formatter {
  implicit val json2Name: Format[Name] = (
    (__ \ "first").format[String](filterNot[String](ValidationError("桁数が少なすぎます", 1))(_.length < 1) keepAnd filterNot[String](ValidationError("桁数が多すぎます", 10))(_.length > 10)) and
      (__ \ "last").format[String]
    )(Name.apply _, unlift(Name.unapply))
  implicit val json2Person: Format[Person] = (
    //frameworkではminメソッドを「filterNot[N](ValidationError("error.min", m))(num.lt(_, m))(reads)」で定義（num、readsはimplicit）
    (__ \ "age").format[Int](filterNot[Int](ValidationError("数値が小さすぎます", 0))(_ < 1) andKeep filterNot[Int](ValidationError("数値が大きすぎます", 0))(_ > 50)) and
      (__ \ "name").format[Name] and
      (__ \ "bloodType").formatNullable[String] and
      (__ \ "numbers").format[Seq[Int]]
    )(Person, unlift(Person.unapply))
}
//object Formatter {
//  //定義の順番が逆だとNameの初期化が行われていない状態となりNullエラーになる（コンパイル時点で警告「Reference to uninitialized value json2Name」でる）
//  implicit val json2Name: Format[Name] = (
//    (__ \ "first").format[String](minLength[String](1) keepAnd maxLength[String](10)) and
//      (__ \ "last").format[String]
//    )(Name.apply _, unlift(Name.unapply))
//  implicit val json2Person: Format[Person] = (
//    (__ \ "age").format[Int](min[Int](0) andKeep max[Int](50)) and
//      (__ \ "name").format[Name] and
//      (__ \ "bloodType").formatNullable[String] and
//      (__ \ "numbers").format[Seq[Int]]
//    //case classだからapplyは省略できる
//    )(Person, unlift(Person.unapply))
////    )(Person.apply _, unlift(Person.unapply))
//}

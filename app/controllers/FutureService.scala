package controllers

import models.{PersonCarts, Cart, Name, Person}
import play.api.libs.json.Json
import play.api.mvc._

/**
 * Created by d-takehana on 2015/09/08.
 */
class FutureService extends Controller {
  //結果型(playのresult)をFutureでラップするので「Action.async」にする
  def getPerson = Action.async { rs =>
    import formatter.Formatter._
    import scala.concurrent._
    import ExecutionContext.Implicits.global
    import scala.util.{Success, Failure}

    val person: Future[Option[Person]] = Future {
      Some(Person(24, Name("first", "last"), Some("O"), Seq(1, 2, 3)))
    }
    val cart: Future[Option[Seq[Cart]]] = Future {
      Some(Seq(Cart(99L, "iphone")))
    }
    //personに紐づくカート明細

    val f = person.flatMap(p => cart.map(c => PersonCarts(p.get, c.get))) //=> Seq[PersonCart]
    //    val f = for {Some(p) <- person; Some(c) <- cart} yield PersonCarts(p, c) // Seq[PersonCarts]
    //    val x = person.map(p => cart.map(c => PersonCarts(p.get, c.get))) // => Seq[Seq[PersonCart]]なのでだめ
    f.map(x => Ok(Json.toJson(x)))
    Future(BadRequest("NG"))
  }
}

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

import play.api.libs.json._

/**
 * Created by d-takehana on 2015/09/04.
 */
/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class PersonServiceSpec extends Specification {

  "PersonAPI#register" should {
    "response json validation error" in new WithApplication {
      val Some(result) = route(FakeRequest(POST, "/service/saveperson",
        FakeHeaders(Seq(CONTENT_TYPE -> "application/json")),
        Json.parse( """{"typo1!!!":24, "name":{"first":"FirstName", "typo2!!!":"LastName"}}""")))
      status(result) mustEqual BAD_REQUEST
      //verupでメッセージ構成変わったかも・・・
      contentAsString(result) mustEqual
         """{"obj.age":[{"msg":["error.path.missing"],"args":[]}],"obj.name.last":[{"msg":["error.path.missing"],"args":[]}]}"""
        ///""" {"person.age":[{"msg":"error.path.missing"}],"person.name.last":[{"msg":"error.path.missing"}]}"""
    }
  }

//  "Application" should {
//
//    "send 404 on a bad request" in new WithApplication {
//      route(FakeRequest(POST, "/service/saveperson")) must beSome.which(status(_) == NOT_FOUND)
//    }
//
//    "render the index page" in new WithApplication {
//      val home = route(FakeRequest(GET, "/")).get
//
//      status(home) must equalTo(OK)
//      contentType(home) must beSome.which(_ == "text/html")
//      contentAsString(home) must contain("Your new application is ready.")
//    }
//  }
}

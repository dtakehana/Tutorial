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
        Json.parse( """{"typo1!!!":24, "name":{"first":"FirstName", "typo2!!!":"LastName"}, "bloodType":"O", "numbers":[1,2,3]}""")))
      status(result) mustEqual BAD_REQUEST
      //playのverupでメッセージ構成変わったのかも・・・
      contentAsString(result) mustEqual
        """{"status":"NG","message":{"obj.age":[{"msg":["error.path.missing"],"args":[]}],"obj.name.last":[{"msg":["error.path.missing"],"args":[]}]}}"""
      ///""" {"person.age":[{"msg":"error.path.missing"}],"person.name.last":[{"msg":"error.path.missing"}]}"""
    }

    "response json validation error max number" in new WithApplication {
      val Some(result) = route(FakeRequest(POST, "/service/saveperson",
        FakeHeaders(Seq(CONTENT_TYPE -> "application/json")),
        Json.parse( """{"age":51, "name":{"first":"FirstName", "last":"LastName"}, "bloodType":"O", "numbers":[1,2,3]}""")))
      status(result) mustEqual BAD_REQUEST
      contentAsString(result) contains "error.max"
    }

    "response json validation error maxLength string" in new WithApplication {
      val Some(result) = route(FakeRequest(POST, "/service/saveperson",
        FakeHeaders(Seq(CONTENT_TYPE -> "application/json")),
        Json.parse( """{"age":24, "name":{"first":"FirstNameLastName", "last":"LastName"}, "bloodType":"O", "numbers":[1,2,3]}""")))
      status(result) mustEqual BAD_REQUEST
      contentAsString(result) contains "error.maxLength"
    }
  }
}

import org.specs2.matcher.JsonMatchers
@RunWith(classOf[JUnitRunner])
class PersonServiceSpec2 extends Specification with JsonMatchers {

  "PersonAPI#register" should {
    "response json validation error element data" in new WithApplication {
      val Some(result) = route(FakeRequest(POST, "/service/saveperson",
        FakeHeaders(Seq(CONTENT_TYPE -> "application/json")),
        Json.parse( """{"age":51, "name":{"first":"FirstName", "last":"LastName"}, "bloodType":"O", "numbers":[1,2,3]}""")))
      status(result) mustEqual BAD_REQUEST
      contentAsString(result) must /("status" -> "NG")
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

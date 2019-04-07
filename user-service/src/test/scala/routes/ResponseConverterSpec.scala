package routes

import org.scalatest.{Matchers, WordSpec}
import routes.HTTPResponse.{Error, ErrorResponse}

class ResponseConverterSpec extends WordSpec with Matchers {

  s"HTTP response converter" should {

    "convert error" in {
      toErrorResponse("Error") shouldBe ErrorResponse(Error("Error"))
    }
  }

}

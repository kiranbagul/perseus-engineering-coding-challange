package utils

import domain.{Template, User}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}

class EmailBuilderTest extends WordSpec with Matchers with MockFactory {

  "Email template builder" should {
    "replace user.salutation token in template for female gender" in {
      val tmpl = new Template(1, "name", "{{user.salutation}}")
      val email = new EmailBuilder(tmpl).of(new User(1, "S", "F", "Female", "E1", true))
      email should be("Miss")
    }

    "replace user.salutation token in template for male gender" in {
      val tmpl = new Template(1, "name", "{{user.salutation}}")
      val email = new EmailBuilder(tmpl).of(new User(1, "S", "F", "Male", "E1", true))
      email should be("Mr")
    }

    "replace all user info tokens in template" in {
      val tmpl = new Template(1, "name", "Hi {{user.salutation}} {{user.name}}, id: {{user.identifier}}")
      val email = new EmailBuilder(tmpl).of(new User(1, "S", "F", "Female", "E1", true))
      email should be("Hi Miss F, id: 1")
    }
  }
}

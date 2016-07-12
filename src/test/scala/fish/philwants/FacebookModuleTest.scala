package fish.philwants

import fish.philwants.modules.FacebookModule
import org.scalatest.{Matchers, FlatSpec}
import TestCredentials._

class FacebookModuleTest extends FlatSpec with Matchers {
  "Facebook module" should "detect a successful login" in {
    val creds = Credentials(FACEBOOK_USERNAME, FACEBOOK_PASSWORD)
    val mod = FacebookModule
    mod.tryLogin(creds) shouldBe true

  }

  it should "detect a failed login" in {
    val creds = Credentials(BAD_USERNAME_EMAIL, BAD_PASSWORD)
    val mod = FacebookModule
    mod.tryLogin(creds) shouldBe false

  }
}

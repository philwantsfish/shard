package fish.philwants

import fish.philwants.modules.TwitterModule
import org.scalatest.{FlatSpec, Matchers}
import TestCredentials._

class TwitterModuleTest extends FlatSpec with Matchers {
  "Twitter module" should "detect a successful login" in {
    val creds = Credentials(TWITTER_USERNAME, TWITTER_PASSWORD)
    val mod = TwitterModule
    mod.tryLogin(creds) shouldBe true
  }

  it should "detect a failed login" in {
    val creds = Credentials(BAD_USERNAME_EMAIL, BAD_PASSWORD)
    val mod = TwitterModule
    mod.tryLogin(creds) shouldBe false
  }
}

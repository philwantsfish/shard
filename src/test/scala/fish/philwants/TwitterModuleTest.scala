package fish.philwants

import fish.philwants.modules.{FailedLogin, TwitterModule, SuccessfulLogin}
import org.scalatest.{FlatSpec, Matchers}
import TestCredentials._

class TwitterModuleTest extends FlatSpec with Matchers {

  "Twitter module" should "detect a successful login" in {
    val creds = Credentials(TWITTER_USERNAME, TWITTER_PASSWORD)
    val mod = TwitterModule
    val result = mod.tryLogin(creds)
    result shouldBe a [SuccessfulLogin]
  }

  it should "detect a failed login" in {
    val creds = Credentials(BAD_USERNAME_EMAIL, BAD_PASSWORD)
    val mod = TwitterModule
    val result = mod.tryLogin(creds)
    result shouldBe a [FailedLogin]
  }

}

package fish.philwants

import fish.philwants.Runner.Credentials
import fish.philwants.modules.{FailedLogin, InstagramModule, SuccessfulLogin, TwitterModule}
import org.scalatest.{Matchers, FlatSpec}
import TestCredentials._

class InstagramModuleTest extends FlatSpec with Matchers {

  "Instagram module" should "be able to detect successful login" in {
    val creds = Credentials(INSTAGRAM_USERNAME, INSTAGRAM_PASSWORD)
    val mod = InstagramModule
    val result = mod.tryLogin(creds)
    result shouldBe a [SuccessfulLogin]
  }

  it should "detect failed logins" in {
    val creds = Credentials(BAD_USERNAME, BAD_PASSWORD)
    val mod = InstagramModule
    val result = mod.tryLogin(creds)
    result shouldBe a [FailedLogin]
  }

}

package fish.philwants

import fish.philwants.modules.InstagramModule
import org.scalatest.{Matchers, FlatSpec}
import TestCredentials._

class InstagramModuleTest extends FlatSpec with Matchers {
  "Instagram module" should "be able to detect successful login" in {
    val creds = Credentials(INSTAGRAM_USERNAME, INSTAGRAM_PASSWORD)
    val mod = InstagramModule
    mod.tryLogin(creds) shouldBe true
  }

  it should "detect failed logins" in {
    val creds = Credentials(BAD_USERNAME, BAD_PASSWORD)
    val mod = InstagramModule
    mod.tryLogin(creds) shouldBe false
  }

}

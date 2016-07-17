package fish.philwants

import fish.philwants.TestCredentials._
import fish.philwants.modules.DailymotionModule
import org.scalatest.{FlatSpec, Matchers}

class DailymotionModuleTest extends FlatSpec with Matchers {
  "Dailymotion module" should "detect a successful login" in {
    val creds = Credentials(DAILYMOTION_USERNAME, DAILYMOTION_PASSWORD)
    val mod = DailymotionModule
    mod.tryLogin(creds) shouldBe true
  }

  it should "detect a failed login" in {
    val creds = Credentials(BAD_USERNAME_EMAIL, BAD_PASSWORD)
    val mod = DailymotionModule
    mod.tryLogin(creds) shouldBe false
  }
}

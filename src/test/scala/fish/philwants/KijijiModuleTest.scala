package fish.philwants

import fish.philwants.modules.{KijijiModule}
import org.scalatest.{FlatSpec, Matchers}
import TestCredentials._

class KijijiModuleTest extends FlatSpec with Matchers {
  "Kijiji module" should "detect a successful login" in {
    val creds = Credentials(KIJIJI_USERNAME, KIJIJI_PASSWORD)
    val mod = KijijiModule
    mod.tryLogin(creds) shouldBe true
  }

  it should "detect a failed login" in {
    val creds = Credentials(BAD_USERNAME_EMAIL, BAD_PASSWORD)
    val mod = KijijiModule
    mod.tryLogin(creds) shouldBe false
  }
}

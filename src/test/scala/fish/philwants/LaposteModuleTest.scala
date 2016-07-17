package fish.philwants

import fish.philwants.modules.LaposteModule
import org.scalatest.{FlatSpec, Matchers}
import TestCredentials._

class LaposteModuleTest extends FlatSpec with Matchers {
  "Laposte module" should "detect a successful login" in {
    val creds = Credentials(LAPOSTE_USERNAME, LAPOSTE_PASSWORD)
    val mod = LaposteModule
    mod.tryLogin(creds) shouldBe true
  }

  it should "detect a failed login" in {
    val creds = Credentials(BAD_USERNAME_EMAIL, BAD_PASSWORD)
    val mod = LaposteModule
    mod.tryLogin(creds) shouldBe false
  }
}

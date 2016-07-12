package fish.philwants

import fish.philwants.modules.LinkedinModule
import org.scalatest.{Matchers, FlatSpec}
import TestCredentials._

class LinkedinModuleTest extends FlatSpec with Matchers {
  "LinkedIn module" should "detect a successful login" in {
    val creds = Credentials(LINKEDIN_USERNAME, LINKEDIN_PASSWORD)
    val mod = LinkedinModule
    mod.tryLogin(creds) shouldBe true
  }

  it should "detect a failed login" in {
    val creds = Credentials(BAD_USERNAME, BAD_PASSWORD)
    val mod = LinkedinModule
    mod.tryLogin(creds) shouldBe false
  }
}

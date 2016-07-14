package fish.philwants

import fish.philwants.modules.GitHubModule
import org.scalatest.{FlatSpec, Matchers}
import TestCredentials._

class DigitalOceanModuleTest extends FlatSpec with Matchers {
  "DigitalOcean module" should "detect a successful login" in {
    val creds = Credentials(DIGITAL_OCEAN_USERNAME, DIGITAL_OCEAN_PASSWORD)
    val mod = GitHubModule
    mod.tryLogin(creds) shouldBe true
  }

  it should "detect a failed login" in {
    val creds = Credentials(BAD_USERNAME_EMAIL, BAD_PASSWORD)
    val mod = GitHubModule
    mod.tryLogin(creds) shouldBe false
  }
}

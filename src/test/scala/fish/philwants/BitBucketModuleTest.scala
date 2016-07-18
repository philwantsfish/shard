package fish.philwants

import fish.philwants.modules.{BitBucketModule, GitHubModule}
import org.scalatest.{FlatSpec, Matchers}
import TestCredentials._

class BitBucketModuleTest extends FlatSpec with Matchers {
  "GitHub module" should "detect a successful login" in {
    val creds = Credentials(BITBUCKET_USERNAME, BITBUCKET_PASSWORD)
    val mod = BitBucketModule
    mod.tryLogin(creds) shouldBe true
  }

  it should "detect a failed login" in {
    val creds = Credentials(BAD_USERNAME_EMAIL, BAD_PASSWORD)
    val mod = BitBucketModule
    mod.tryLogin(creds) shouldBe false
  }
}

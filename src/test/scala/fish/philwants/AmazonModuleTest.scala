package fish.philwants

import fish.philwants.modules.{AmazonModule, TwitterModule}
import org.scalatest.{FlatSpec, Matchers}
import TestCredentials._

class AmazonModuleTest extends FlatSpec with Matchers {
  "Amazon module" should "detect a successful login" ignore {
    val creds = Credentials(AMAZON_USERNAME, AMAZON_PASSWORD)
    val mod = AmazonModule
    mod.tryLogin(creds) shouldBe true
  }

//  it should "detect a failed login" in {
//    val creds = Credentials(BAD_USERNAME_EMAIL, BAD_PASSWORD)
//    val mod = AmazonModule
//    mod.tryLogin(creds) shouldBe false
//  }
}

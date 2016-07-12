package fish.philwants

import fish.philwants.modules.RedditModule
import org.scalatest.{FlatSpec, Matchers}
import TestCredentials._

class RedditModuleTest extends FlatSpec with Matchers {
   "The Reddit module" should "detect successful login" in {
     val creds = Credentials(REDDIT_USERNAME, REDDIT_PASSWORD)
     val mod = RedditModule
     mod.tryLogin(creds) shouldBe true
   }

   it should "detect failed login" in {
     val creds = Credentials(BAD_USERNAME, BAD_PASSWORD)
     val mod = RedditModule
     mod.tryLogin(creds) shouldBe false
   }
 }

package fish.philwants

import fish.philwants.modules.{FailedLogin, SuccessfulLogin, RedditModule}
import org.scalatest.{FlatSpec, Matchers}
import TestCredentials._

class RedditModuleTest extends FlatSpec with Matchers {
   "The Reddit module" should "detect successful login" in {
     val creds = Credentials(REDDIT_USERNAME, REDDIT_PASSWORD)
     val mod = RedditModule
     val result = mod.tryLogin(creds)
     result shouldBe a [SuccessfulLogin]
   }

   it should "detect failed login" in {
     val creds = Credentials(BAD_USERNAME, BAD_PASSWORD)
     val mod = RedditModule
     val result = mod.tryLogin(creds)
     result shouldBe a [FailedLogin]
   }
 }

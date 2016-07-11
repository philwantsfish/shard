package fish.philwants

import fish.philwants.modules.{AbstractModule, SuccessfulLogin, TwitterModule}
import org.scalatest.{FlatSpec, Matchers}


class ModuleTest extends FlatSpec with Matchers {
  def testSuccessfulLogn(mod: AbstractModule, creds: Credentials) = {
    val result = mod.tryLogin(creds)
    result shouldBe a[SuccessfulLogin]
  }
}

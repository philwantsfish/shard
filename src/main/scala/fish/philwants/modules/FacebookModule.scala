package fish.philwants.modules

import fish.philwants.Credentials
import scala.collection.JavaConversions._

object FacebookModule extends AbstractModule {
  override val moduleName: String = "Facebook"
  override val uri: String = "https://www.facebook.com"

  override def tryLogin(creds: Credentials): Boolean = {
    val resp = get(uri).execute()

    // The login parameters
    val params = Map(
      "email" -> creds.username,
      "pass" -> creds.password
    )

    // Send login request
    val loginUri = "https://www.facebook.com/login.php"
    val loginResp = post(loginUri)
      .data(params)
      .cookies(resp.cookies())
      .followRedirects(false)
      .execute()

    // Check login result
    loginResp.statusCode() == 302
  }
}

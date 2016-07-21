package fish.philwants.modules

import fish.philwants.Credentials
import org.jsoup.Connection.Response
import scala.collection.JavaConversions._
import org.jsoup.nodes.FormElement
import fish.philwants.JsoupImplicits._

object KijijiModule extends AbstractModule {
  val uri = "https://kijiji.com/"
  val moduleName = "Kijiji"

  def tryLogin(creds: Credentials): Boolean = {
    val loginUri = "https://www.kijiji.ca/t-login.html"
    val resp = get(loginUri).execute()

    // Get the form and update it with credentials
    val form = resp.selectForm("form.special.track-form-flow")
      .update("emailOrNickname", creds.username)
      .update("password", creds.password)

    // Try to login
    val loginResp = form
      .submit()
      .header("Content-Type", "application/x-www-form-urlencoded")
      .cookies(resp.cookies())
      .followRedirects(false)
      .execute()

    // Check login result
    loginResp.statusCode() == 302
  }
}

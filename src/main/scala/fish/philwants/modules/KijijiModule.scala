package fish.philwants.modules

import fish.philwants.Credentials
import org.jsoup.Connection.Response
import scala.collection.JavaConversions._
import org.jsoup.nodes.FormElement

object KijijiModule extends AbstractModule {
  val uri = "https://kijiji.com/"
  val moduleName = "Kijiji"

  def tryLogin(creds: Credentials): Boolean = {
    val loginUri = "https://www.kijiji.ca/t-login.html"
    val resp = get(loginUri).execute()

    // Parse the form the response
    val form = resp
      .parse()
      .select("form.special.track-form-flow")
      .first()
      .asInstanceOf[FormElement]

    // Update the form data to include username and password
    val usernameKey = "emailOrNickname"
    val passwordKey = "password"
    val formdata: Map[String, String] = form.formData().map { e => e.key() -> e.value() }.toMap
    val updatedFormData = formdata + (usernameKey -> creds.username) + (passwordKey -> creds.password)

    // Send login request
    val loginResp = post(loginUri)
      .header("Content-Type", "application/x-www-form-urlencoded")
      .data(updatedFormData)
      .cookies(resp.cookies())
      .followRedirects(false)
      .execute()

    // Check login result
    loginResp.statusCode() == 302
  }
}

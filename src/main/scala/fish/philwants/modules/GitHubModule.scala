package fish.philwants.modules

import fish.philwants.Credentials
import org.jsoup.Connection.Response
import scala.collection.JavaConversions._
import org.jsoup.nodes.FormElement

object GitHubModule extends AbstractModule {
  val uri = "https://github.com/"
  val moduleName = "GitHub"

  /**
   * The GitHub login process uses a hidden form parameter `authenticity_token` and requires
   * a session cookie. Visit the login page, persist the cookies, submit the form with the hidden
   * parameter
   */
  def tryLogin(creds: Credentials): Boolean = {
    val loginUri = "https://github.com/login"
    val resp = get(loginUri).execute()

    // Parse the form the response
    val form = resp
      .parse()
      .select("form")
      .first()
      .asInstanceOf[FormElement]

    // Update the form data to include username and password
    val usernameKey = "login"
    val passwordKey = "password"
    val formdata: Map[String, String] = form.formData().map { e => e.key() -> e.value() }.toMap
    val updatedFormData = formdata + (usernameKey -> creds.username) + (passwordKey -> creds.password)

    // Send login request
    val loginUri2 = "https://github.com/session"
    val loginResp = post(loginUri2)
      .header("Content-Type", "application/x-www-form-urlencoded")
      .data(updatedFormData)
      .cookies(resp.cookies())
      .followRedirects(false)
      .execute()

    // Check login result
    loginResp.statusCode() == 302
  }
}

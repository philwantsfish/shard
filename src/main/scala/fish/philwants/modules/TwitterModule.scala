package fish.philwants.modules

import fish.philwants.Credentials
import org.jsoup.Connection.Response
import org.jsoup.{Connection, Jsoup}
import scala.collection.JavaConversions._
import org.jsoup.nodes.FormElement

object TwitterModule extends AbstractModule {
  val uri = "https://twitter.com/"
  val moduleName = "Twitter"

  /**
   * The Twitter login process uses a hidden form parameter `authenticity_token` and requires
   * a session cookie. Visit the login page, persist the cookies, submit the form with the hidden
   * parameter
   */
  def tryLogin(creds: Credentials): Boolean = {
    val resp = get(uri).execute()

    // Parse the form the response
    val form: FormElement = resp
      .parse()
      .select("form.LoginForm.js-front-signin")
      .first()
      .asInstanceOf[FormElement]

    // Update the form data to include username and password
    val usernameKey = "session[username_or_email]"
    val passwordKey = "session[password]"
    val formdata: Map[String, String] = form
      .formData()
      .map { e => e.key() -> e.value() }
      .toMap
    val updatedFormData = formdata + (usernameKey -> creds.username) + (passwordKey -> creds.password)

    // Send login request
    val loginUri = "https://twitter.com/sessions"
    val loginResp = post(loginUri)
      .header("User-Agent", useragent)
      .header("Content-Type", "application/x-www-form-urlencoded")
      .data(updatedFormData)
      .cookies(resp.cookies())
      .followRedirects(false)
      .execute()

    // Check login result
    isLoginSuccessful(loginResp)
  }

  def isLoginSuccessful(resp: Response): Boolean = {
    val locationHeader = resp.header("Location")
    !locationHeader.contains("login/error?username_or_email")
  }
}

package fish.philwants.modules

import fish.philwants.Credentials
import org.jsoup.Connection.Response
import scala.collection.JavaConversions._
import org.jsoup.nodes.FormElement

object VimeoModule extends AbstractModule {
  val uri = "https://vimeo.com/"
  val moduleName = "Vimeo"

  /**
   * The GitHub login process uses a hidden form parameter `authenticity_token` and requires
   * a session cookie. Visit the login page, persist the cookies, submit the form with the hidden
   * parameter
   */
  def tryLogin(creds: Credentials): Boolean = {
    val loginUri = "https://vimeo.com/login_in?modal=new"
    val resp = get(loginUri).execute()

    // Parse the form the response
    val form = resp
      .parse()
      .getElementById("login_form")
      .asInstanceOf[FormElement]

    // Update the form data to include username and password
    val usernameKey = "email"
    val passwordKey = "password"
    val formdata: Map[String, String] = form.formData().map { e => e.key() -> e.value() }.toMap
    val updatedFormData = formdata + (usernameKey -> creds.username) + (passwordKey -> creds.password)

    // Send login request
    val loginUri2 = "https://vimeo.com/log_in?ssl=0&iframe=0&popup=0&player=0&product_id="
    val loginResp = post(loginUri2)
      .header("Content-Type", "application/x-www-form-urlencoded")
      .data(updatedFormData)
      .cookies(resp.cookies())
      .followRedirects(false)
      .execute()

    // Check login result
    loginResp.statusCode() == 200
  }
}

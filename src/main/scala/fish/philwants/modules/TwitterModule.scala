package fish.philwants.modules

import fish.philwants.Credentials
import org.jsoup.Connection.Response

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

    // Get the form and update it with credentials
    val form = resp.selectForm("form.LoginForm.js-front-signin")
      .update("session[username_or_email]", creds.username)
      .update("session[password]", creds.password)

    // Try to login
    val loginResp = form
      .submit()
      .cookies(resp.cookies())
      .header("Content-Type", "application/x-www-form-urlencoded")
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

package fish.philwants.modules

import fish.philwants.Credentials

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

    // Get the form and update it with credentials
    val form = resp.firstForm
      .update("login", creds.username)
      .update("password", creds.password)

    // Send login request
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

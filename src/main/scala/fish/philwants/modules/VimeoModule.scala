package fish.philwants.modules

import fish.philwants.Credentials

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

    // Get the form and update it with credentials
    val form = resp.getFormById("login_form")
        .update("email", creds.username)
        .update("password", creds.password)

    // Try to login
    val loginResp = form
      .submit()
      .header("Content-Type", "application/x-www-form-urlencoded")
      .cookies(resp.cookies())
      .followRedirects(false)
      .execute()

    // Check login result
    loginResp.statusCode() == 200
  }
}

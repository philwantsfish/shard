package fish.philwants.modules

import fish.philwants.Credentials
import fish.philwants.JsoupImplicits._


object LinkedinModule extends AbstractModule {
  override val moduleName: String = "LinkedIn"
  override val uri: String = "https://www.linkedin.com"

  // Given credentials return a LoginResult
  override def tryLogin(creds: Credentials): Boolean = {
    val resp = get(uri).execute()

    // Get the form and update it with credentials
    val form = resp.selectForm("form.login-form")
      .update("session_key", creds.username)
      .update("session_password", creds.password)

    // Try to login
    val loginResp = form
      .submit()
      .cookies(resp.cookies())
      .followRedirects(false)
      .execute()

    // Check login result
    loginResp.statusCode() == 302
  }
}

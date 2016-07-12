package fish.philwants.modules

import fish.philwants.Credentials
import fish.philwants.modules.FacebookModule._
import org.jsoup.nodes.FormElement
import org.jsoup.{Connection, Jsoup}
import scala.collection.JavaConversions._


object LinkedinModule extends AbstractModule {
  override val moduleName: String = "LinkedIn"
  override val uri: String = "https://www.linkedin.com"

  // Given credentials return a LoginResult
  override def tryLogin(creds: Credentials): Boolean = {
    val resp = get(uri).execute()

    // Parse the form the response
    val form: FormElement = resp
      .parse()
      .select("form.login-form")
      .first()
      .asInstanceOf[FormElement]

    // Update the form data to include username and password
    val usernameKey = "session_key"
    val passwordKey = "session_password"
    val formdata: Map[String, String] = form.formData().map { e => e.key() -> e.value() }.toMap
    val updatedFormData = formdata + (usernameKey -> creds.username) + (passwordKey -> creds.password)

    // Send login request
    val loginUri = "https://www.linkedin.com/uas/login-submit"
    val loginResp = post(loginUri)
      .data(updatedFormData)
      .cookies(resp.cookies())
      .followRedirects(false)
      .execute()

    // Check login result
    loginResp.statusCode() == 302
  }


}

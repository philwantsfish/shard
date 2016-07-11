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
  override def tryLogin(creds: Credentials): LoginResult = {
    val resp = Jsoup.connect(uri)
      .method(Connection.Method.GET)
      .header("User-Agent", useragent)
      .execute()

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
    val loginResp = Jsoup
      .connect(loginUri)
      .method(Connection.Method.POST)
      .header("User-Agent", useragent)
      .data(updatedFormData)
      .cookies(resp.cookies())
      .followRedirects(false)
      .execute()

    // Check login result
    if(loginResp.statusCode() == 302) SuccessfulLogin(creds, moduleName, uri)
    else FailedLogin(creds, moduleName, uri)
  }


}

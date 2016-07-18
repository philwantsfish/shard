package fish.philwants.modules

import fish.philwants.Credentials
import scala.collection.JavaConversions._
import org.jsoup.nodes.FormElement

object LaposteModule extends AbstractModule {
  val uri = "https://laposte.net/"
  val moduleName = "Laposte"

  def tryLogin(creds: Credentials): Boolean = {
    val loginUri = "https://www.laposte.net/accueil"
    val resp = get(loginUri)
      .validateTLSCertificates(false)
      .execute()

    // Parse the form the response
    val form: FormElement = resp
      .parse()
      .select("form")
      .first()
      .asInstanceOf[FormElement]

    // Update the form data to include username and password
    val usernameKey = "login"
    val passwordKey = "password"
    val formdata: Map[String, String] = form
      .formData()
      .map { e => e.key() -> e.value() }
      .toMap
    val updatedFormData = formdata + (usernameKey -> creds.username) + (passwordKey -> creds.password)

    // Send login request
    val loginUri2 = "https://compte.laposte.net/login.do"
    val loginResp = post(loginUri2)
      .header("Content-Type", "application/x-www-form-urlencoded")
      .data(updatedFormData)
      .cookies(resp.cookies())
      .followRedirects(false)
      .validateTLSCertificates(false)
      .execute()

    // Check login result
    loginResp.statusCode() == 302
  }
}

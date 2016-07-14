package fish.philwants.modules

import fish.philwants.Credentials
import org.jsoup.Connection.Response
import scala.collection.JavaConversions._
import org.jsoup.nodes.FormElement

object DigitalOceanModule extends AbstractModule {
  val uri = "https://digitialcoean.com/"
  val moduleName = "DigitalOcean"

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
    val usernameKey = "user%5Bemail%5"
    val passwordKey = "user%5Bemail%5"
    val formdata: Map[String, String] = form.formData().map { e => e.key() -> e.value() }.toMap
    val updatedFormData = formdata + (usernameKey -> creds.username) + (passwordKey -> creds.password)

    // Send login request
    val loginUri2 = "https://digitalocean.com/session"
    val loginResp = post(loginUri2)
      .header("Content-Type", "application/x-www-form-urlencoded")
      .data(updatedFormData)
      .cookies(resp.cookies())
      .followRedirects(false)
      .execute()

    // Check the Location response header
    !loginResp.header("Location").matches("https://cloud.digitalocean.com/droplets")
  }
}

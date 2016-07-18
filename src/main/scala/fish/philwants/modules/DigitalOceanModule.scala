package fish.philwants.modules

import com.typesafe.scalalogging.LazyLogging
import fish.philwants.Credentials
import org.jsoup.Connection.Response

import scala.collection.JavaConversions._
import org.jsoup.nodes.FormElement

object DigitalOceanModule extends AbstractModule with LazyLogging {
  val uri = "https://digitalocean.com"
  val moduleName = "DigitalOcean"

  def tryLogin(creds: Credentials): Boolean = {
    val loginUri = "https://cloud.digitalocean.com/login"
    val resp = get(loginUri).execute()

    // Parse the form the response
    val form = resp
      .parse()
      .select("form")
      .first()
      .asInstanceOf[FormElement]

    // Update the form data to include username and password
    val usernameKey = "user[email]"
    val passwordKey = "user[password]"
    val formdata: Map[String, String] = form.formData().map { e => e.key() -> e.value() }.toMap
    val updatedFormData = formdata + (usernameKey -> creds.username) + (passwordKey -> creds.password)

    // Send login request
    val loginUri2 = "https://cloud.digitalocean.com/sessions"
    val loginResp = post(loginUri2)
      .header("Content-Type", "application/x-www-form-urlencoded")
      .data(updatedFormData)
      .cookies(resp.cookies())
      .followRedirects(false)
      .execute()

    // Check the Location response header
    val location = loginResp.header("Location")
    location.matches("https://cloud.digitalocean.com/droplets")
  }
}

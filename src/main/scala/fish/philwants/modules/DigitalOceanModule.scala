package fish.philwants.modules

import com.typesafe.scalalogging.LazyLogging
import fish.philwants.Credentials
import org.jsoup.Connection.Response
import fish.philwants.JsoupImplicits._

import scala.collection.JavaConversions._
import org.jsoup.nodes.FormElement

object DigitalOceanModule extends AbstractModule with LazyLogging {
  val uri = "https://digitalocean.com"
  val moduleName = "DigitalOcean"

  def tryLogin(creds: Credentials): Boolean = {
    val loginUri = "https://cloud.digitalocean.com/login"
    val resp = get(loginUri).execute()

    // Get the form and update it with credentials
    val form = resp.firstForm
      .update("user[email]", creds.username)
      .update("user[password]", creds.password)

    // Send login request
    val loginResp = form
      .submit()
      .header("Content-Type", "application/x-www-form-urlencoded")
      .cookies(resp.cookies())
      .followRedirects(false)
      .execute()

    // Check the Location response header
    val location = loginResp.header("Location")
    location.matches("https://cloud.digitalocean.com/droplets")
  }
}

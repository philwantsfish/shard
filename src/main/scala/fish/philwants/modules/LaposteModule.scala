package fish.philwants.modules

import fish.philwants.Credentials
import scala.collection.JavaConversions._
import org.jsoup.nodes.FormElement
import fish.philwants.JsoupImplicits._

object LaposteModule extends AbstractModule {
  val uri = "https://laposte.net/"
  val moduleName = "Laposte"

  def tryLogin(creds: Credentials): Boolean = {
    val loginUri = "https://www.laposte.net/accueil"
    val resp = get(loginUri)
      .validateTLSCertificates(false)
      .execute()

    // Get the form and update it with credentials
    val form = resp.firstForm
      .update("login", creds.username)
      .update("password", creds.password)

    // Try to login
    val loginResp = form
      .submit()
      .header("Content-Type", "application/x-www-form-urlencoded")
      .cookies(resp.cookies())
      .followRedirects(false)
      .validateTLSCertificates(false)
      .execute()

    // Check login result
    loginResp.statusCode() == 302
  }
}

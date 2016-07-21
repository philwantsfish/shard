package fish.philwants.modules

import fish.philwants.Credentials
import org.jsoup.Connection.Response
import scala.collection.JavaConversions._
import org.jsoup.nodes.FormElement
import fish.philwants.JsoupImplicits._

object BitBucketModule extends AbstractModule {
  val uri = "https://bitbucket.org/"
  val moduleName = "BitBucket"

  def tryLogin(creds: Credentials): Boolean = {
    val loginUri = "https://bitbucket.org/account/signin/?next=/"
    val resp = get(loginUri).execute()

    // Get the form and update it with credentials
    val form = resp.selectForm("form.aui.aid-form.errors-below-inputs")
      .update("username", creds.username)
      .update("password", creds.password)

    // Send login request
    val loginResp = form
      .submit()
      .header("Content-Type", "application/x-www-form-urlencoded")
      .header("Referer", loginUri)
      .cookies(resp.cookies())
      .followRedirects(false)
      .ignoreHttpErrors(true)
      .execute()

    // Check login result
    loginResp.statusCode() == 302
  }
}

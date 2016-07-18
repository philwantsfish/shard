package fish.philwants.modules

import fish.philwants.Credentials
import org.jsoup.Connection.Response
import scala.collection.JavaConversions._
import org.jsoup.nodes.FormElement

object BitBucketModule extends AbstractModule {
  val uri = "https://bitbucket.org/"
  val moduleName = "BitBucket"

  def tryLogin(creds: Credentials): Boolean = {
    val loginUri = "https://bitbucket.org/account/signin/?next=/"
    val resp = get(loginUri).execute()

    // Parse the form the response
    val form = resp
      .parse()
      .select("form.aui.aid-form.errors-below-inputs")
      .first()
      .asInstanceOf[FormElement]

    // Update the form data to include username and password
    val usernameKey = "username"
    val passwordKey = "password"
    val formdata: Map[String, String] = form.formData().map { e => e.key() -> e.value() }.toMap
    val updatedFormData = formdata + (usernameKey -> creds.username) + (passwordKey -> creds.password)

    // Send login request
    val loginUri2 = "https://bitbucket.org/account/signin/"
    val loginResp = post(loginUri2)
      .header("Content-Type", "application/x-www-form-urlencoded")
      .header("Referer", loginUri)
      .data(updatedFormData)
      .cookies(resp.cookies())
      .followRedirects(false)
      .ignoreHttpErrors(true)
      .execute()

    // Check login result
    loginResp.statusCode() == 302
  }
}

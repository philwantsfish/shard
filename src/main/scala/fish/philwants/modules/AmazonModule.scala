package fish.philwants.modules

import fish.philwants.Credentials
import org.jsoup.Connection.Response
import org.jsoup.{Connection, Jsoup}
import scala.collection.JavaConversions._
import org.jsoup.nodes.FormElement

object AmazonModule extends AbstractModule {
  val uri = "https://amazon.com/"
  val moduleName = "Amazon"


  def tryLogin(creds: Credentials): Boolean = {
    val loginUri = "https://www.amazon.com/ap/signin?_encoding=UTF8&openid.assoc_handle=usflex&openid.claimed_id=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.identity=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&openid.mode=checkid_setup&openid.ns=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0&openid.ns.pape=http%3A%2F%2Fspecs.openid.net%2Fextensions%2Fpape%2F1.0&openid.pape.max_auth_age=0&openid.return_to=https%3A%2F%2Fwww.amazon.com%2F%3Fref_%3Dnav_signin"
//
//    val referer = loginUri
//
    val resp = get(loginUri).execute()
//
//    // Parse the form the response
//    val form: FormElement = resp
//      .parse()
//      .select("form")
//      .first()
//      .asInstanceOf[FormElement]
//
//    // Update the form data to include username and password
//    val usernameKey = "email"
//    val passwordKey = "password"
//    val formdata: Map[String, String] = form.formData().map { e => e.key() -> e.value() }.toMap
//    val updatedFormData = formdata + (usernameKey -> creds.username) + (passwordKey -> creds.password)
//
//    println("form data" + updatedFormData)

    val referer = "https://www.amazon.com/ap/signin?_encoding=UTF8&amp;openid.assoc_handle=usflex&amp;openid.claimed_id=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&amp;openid.identity=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0%2Fidentifier_select&amp;openid.mode=checkid_setup&amp;openid.ns=http%3A%2F%2Fspecs.openid.net%2Fauth%2F2.0&amp;openid.ns.pape=http%3A%2F%2Fspecs.openid.net%2Fextensions%2Fpape%2F1.0&amp;openid.pape.max_auth_age=0&amp;openid.return_to=https%3A%2F%2Fwww.amazon.com%2F%3Fref_%3Dnav_signin"

    val formdata = Map(
      "openid.assoc_handle" -> "usflex",
      "openid.claimed_id" -> "http://specs.openid.net/auth/2.0/identifier_select",
      "openid.identity" -> "http://specs.openid.net/auth/2.0/identifier_select",
      "openid.mode" -> "checkid_setup",
      "openid.ns" -> "http://specs.openid.net/auth/2.0",
      "openid.ns.pape" -> "http://specs.openid.net/extensions/pape/1.0",
      "openid.pape.max_auth_age" -> "0",
      "openid.return_to" -> "https://www.amazon.com/?ref_=nav_signin",
      "email" -> creds.username,
      "password" -> creds.password
    )

    // Send login request
    val loginUri2 = "https://www.amazon.com/ap/signin"
    val loginResp = post(loginUri2)
      .header("Content-Type", "application/x-www-form-urlencoded")
      .header("Referer", referer)
      .data(formdata)
//      .cookies(resp.cookies())
      .followRedirects(false)
      .execute()

    // Check login result
//    println(s"Status code: ${loginResp.statusCode()}")
    loginResp.statusCode() == 302
  }
}

package fish.philwants.modules

import fish.philwants.Credentials
import org.jsoup.Connection.Response
import org.jsoup.{Connection, Jsoup}
import scala.collection.JavaConversions._
import spray.json._


object InstagramModule extends AbstractModule {
  override val moduleName: String = "Instagram"
  override val uri: String = "https://www.instagram.com"
  import JsonFormats._

  // Given credentials return a LoginResult
  override def tryLogin(creds: Credentials): Boolean = {
    // Request the login page for the form and cookies
    val loginUri = "https://www.instagram.com/accounts/login/"
    val resp = get(loginUri).execute()

    // Update the form data to include username and password
    val params = Map("username" -> creds.username, "password" -> creds.password)

    // Send login request
    val loginUri2 = "https://www.instagram.com/accounts/login/ajax/"

    val loginResp = post(loginUri2)
      .header("Content-Type", "application/x-www-form-urlencoded")
      .header("X-CSRFToken", resp.cookie("csrftoken"))
      .header("Accept", "*/*")
      .header("Referer", "https://www.instagram.com")
      .data(params)
      .cookies(resp.cookies())
      .followRedirects(false)
      .execute()

    // Check login result
    isLoginSuccessful(loginResp)
  }

  def isLoginSuccessful(resp: Response): Boolean = {
    resp.body().parseJson.convertTo[InstagramResponse].authenticated
  }
}

case class InstagramResponse(status: String, authenticated: Boolean, user: String)

object JsonFormats extends DefaultJsonProtocol {
  implicit val instaResponseFormat: JsonReader[InstagramResponse] = jsonFormat3(InstagramResponse)
}




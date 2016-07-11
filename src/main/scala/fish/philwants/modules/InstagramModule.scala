package fish.philwants.modules

import fish.philwants.Credentials
import org.jsoup.Connection.Response
import org.jsoup.nodes.FormElement
import org.jsoup.{Connection, Jsoup}
import scala.collection.JavaConversions._
import spray.json._
import spray.json.DefaultJsonProtocol._


object InstagramModule extends AbstractModule {
  override val moduleName: String = "Instagram"
  override val uri: String = "https://www.instagram.com"
  import JsonFormats._

  // Given credentials return a LoginResult
  override def tryLogin(creds: Credentials): LoginResult = {
    val loginUri = "https://www.instagram.com/accounts/login/"

    // Request the login page for the form and cookies
    val resp = Jsoup.connect(loginUri)
      .method(Connection.Method.GET)
      .header("User-Agent", useragent)
      .execute()

    // Update the form data to include username and password
    val params = Map("username" -> creds.username, "password" -> creds.password)

    // Send login request
    val loginUri2 = "https://www.instagram.com/accounts/login/ajax/"

    val loginResp = Jsoup
      .connect(loginUri2)
      .method(Connection.Method.POST)
      .header("User-Agent", useragent)
      .header("Content-Type", "application/x-www-form-urlencoded")
      .header("X-CSRFToken", resp.cookie("csrftoken"))
      .header("Accept", "*/*")
      .header("Referer", "https://www.instagram.com")
      .ignoreContentType(true)
      .data(params)
      .cookies(resp.cookies())
      .followRedirects(false)
      .execute()

    // Check login result
    if(isLoginSuccessful(loginResp)) SuccessfulLogin(creds, moduleName, uri)
    else FailedLogin(creds, moduleName, uri)
  }

  def isLoginSuccessful(resp: Response): Boolean = {
    resp.body().parseJson.convertTo[InstagramResponse].authenticated
  }
}

case class InstagramResponse(status: String, authenticated: Boolean, user: String)

object JsonFormats extends DefaultJsonProtocol {
  implicit val instaResponseFormat: JsonReader[InstagramResponse] = jsonFormat3(InstagramResponse)
}




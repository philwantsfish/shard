package fish.philwants.modules

import fish.philwants.Runner.Credentials
import fish.philwants.modules.TwitterModule._
import org.jsoup.{Connection, Jsoup}
import scala.collection.JavaConversions._


object FacebookModule extends AbstractModule {
  override val moduleName: String = "Facebook"
  override val uri: String = "https://www.facebook.com"

  override def tryLogin(creds: Credentials): LoginResult = {
    val resp = Jsoup.connect(uri)
      .method(Connection.Method.GET)
      .header("User-Agent", useragent)
      .execute()


    // The login parameters
    val params = Map(
      "email" -> creds.username,
      "pass" -> creds.password
    )

    // Send login request
    val loginUri = "https://www.facebook.com/login.php"
    val loginResp = Jsoup
      .connect(loginUri)
      .method(Connection.Method.POST)
      .header("User-Agent", useragent)
      .data(params)
      .cookies(resp.cookies())
      .followRedirects(false)
      .execute()

    // Check login result
    if(loginResp.statusCode() == 302) SuccessfulLogin(creds, moduleName, uri)
    else FailedLogin(creds, moduleName, uri)
  }
}

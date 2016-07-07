package fish.philwants.modules

import fish.philwants.Runner.Credentials
import org.jsoup.{Connection, Jsoup}
import collection.JavaConverters._

object RedditModule extends AbstractModule {
  val uri = "http://www.reddit.com"
  val moduleName = "Reddit"

  def tryLogin(creds: Credentials): LoginResult = {
    val httpResponseString = loginHttpResponse(creds)

    if(successfulLogin(httpResponseString)) SuccessfulLogin(creds, moduleName, uri)
    else FailedLogin(creds, moduleName, uri)
  }

  def loginHttpResponse(creds: Credentials): String = {
    val post_data = Map("user" -> creds.username, "passwd" -> creds.password, "api_type" -> "json").asJava
    val uri = s"http://www.reddit.com/api/login/"

    Jsoup
      .connect(uri)
      .method(Connection.Method.POST)
      .ignoreContentType(true)
      .header("Content-Type", "application/x-www-form-urlencoded")
      .header("User-Agent", randUserAgent)
      .data(post_data)
      .execute()
      .body()
  }

  def successfulLogin(httpResponseString: String): Boolean = {
    if(httpResponseString.contains("wrong password")) false
    else true
  }
}

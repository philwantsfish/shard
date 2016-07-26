package fish.philwants.modules

import fish.philwants.Credentials
import collection.JavaConverters._

object RedditModule extends AbstractModule {
  val uri = "http://www.reddit.com"
  val moduleName = "Reddit"

  def tryLogin(creds: Credentials): Boolean = {
    val httpResponseString = loginHttpResponse(creds)
    successfulLogin(httpResponseString)
  }

  def loginHttpResponse(creds: Credentials): String = {
    val post_data = Map("user" -> creds.username, "passwd" -> creds.password, "api_type" -> "json").asJava
    val uri = s"http://www.reddit.com/api/login/"

    post(uri)
      .header("Content-Type", "application/x-www-form-urlencoded")
      .data(post_data)
      .execute()
      .body()
  }

  def successfulLogin(httpResponseString: String): Boolean = {
    if(httpResponseString.contains("wrong password")) false
    else true
  }
}

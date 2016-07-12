package fish.philwants.modules

import fish.philwants.Credentials
import org.jsoup.{Connection, Jsoup}
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

trait AbstractModule {
  // The URI of the website
  val uri: String
  
  // The name of the module
  val moduleName: String

  // A method to check if the given credentials can log in
  def tryLogin(creds: Credentials): Boolean

  // Pick a random User-Agent for each request
  def randUserAgent: String = {
    val userAgentHeaders = Seq("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")

    val randIndex = scala.util.Random.nextInt(userAgentHeaders.size)
    userAgentHeaders(randIndex)
  }

  def post(uri: String): Connection = {
    Jsoup
      .connect(uri)
      .method(Connection.Method.POST)
      .ignoreContentType(true)
      .header("User-Agent", useragent)
      .timeout(defaultTimeout)
  }

  def get(uri: String): Connection = {
    Jsoup
      .connect(uri)
      .method(Connection.Method.GET)
      .ignoreContentType(true)
      .header("User-Agent", useragent)
      .timeout(defaultTimeout)
  }

  def tryCredential(creds: Credentials): Boolean = {
    // Occasionally a SocketTimeoutException occurs. Retry the module before giving up
    val tryResult = Try(tryLogin(creds)) match {
      case Success(result) => Success(result)
      case Failure(e) => Try(tryLogin(creds))
    }

    tryResult.getOrElse(false)
  }

  val useragent = randUserAgent
  val defaultTimeout = 30.seconds.toMillis.toInt
}



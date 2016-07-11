package fish.philwants.modules

import fish.philwants.Credentials

trait AbstractModule {
  // The URI of the website
  val uri: String
  
  // The name of the module
  val moduleName: String

  // Given credentials return a LoginResult
  def tryLogin(creds: Credentials): LoginResult

  // Pick a random User-Agent for each request
  def randUserAgent: String = {
    val userAgentHeaders = Seq("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36")

    val randIndex = scala.util.Random.nextInt(userAgentHeaders.size)
    userAgentHeaders(randIndex)
  }

  val useragent = randUserAgent
}

trait LoginResult
case class SuccessfulLogin(creds: Credentials, moduleName: String, uri: String) extends LoginResult
case class FailedLogin(creds: Credentials, moduleName: String, uri: String) extends LoginResult

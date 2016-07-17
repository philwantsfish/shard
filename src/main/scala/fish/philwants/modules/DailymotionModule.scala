package fish.philwants.modules

import fish.philwants.Credentials
import scala.collection.JavaConversions._

object DailymotionModule extends AbstractModule {
  val uri = "http://www.dailymotion.com/"
  val moduleName = "Dailymotion"

  def tryLogin(cred: Credentials): Boolean = {
    val loginUri = "http://www.dailymotion.com/signin"
    val resp = get(loginUri).execute()

    // Send login request
    val loginUri2 = "http://www.dailymotion.com/pageitem/authenticationForm?request=%2Fsignin"
    val params = Map(
      "form_name" -> "dm_pageitem_authenticationform",
      "_csrf" -> resp.cookie("_csrf/form"),
      "_csrf_l" -> resp.cookie("_csrf/link"),
      "_fid" -> "",
      "username" -> cred.username,
      "authChoice" -> "login",
      "password" -> cred.password,
      "from_request" -> "/signin"
    )

    val loginResp = post(loginUri2)
      .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
      .header("X-Requested-With", "XMLHttpRequest")
      .header("Referer", "http://www.dailymotion.com/signin")
      .data(params)
      .cookies(resp.cookies())
      .followRedirects(false)
      .execute()

    // Check login result
    isValidHeader(loginResp.header("X-Json"))
  }

  private def isValidHeader(headerContent: String): Boolean = {
    headerContent contains "dm:register_or_login:logged"
  }
}

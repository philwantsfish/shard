package fish.philwants.modules

import fish.philwants.Credentials

object AmazonModule extends AbstractModule {
  val uri = "https://amazon.com/"
  val moduleName = "Amazon"


  def tryLogin(creds: Credentials): Boolean = {
    val r = get("https://www.amazon.com")
      .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
      .followRedirects(true)
      .execute()

    val r2 = get("https://www.amazon.com")
      .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
      .followRedirects(true)
      .cookies(r.cookies())
      .execute()
    println(s"cookies2!! ${r2.cookies()}")

    val r3 = get("https://www.amazon.com")
      .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
      .followRedirects(true)
      .cookies(r2.cookies())
      .execute()

    r3.cookie("ubid-main", r2.cookie("ubid-main"))
    println(s"cookies r3: ${r3.cookies()}")

    val loginUri = r3.parse.select("a[tabindex=3").first().attr("href")
    println(s"loginUri: $loginUri")
    val resp = get(loginUri)
      .cookies(r3.cookies())
      .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
      .followRedirects(true)
      .execute()

    val form = resp.firstForm
      .update("email", creds.username)
      .update("password", creds.password)


    println(s"cookies: ${r3.cookies()}")

    // Send login request
    val loginResp = post("https://www.amazon.com/ap/signin")
      .header("Content-Type", "application/x-www-form-urlencoded")
      .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
      .data(form.formData())
      .cookies(r3.cookies())
      .followRedirects(false)
      .execute()

    println(s"Response code ${loginResp.statusMessage()}")

    // Check login result
//    println(s"Status code: ${loginResp.statusCode()}")
    loginResp.statusCode() == 302
  }
}

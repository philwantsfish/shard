package fish.philwants


object Util {
  val emailregex = """[^@]+@[^\.].{2,3}"""
  def emailToUsername(email: String): String = {
    if(email.matches(emailregex)) email.substring(0, email.indexOf('@'))
    else email
  }
}

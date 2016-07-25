package fish.philwants.modes

import fish.philwants.{Credentials, ValidCredentials}
import fish.philwants.modules.AbstractModule
import scala.io.{BufferedSource, Source}

object SingleUserMultiPasswordMode extends ModeHelper {

  def collect(username: String, path: String, modules: Seq[AbstractModule]): Unit = {
    logger.info("Running in single-user multi-password mode")

    val passwords = parsePasswordsFromFile(Source.fromFile(path))
    logger.info(s"Parsed ${passwords.size} passwords")
    logger.info(s"Running ${modules.size} modules")

    val creds = passwords.map { p => Credentials(username, p) }
    val credsFound = creds.map { c => ValidCredentials(c, modules.filter { m => m.tryCredential(c) }) }

    credsFound.foreach { cf => printResults(cf) }
  }

  def parsePasswordsFromFile(path: BufferedSource): Seq[String] = {
    val passwords = path.getLines().toStream
    passwords
  }

}

package fish.philwants.modes

import fish.philwants.Main._
import fish.philwants.{Credentials, ValidCredentials}
import fish.philwants.modules.AbstractModule

import scala.io.{BufferedSource, Source}
import scala.util.matching.Regex

object MultiUserMultiPasswordMode extends ModeHelper {

  def collect(path: String, format: String, modules: Seq[AbstractModule]): Unit = {
    logger.info("Running in multi-user multi-password mode")

    val credentialRegex = if (format.nonEmpty) format else defaultCredentialRegex
    val creds = parseCredsFromFile(Source.fromFile(path), credentialRegex.r)
    logger.info(s"Parsed ${creds.size} credentials")
    logger.info(s"Running ${modules.size} modules")

    creds
      .map { c => ValidCredentials(c, modules.filter { m => m.tryCredential(c) }) }
      .foreach { vc => printResults(vc) }
  }

  def parseCredsFromFile(path: BufferedSource, credentialRegex: Regex): Stream[Credentials] = {
    val credentialLines = path.getLines().toStream

    credentialLines.flatMap {
      case credentialRegex(username, password) => Some(Credentials(username, password))
      case _ => None
    }
  }
}

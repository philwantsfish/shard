package fish.philwants.modes

import fish.philwants.{Credentials, ValidCredentials}
import fish.philwants.modules.AbstractModule

object SingleUserSinglePasswordMode extends ModeHelper {
  def collect(creds: Credentials, modules: Seq[AbstractModule]): Unit = {
    logger.info(s"Selected single-user single-password mode")
    logger.info(s"Running ${modules.size} modules")
    val credsFound = ValidCredentials(creds, modules.filter { m => m.tryCredential(creds) })
    printResults(credsFound)
  }
}

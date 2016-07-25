package fish.philwants.modes

import com.typesafe.scalalogging.LazyLogging
import fish.philwants.{Credentials, ModuleFactory, ValidCredentials}


trait ModeHelper extends LazyLogging {
  def tryCredential(creds: Credentials, moduleFilter: Seq[String]): ValidCredentials = {
    val modules = ModuleFactory.modules.filter { m => moduleFilter.contains(m.moduleName) }
    logger.info(s"Running ${modules.size} modules")
    ValidCredentials(creds, modules.filter { m => m.tryCredential(creds) })
  }

  def printResults(vc: ValidCredentials): Unit = {
    if (vc.modules.size <= 0) {
      logger.info(s"${vc.creds} - No results")
    } else {
      val successfulModuleNames = vc.modules.map { m => m.moduleName }.mkString(", ")
      logger.info(s"${vc.creds} - $successfulModuleNames")
    }
  }

}

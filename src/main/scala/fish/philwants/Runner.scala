package fish.philwants

import com.typesafe.scalalogging.LazyLogging
import fish.philwants.modules._
import scala.io.{BufferedSource, Source}
import scala.util.matching.Regex

case class Credentials(username: String, password: String) {
  override def toString: String = { s"$username:$password" }
}

case class ValidCredentials(creds: Credentials, modules: Seq[AbstractModule])

object Runner extends LazyLogging {
  val defaultCredentialRegex = """"((?:\"|[^"])+)":"((?:\"|[^"])+)""""
  val versionNumber = "1.2"

  def singleCredentialMode(username: String, password: String, moduleFilter: Seq[String]): Unit = {
    logger.info("Running in single credential mode")
    val creds = Credentials(username, password)
    printResults(tryCredential(creds, moduleFilter))
  }

  def multiCredentialMode(path: String, format: String, moduleFilter: Seq[String]): Unit = {
    logger.info("Running in multi-credential mode")
    val credentialRegex = if (format.nonEmpty) format else defaultCredentialRegex
    val creds = parseCredsFromFile(Source.fromFile(path), credentialRegex.r)
    logger.info(s"Parsed ${creds.size} credentials")

    creds
      .map { c => tryCredential(c, moduleFilter) }
      .foreach { vc => printResults(vc) }
  }

  def tryCredential(creds: Credentials, moduleFilter: Seq[String]): ValidCredentials = {
    val modules = ModuleFactory.modules.filter { m => moduleFilter.contains(m.moduleName) }
    logger.info(s"Running ${modules.size} modules")
    ValidCredentials(creds, modules.filter { m => m.tryCredential(creds) })
  }

  def parseCredsFromFile(path: BufferedSource, credentialRegex: Regex): Stream[Credentials] = {
    val credentialLines = path.getLines().toStream

    credentialLines.flatMap {
      case credentialRegex(username, password) => Some(Credentials(username, password))
      case _ => None
    }
  }

  def printResults(vc: ValidCredentials): Unit = {
    if (vc.modules.size <= 0) {
      logger.info(s"${vc.creds} - No results")
    } else {
      val successfulModuleNames = vc.modules.map { m => m.moduleName }.mkString(", ")
      logger.info(s"${vc.creds} - $successfulModuleNames")
    }
  }

  def main(args: Array[String]): Unit = {
    case class Config(
                     list: Boolean = false,
                     verbose: Boolean = false,
                     username: String = "",
                     password: String = "",
                     file: String = "",
                     format: String = "",
                     modules: String = "",
                     version: Boolean = false
                     )

    val parser = new scopt.OptionParser[Config](s"java -jar shard-$versionNumber.jar") {
      head("Shard", versionNumber)

      opt[String]('u', "username")
        .action((v,c) => c.copy(username = v))
        .text("Username to test")

      opt[String]('p', "password")
        .action((v,c) => c.copy(password = v))
        .text("Password to test")

      opt[String]('f', "file")
        .action((v,c) => c.copy(file = v))
        .text("File containing a set of credentials")

      opt[String]("format")
      .action((v,c) => c.copy(format = v))
      .text("The format of the credentials. Must be a regular expression with 2 capture groups. The first capture group for the username and the second capture group for the password. Defaults to a regex that will match:\n\t\"username\":\"password\"")

      opt[Unit]('l', "list")
        .action( (_, c) => c.copy(list = true))
        .text("List available modules")

      opt[Unit]('v', "version")
        .action( (_, c) => c.copy(version = true))
        .text("Print the version")

      opt[String]("modules")
          .action( (v, c) => c.copy(modules = v))
            .text("Only run specific modules. A comma separated list")

      help("help")
        .text("prints this usage text")
    }

    parser.parse(args, Config()) match {
      case Some(config) =>
        val moduleFilter: Seq[String] = if(config.modules.nonEmpty) config.modules.split(',') else Seq()

        // Print the list of modules if asked
        if(config.version) {
          println(s"Shard version $versionNumber")
        } else if(config.list) {
          val modules = ModuleFactory.modules
          println("Available modules:")
          modules.foreach { m => println(s"\t${m.moduleName}")}
        } else if(config.file.nonEmpty) {
          // User has provided a file of credentials
          Runner.multiCredentialMode(config.file, config.format, moduleFilter)
        } else if(config.username.nonEmpty && config.password.nonEmpty) {
          // User has provided a single username and password combination
          Runner.singleCredentialMode(config.username, config.password, moduleFilter)
        } else {
          println("Must provide a file of credentials (-f) or a single credential using -u and -p.")
        }
      case None =>
        println("Bad arguments")
    }
  }
}
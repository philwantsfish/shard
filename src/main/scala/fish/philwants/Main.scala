package fish.philwants

import com.typesafe.scalalogging.LazyLogging
import fish.philwants.modes._

/**
  * This is the main entry point for shard. This object is responsible for parsing the input parameters, selecting
  * an execution mode, and running it.
  */
object Main extends LazyLogging {
  val defaultCredentialRegex = """"((?:\"|[^"])+)":"((?:\"|[^"])+)""""
  val versionNumber = "1.5"

  val modeExplanation =
    s"""
      |Shard ($versionNumber) can run in 3 modes:
      |
      |1) Single user single password          - Use -u and -p
      |2) Single user multiple passwords       - Use -u and -f
      |3) Multiple users and multple passwords - Use -f only
      |
      |For more detailed usage examples see the wiki.
    """.stripMargin

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
      head(modeExplanation)

      opt[String]('u', "username")
        .action((v,c) => c.copy(username = v))
        .text("Username to test")

      opt[String]('p', "password")
        .action((v,c) => c.copy(password = v))
        .text("Password to test")

      opt[String]('f', "file")
        .action((v,c) => c.copy(file = v))
        .text("A path to a file containing a set of credentials or passwords")

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
        .text("Prints this usage text")
    }

    parser.parse(args, Config()) match {
      case Some(config) =>
        val moduleFilter: Seq[String] = if(config.modules.nonEmpty) config.modules.split(',') else ModuleFactory.modules.map(_.moduleName)
        val modules = ModuleFactory.modules.filter { m => moduleFilter.contains(m.moduleName) }

        // Check the flags and execute the appropriate action
        if(config.version) {
          logger.info(s"Shard version $versionNumber")
        } else if(config.list) {
          logger.info("Available modules:")
          ModuleFactory.modules.foreach { m => logger.info(s"\t${m.moduleName}")}
        } else if(config.username.nonEmpty && config.password.nonEmpty) {
          // if -u and -p flags are passed run single-user single-password mode
          val creds = Credentials(config.username, config.password)
          SingleUserSinglePasswordMode.collect(creds, modules)
        } else if (config.username.nonEmpty && config.file.nonEmpty) {
          // if -u and -f are passed run single-user multi-password mode
          SingleUserMultiPasswordMode.collect(config.username, config.file, modules)
        } else if(config.file.nonEmpty) {
          // if only the -f flag is passed run multi-user multi-password mode
          MultiUserMultiPasswordMode.collect(config.file, config.format, modules)
        } else {
          logger.info("Must provide a file of credentials (-f) or a single credential using -u and -p.")
        }
      case None =>
        logger.info("Bad arguments, must select a mode")
        logger.info("Use --help for more information or see the wiki")
    }
  }
}
package fish.philwants

import com.typesafe.scalalogging.LazyLogging
import fish.philwants.modules.{LoginResult, FailedLogin, SuccessfulLogin, RedditModule}
import Runner.Credentials
import scala.io.Source

object Runner extends LazyLogging {
  case class Credentials(username: String, password: String)

  def parseCreds(): Seq[Credentials] = {
    val credStrs = Source.fromInputStream(getClass.getResourceAsStream("/creds.txt")).getLines().toSeq
    credStrs.map { cStr =>
      val t = cStr.split(":").toSeq
      Credentials(t(0), t(1))
    }
  }

  def tryCredential(creds: Credentials): Seq[LoginResult] = {
    ModuleFactory.modules.map { m => m.tryLogin(creds)}
  }

  def printResult(results: Seq[LoginResult]): Unit = {
    logger.info(s"Tried credentials on ${ModuleFactory.modules.size} sites")
    val groupedResults = results.groupBy {
      case _: SuccessfulLogin => "success"
      case _: FailedLogin => "failed"
    }

    val successfulResults = groupedResults
      .getOrElse("success", Seq())
      .map { r => r.asInstanceOf[SuccessfulLogin] }
      .map { r => r.moduleName }

    val failedResults = groupedResults
      .getOrElse("failed", Seq())
      .map { r => r.asInstanceOf[FailedLogin] }
      .map { r => r.moduleName }

    logger.info(s"Failed authentication for ${failedResults.size} sites:")
    failedResults.foreach { name => logger.info(s"\t$name") }

    logger.info(s"Discovered ${successfulResults.size} successful authentications:")
    successfulResults.foreach { name => logger.info(s"\t$name") }

  }

  def main(args: Array[String]): Unit = {
    case class Config(
                     list: Boolean = false,
                     verbose: Boolean = false,
                     username: String = "",
                     password: String = ""
                       )

    val parser = new scopt.OptionParser[Config]("java -jar shard-1.0.jar") {
      head("Shard", "1.0")

      opt[String]('u', "username")
        .action((v,c) => c.copy(username = v))
        .text("Username to test")

      opt[String]('p', "password")
        .action((v,c) => c.copy(password = v))
        .text("Password to test")

      opt[Unit]('l', "list")
        .action( (_, c) => c.copy(list = true))
        .text("List available modules")

      opt[Unit]("verbose")
        .action( (_,c) => c.copy(verbose = true))
        .text("Verbose logging")


      help("help")
        .text("prints this usage text")
    }

    parser.parse(args, Config()) match {
      case Some(config) =>
        // Print the list of modules if asked
        if(config.list) {
          val modules = ModuleFactory.modules
          println("Available modules:")
          modules.foreach { m => println(s"\t${m.moduleName}")}
        } else if(config.username.isEmpty || config.password.isEmpty) {
          println("Missing a username or password")
        } else {
          val results = Runner.tryCredential(Credentials(config.username, config.password))
          Runner.printResult(results)
        }
      case None =>
        println("Bad arguments")
    }
  }
}
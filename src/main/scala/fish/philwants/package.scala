package fish

import fish.philwants.modules.AbstractModule

package object philwants {

  case class Credentials(username: String, password: String) {
    override def toString: String = { s"$username:$password" }
  }

  case class ValidCredentials(creds: Credentials, modules: Seq[AbstractModule])



}

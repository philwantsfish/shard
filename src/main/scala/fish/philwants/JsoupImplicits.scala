package fish.philwants

import org.jsoup.Connection
import org.jsoup.nodes.FormElement
import scala.language.implicitConversions

/**
  * This trait contains pimps for the Jsoup library. Specifically the Connection.Response and FormElement objects.
  * This trait should be mixed with a package object to avoid import tax
  */
trait JsoupImplicits {
  implicit def connectionResponseImplicit(resp: Connection.Response): ConnectionResponseImplicit =
    new ConnectionResponseImplicit(resp)

  implicit def formElementImplicit(form: FormElement): FormElementImplicit =
    new FormElementImplicit(form)
}

class ConnectionResponseImplicit(resp: Connection.Response) {
  def firstForm: FormElement = resp.parse().select("form").first().asInstanceOf[FormElement]
  def selectForm(selector: String): FormElement = resp.parse().select(selector).first().asInstanceOf[FormElement]
  def getFormById(id: String): FormElement = resp.parse().getElementById(id).asInstanceOf[FormElement]
}

class FormElementImplicit(form: FormElement) {
  def update(key: String, value: String): FormElement = {
    form.elements().select(s"[name=$key]").first().attr("value", value)
    form
  }
}


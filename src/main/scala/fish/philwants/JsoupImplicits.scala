package fish.philwants

import org.jsoup.Connection
import org.jsoup.nodes.FormElement

import scala.language.implicitConversions


object JsoupImplicits {
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


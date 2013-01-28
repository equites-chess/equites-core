package controllers

import play.api._
import play.api.mvc._

import eu.timepit.equites._

object Application extends Controller {
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def knightstour = Action {
    Ok("works " + Square.random().toString)
  }
}

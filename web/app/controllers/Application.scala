package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  def knightstour = Action {
    Ok(views.html.knightstour())
  }
}

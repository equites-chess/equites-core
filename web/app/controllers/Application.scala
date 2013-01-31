package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  def knightstour_html = Action {
    Ok(views.html.knightstour())
  }
}

package controllers

import play.api._
import play.api.mvc._

import play.api.libs.json.Json._

import eu.timepit.equites._
import eu.timepit.equites.problems.KnightsTour._

object Application extends Controller {
  def knightstour_html = Action {
    Ok(views.html.knightstour())
  }

  def knightstour = Action {
    Ok(toJson(warnsdorffTour(Square.random()).map(_.toSeq)))
  }
}

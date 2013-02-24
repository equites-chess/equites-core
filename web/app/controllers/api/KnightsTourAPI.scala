// Equites, a Scala chess playground
// Copyright © 2013 Frank S. Thomas <frank@timepit.eu>
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package controllers
package api

import play.api.libs.json.Json._
import play.api.mvc.Action
import play.api.mvc.Results._

import eu.timepit.equites._
import eu.timepit.equites.problem.KnightsTour._

object KnightsTourAPI {
  def staticTourAsJson = genericTourAsJson(staticTour)
  def randomTourAsJson = genericTourAsJson(randomTour)
  def warnsdorffTourAsJson = genericTourAsJson(warnsdorffTour)
  def randomWarnsdorffTourAsJson = genericTourAsJson(randomWarnsdorffTour)

  def genericTourAsJson(tourFun: Square => Stream[Square]) = Action {
    Ok(toJson(tourFun(Square.randomImpure()).map(_.toSeq)))
      .withHeaders(("Cache-Control", "no-cache"))
  }
}

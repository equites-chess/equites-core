abstract class Color
case object White extends Color
case object Black extends Color

abstract class Piece
// def possibleMoves

class King   extends Piece
// val s = List(-1,0,1)
// for (x <- s; y <- s; if (x != 0 || y != 0)) yield (x, y)

class Queen  extends Piece
// combination of rook and bishop

class Rook   extends Piece
// s.map((_, 0)) ++ s.map((0,_))

class Bishop extends Piece
// val s = -7 to 7 ohne null!
// (t zip t).map( f => (-f._1, f._2) )

class Knight extends Piece
// val t = List(-2,-1,1,2)
// for (x <- t; y <- t; if x.abs != y.abs) yield (x, y)

class Pawn   extends Piece

//class Position {
//  self: Pair[Int, Int]
//}

class Move

class Board


val k = new King
println(k)

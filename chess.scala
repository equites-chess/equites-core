abstract class Color
case object White extends Color
case object Black extends Color

abstract class Piece
// def possibleMoves

class King   extends Piece
class Queen  extends Piece
class Rook   extends Piece
class Bishop extends Piece
class Knight extends Piece
class Pawn   extends Piece

//class Position {
//  self: Pair[Int, Int]
//}

class Move

class Board

// [+-1, +-1] King
// [+-7, 0] or [0, +-7] Rook
// [+-2, +-1] or [+-1, +-2] Knight

val k = new King
println(k)

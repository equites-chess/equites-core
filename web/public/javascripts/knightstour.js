var board_width = 600;
var board_height = 600;

var file_count = 8;
var rank_count = 8;

var square_width = board_width / file_count;
var square_height = board_height / rank_count;

function createBoard(paper, width, height) {
  var board = paper.rect(0, 0, width, height);
  board.attr({
    "stroke": "#ddd",
    "stroke-width": 4
  });
  return board;
}

function createSquares(paper) {
  var squares = new Array(file_count);
  for (var file = 0; file < file_count; file++) {
    squares[file] = new Array(rank_count);
    for (var rank = 0; rank < rank_count; rank++) {
      var x = square_width * file;
      var y = square_height * (rank_count - rank -1);

      var square = paper.rect(x, y, square_width, square_height);
      square.attr({
        "stroke": "#ddd",
        "stroke-width": 2
      });

      squares[file][rank] = square;
    }
  }
  return squares;
}

function visitSquare(square) {
  square.attr("fill", "#666");
  square.toBack();
}

function drawLine(line, last_square, curr_square) {
  if (last_square == null || curr_square == null) return;

  var last_cx = last_square.attr("x") + square_width / 2;
  var last_cy = last_square.attr("y") + square_height / 2;

  var curr_cx = curr_square.attr("x") + square_width / 2;
  var curr_cy = curr_square.attr("y") + square_height / 2;

  var new_path = "M" + last_cx + "," + last_cy +
                 "L" + curr_cx + "," + curr_cy;

  var path = line.attr("path");
  line.attr("path", path + new_path);
}

window.onload = function () {
  var paper = Raphael("canvas", board_width, board_height);
  var squares = createSquares(paper);
  var board = createBoard(paper, board_width, board_height);

  var tour = {};
  var index = 0;

  var last_square = null;
  var curr_square = null;
  var line = paper.path();

  line.attr({
    "arrow-end": "block-wide-long",
    "stroke": "#FFBF00",
    "stroke-linecap": "round",
    "stroke-width": 3
  });

  function jump() {
    curr_square = squares[tour[index][0]][tour[index][1]];
    visitSquare(curr_square);
    drawLine(line, last_square, curr_square);

    setTimeout(repeat, 500);
  }

  function repeat() {
    ++index;
    last_square = curr_square;

    if (index < tour.length) {
      jump();
    } else {
      window.location.reload(false);
    }
  }

  //$.getJSON("api/knightstour/static.json", function(json) {
  $.getJSON("api/knightstour/warnsdorff.json", function(json) {
  //$.getJSON("api/knightstour/random.json", function(json) {
    tour = eval(json);
    jump();
  });
};

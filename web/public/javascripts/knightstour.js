var curr = 0;
var tour = {};

window.onload = function () {
  var board_width = 600,
      board_height = 600;

  var file_cnt = 8;
  var rank_cnt = 8;

  var square_width = board_width / file_cnt,
      square_height = board_height / rank_cnt;

  var paper = Raphael("canvas", board_width, board_height);

  var squares = new Array(file_cnt);
  for (var file = 0; file < file_cnt; file++) {
    squares[file] = new Array(rank_cnt);
    for (var rank = 0; rank < rank_cnt; rank++) {
      squares[file][rank] =
        paper.rect(file * square_width, (rank_cnt - rank - 1) * square_height,
                   square_width, square_height);
      squares[file][rank].attr("stroke", "#909090");
    }
  }

  var board = paper.rect(0, 0, board_width, board_height);
  board.attr({
    "stroke": "#808080",
    "stroke-width": 3
  });

  function visit(file, rank) {
    var square = squares[file][rank];
    square.attr("fill", "#EEE");
  }
  
  function visitStart() {
    visit(tour[curr][0], tour[curr][1]);
    setTimeout(visitEnd, 1000);
  }
  function visitEnd(){
    ++curr;
    visitStart();
  }

  $.getJSON("knightstour.json", function(json) {
    tour = eval(json);
    visitStart()
  });

};

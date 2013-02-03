var knightstour = function () {
    function centerOf(elem) {
        return {
            x: elem.attr("x") + elem.attr("width")  / 2,
            y: elem.attr("y") + elem.attr("height") / 2
        };
    }

    function lineBetween(fromElem, toElem) {
        var c1 = centerOf(fromElem);
        var c2 = centerOf(toElem);

        return "M" + c1.x + "," + c1.y +
               "L" + c2.x + "," + c2.y;
    }

    function addLine(path, fromElem, toElem) {
        if (fromElem === null || toElem === null) {
            return;
        }
        var path_attr = path.attr("path");
        path.attr("path", path_attr + lineBetween(fromElem, toElem));
    }

    var boardWidth = 600;
    var boardHeight = 600;
    var fileCount = 8;
    var rankCount = 8;

    var squareWidth = boardWidth / fileCount;
    var squareHeight = boardHeight / rankCount;

    var borderColor = "#EEE";
    var pathColor = "#FFBF00";
    var fillColor = "#666";
    var timeout = 400;

    var paper = Raphael("canvas", boardWidth, boardHeight);

    function createBoard() {
        var board = paper.rect(0, 0, boardWidth, boardHeight);
        board.attr({
            "stroke": borderColor,
            "stroke-width": 4
        });
        return board;
    }

    function createPath() {
        var path = paper.path();
        path.attr({
            "arrow-end": "block-wide-long",
            "stroke": pathColor,
            "stroke-linecap": "round",
            "stroke-width": 3
        });
        return path;
    }

    function createStartPoint(square) {
        var center = centerOf(square);
        var point = paper.circle(center.x, center.y, 6);
        point.attr({
            "fill": pathColor,
            "stroke": pathColor,
            "stroke-width": 3
        });
        return point;
    }

    function createSquares() {
        var squares = [];
        for (var file = 0; file < fileCount; file++) {
            squares[file] = [];

            for (var rank = 0; rank < rankCount; rank++) {
                var x = squareWidth * file;
                var y = squareHeight * (rankCount - rank -1);

                var square = paper.rect(x, y, squareWidth, squareHeight);
                square.attr({
                    "stroke": borderColor,
                    "stroke-width": 2
                });
                squares[file][rank] = square;
            }
        }
        return squares;
    }

    var squares = createSquares();
    var board = createBoard();
    var path = createPath();
    var startPoint = null;

    var tour = {};
    var index = 0;

    var lastSquare = null;
    var currSquare = null;

    function resetSquares() {
        for (var i = 0; i < squares.length; i++) {
            for (var j = 0; j < squares[i].length; j++) {
                squares[i][j].attr("fill", "");
            }
        }
    }

    function resetAll() {
        path.attr("path", "");
        resetSquares();
        if (startPoint !== null) {
            startPoint.remove();
        }

        tour = {};
        index = 0;

        lastSquare = null;
        currSquare = null;
    }

    function fillSquare(square) {
        square.attr("fill", fillColor);
    }

    function runAnimation() {
        var file = tour[index][0];
        var rank = tour[index][1];
        currSquare = squares[file][rank];

        if (lastSquare === null) {
            startPoint = createStartPoint(currSquare);
        }
        fillSquare(currSquare);
        addLine(path, lastSquare, currSquare);

        setTimeout(loopAnimation, timeout);
    }

    function loopAnimation() {
        lastSquare = currSquare;
        ++index;

        if (index < tour.length) {
            runAnimation();
        } else {
            setTimeout(queryTour, timeout);
        }
    }

    function queryTour() {
        $.getJSON("api/knightstour/warnsdorff.json", function(jsonTour) {
            resetAll();
            tour = eval(jsonTour);
            runAnimation();
        });
    }
    queryTour();
};

$(document).ready(knightstour);

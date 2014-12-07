# Equites, a Scala chess playground
[![Build Status](https://travis-ci.org/fthomas/equites.svg?branch=master)](https://travis-ci.org/fthomas/equites)
[![Coverage Status](https://img.shields.io/coveralls/fthomas/equites.svg)](https://coveralls.io/r/fthomas/equites?branch=master)

Equites is a bunch of chess related code mostly implemented in Scala.
Currently there is little usable from a chess player's point of view.
But it can animate random [Knight's tours][Equites] :-) and [talk with
chess engines][UciEngineVsItself] via the [Universal Chess Interface (UCI)][UCI].

[Equites]: http://equites.timepit.eu/
[UCI]: http://en.wikipedia.org/wiki/Universal_Chess_Interface
[UciEngineVsItself]: https://github.com/fthomas/equites/blob/master/cli/src/main/scala/eu/timepit/equites/cli/UciEngineVsItself.scala

## Web resources

Equites also provides some free to use web resources:

* Convert [FEN] placements to other formats:
 * [/api/fen/figurine/]
 * [/api/fen/letter/]
 * [/api/fen/wiki/]

[FEN]: http://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
[/api/fen/figurine/]: http://equites.timepit.eu/api/fen/figurine/rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR
[/api/fen/letter/]: http://equites.timepit.eu/api/fen/letter/rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR
[/api/fen/wiki/]: http://equites.timepit.eu/api/fen/wiki/rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR

## Download

You can download the Equites sources in either [tar.gz][] or [zip][] formats.

[tar.gz]: https://github.com/fthomas/equites/tarball/master
[zip]:    https://github.com/fthomas/equites/zipball/master

The version control system used for development of Equites is Git. The [Git
repository][] can be inspected and browsed online at [GitHub][] and it can
be cloned by running:

    git clone git://github.com/fthomas/equites.git

[Git repository]: http://github.com/fthomas/equites
[GitHub]: http://github.com/

## License

Equites is [free software][] and licensed under the [GPLv3][]. The full text
of the license can be found in the file [`COPYING`][COPYING] in Equites'
source tree.

[free software]: http://www.gnu.org/philosophy/free-sw.html
[GPLv3]: http://www.gnu.org/licenses/gpl-3.0.html
[COPYING]: https://github.com/fthomas/equites/blob/master/COPYING

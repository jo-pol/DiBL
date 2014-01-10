//  Copyright 2014, J. Pol
//
// This file is part of free software: you can redistribute it and/or modify it under the terms of the
// GNU General Public License as published by the Free Software Foundation.
// 
// This package is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
// the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
// 
// See the GNU General Public License for more details. A copy of the GNU General Public License is
// available at <http://www.gnu.org/licenses/>.

function diagonalDimensions(max) {

	f = document.getElementById('diagonalPattern');
	f.max = max;
	f.value = 1;
	document.getElementById('diagonalNr').innerHTML = 1;
}

function diagonalRange(value) {

	document.getElementById('diagonalNr').innerHTML = value;
}

function interleavedDimensions(max) {

	f = document.getElementById('interleavedPattern');
	f.max = max;
	f.value = 1;
	document.getElementById('interleavedNr').innerHTML = 1;
}

function interleavedRange(value) {

	document.getElementById('interleavedNr').innerHTML = value;
}

function showFlanders() {

	// export PATH=$PATH:/C/Program\ Files/Java/jre6/bin

	document.getElementById('mu').innerHTML
	= "java -jar dibl-cmd-0.1.2.jar -ext "
	+ document.flanders.ext.value + " '3;2\n"
	+ document.flanders.A1.value + ";"
	+ document.flanders.B1.value + "\n"
	+ document.flanders.A2.value + ";"
	+ document.flanders.B2.value + "\n"
	+ document.flanders.A3.value + ";"
	+ document.flanders.B3.value + "'"
	+ " < input/flanders.svg"
	+ " > output." + document.flanders.ext.value
	;

	document.getElementById('win').innerHTML
	= "java -jar dibl-cmd-0.1.2.jar 3;2^\r\n\r\n"
	+ document.flanders.A1.value + ";"
	+ document.flanders.B1.value + "^\r\n\r\n"
	+ document.flanders.A2.value + ";"
	+ document.flanders.B2.value + "^\r\n\r\n"
	+ document.flanders.A3.value + ";"
	+ document.flanders.B3.value
	+ " < input/flanders.svg"
	+ " > output." + document.flanders.ext.value
	;
}

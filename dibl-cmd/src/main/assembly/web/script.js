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

function diagramTypeChanged (){

	var newDiagramType = document.diagramConfig.diagramType.value;
	var newDisplayValue = newDiagramType == '-pair'? 'block' : 'none';

	var elements = document.getElementsByClassName('pair');
	for (var i=0 ; i<elements.length ; i++ ) {
		elements[i].style.display = newDisplayValue;
	}
}

function setRangeMax(max) {

	f = document.diagramConfig.pattern;
	f.value = 1;
	f.max = max;
	document.getElementById('nr').innerHTML = 1;
	document.getElementById('max').innerHTML = max;
}

function showRangeValue() {

	document.getElementById('nr').innerHTML = document.diagramConfig.pattern.value;
}

function showFlanders() {

	// add to .bashrc on Windows something like:
	// export PATH=$PATH:/C/Program\ Files/Java/jre6/bin

	var io = "< input/flanders.svg > diagram." 
	+ document.diagramConfig.ext.value
	;
	var command = "java -jar dibl-cmd-0.1.2.jar -ext "
	+ document.diagramConfig.ext.value
	;
	document.getElementById('sh').innerHTML
	= command + " '3;2\n"
	+ document.diagramConfig.A1.value + ";"
	+ document.diagramConfig.B1.value + "\n"
	+ document.diagramConfig.A2.value + ";"
	+ document.diagramConfig.B2.value + "\n"
	+ document.diagramConfig.A3.value + ";"
	+ document.diagramConfig.B3.value + "' " + io
	;
	document.getElementById('bat').innerHTML
	= command + " 3;2^\r\n\r\n"
	+ document.diagramConfig.A1.value + ";"
	+ document.diagramConfig.B1.value + "^\r\n\r\n"
	+ document.diagramConfig.A2.value + ";"
	+ document.diagramConfig.B2.value + "^\r\n\r\n"
	+ document.diagramConfig.A3.value + ";"
	+ document.diagramConfig.B3.value + " " + io
	;
}

function showPairTraversal() {

	// add to .bashrc on Windows something like:
	// export PATH=$PATH:/C/Program\ Files/Java/jre6/bin

	var command = "java -jar dibl-cmd-0.1.2.jar "
	+ document.diagramConfig.options.value + " -ext " 
	+ document.diagramConfig.ext.value
	;
	var templatePath = " input/PairTraversal/"
	+ document.diagramConfig.traversalType.value + "/"
	+ document.diagramConfig.dimensions.value
	;
	var io = " <" + templatePath 
	+ document.diagramConfig.diagramType.value  + ".svg > diagram."
	+ document.diagramConfig.ext.value
	;
	var pattern = templatePath + "/"
	+ document.diagramConfig.dimensions.value + "_"
	+ document.diagramConfig.pattern.value + ".txt"
	;
	var r1 
	= document.diagramConfig.A1.value + ";"
	+ document.diagramConfig.B1.value + ";"
	+ document.diagramConfig.C1.value + ";"
	+ document.diagramConfig.D1.value
	;
	var r2
	= document.diagramConfig.A2.value + ";"
	+ document.diagramConfig.B2.value + ";"
	+ document.diagramConfig.C2.value + ";"
	+ document.diagramConfig.D2.value
	;
	var r3
	= document.diagramConfig.A3.value + ";"
	+ document.diagramConfig.B3.value + ";"
	+ document.diagramConfig.C3.value + ";"
	+ document.diagramConfig.D3.value
	;
	var r4
	= document.diagramConfig.A4.value + ";"
	+ document.diagramConfig.B4.value + ";"
	+ document.diagramConfig.C4.value + ";"
	+ document.diagramConfig.D4.value
	;
	document.getElementById('sh').innerHTML
	= command + " '4;4\n"
	+ r1 + "\n"
	+ r2 + "\n"
	+ r3 + "\n"
	+ r4 + "'" + pattern + io
	;
	document.getElementById('bat').innerHTML
	= command + " 4;4^\r\n\r\n"
	+ r1 + "^\r\n\r\n"
	+ r2 + "^\r\n\r\n"
	+ r3 + "^\r\n\r\n"
	+ r4 + pattern + io
	;
}

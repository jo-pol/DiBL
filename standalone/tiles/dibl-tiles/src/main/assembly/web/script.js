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

var command = "java -jar dibl-cmd-${project.version}.jar -ext ";

function diagramTypeChanged (cfg){

	showPairTraversal(cfg);

	var newDiagramType = cfg.diagramType.value;
	var newDisplayValue = newDiagramType == '-pair'? 'block' : 'none';

	var elements = document.getElementsByClassName('pair');
	for (var i=0 ; i<elements.length ; i++ ) {
		elements[i].style.display = newDisplayValue;
	}
}

function setRangeMax(cfg) {

	dim = cfg.dimensions;
	iHTML = dim.options[dim.selectedIndex].innerHTML;
	cfg.pattern.value = 1;
	cfg.pattern.max = parseInt(iHTML.split(":")[1]);

	showPairTraversal(cfg);
}

function showFlanders() {

	// add to .bashrc on Windows something like:
	// export PATH=$PATH:/C/Program\ Files/Java/jre6/bin

	var cfg = document.flandersConfig;
	var io = "< input/flanders.svg > diagram." 
	+ cfg.ext.value
	;
	document.getElementById('sh').innerHTML
	= command + cfg.ext.value + " '3;2\n"
	+ cfg.A1.value + ";"
	+ cfg.B1.value + "\n"
	+ cfg.A2.value + ";"
	+ cfg.B2.value + "\n"
	+ cfg.A3.value + ";"
	+ cfg.B3.value + "' " + io
	;
	document.getElementById('bat').innerHTML
	= command + cfg.ext.value + " 3;2^\r\n\r\n"
	+ cfg.A1.value + ";"
	+ cfg.B1.value + "^\r\n\r\n"
	+ cfg.A2.value + ";"
	+ cfg.B2.value + "^\r\n\r\n"
	+ cfg.A3.value + ";"
	+ cfg.B3.value + " " + io
	;
}

function showPairTraversal(cfg) {

	// add to .bashrc on Windows something like:
	// export PATH=$PATH:/C/Program\ Files/Java/jre6/bin

	var templatePath = " input/PairTraversal/"
	+ cfg.traversalType.value + "/"
	+ cfg.dimensions.value
	;
	var io = " <" + templatePath 
	+ cfg.diagramType.value  + ".svg > diagram."
	+ cfg.ext.value
	;
	var pattern = templatePath + "/"
	+ cfg.dimensions.value + "_"
	+ cfg.pattern.value + ".txt"
	;
	var r1 
	= cfg.A1.value + ";"
	+ cfg.B1.value + ";"
	+ cfg.C1.value + ";"
	+ cfg.D1.value
	;
	var r2
	= cfg.A2.value + ";"
	+ cfg.B2.value + ";"
	+ cfg.C2.value + ";"
	+ cfg.D2.value
	;
	var r3
	= cfg.A3.value + ";"
	+ cfg.B3.value + ";"
	+ cfg.C3.value + ";"
	+ cfg.D3.value
	;
	var r4
	= cfg.A4.value + ";"
	+ cfg.B4.value + ";"
	+ cfg.C4.value + ";"
	+ cfg.D4.value
	;
	document.getElementById('sh').innerHTML
	= command + cfg.ext.value + " "  + cfg.options.value + " '4;4\n"
	+ r1 + "\n"
	+ r2 + "\n"
	+ r3 + "\n"
	+ r4 + "'" + pattern + io
	;
	document.getElementById('bat').innerHTML
	= command + cfg.ext.value + " " + cfg.options.value + " 4;4^\r\n\r\n"
	+ r1 + "^\r\n\r\n"
	+ r2 + "^\r\n\r\n"
	+ r3 + "^\r\n\r\n"
	+ r4 + pattern + io
	;
}

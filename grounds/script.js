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
var command = 'java -jar dibl-tiles-0.1.3.jar -ext ';
function init() {
  diagramTypeChanged(document.tab2form);
  showFlanders()
}
function diagramTypeChanged() {
  var cfg = getForm();
  showPairTraversal(cfg);
  var newDisplayValue = cfg.diagramType.value != '-pair';
  var elements = document.getElementsByClassName('pair');
  for (var i = 0; i < elements.length; i++) {
    elements[i].disabled = newDisplayValue;
  }
}
function setRangeMax() {
  var cfg = getForm();
  var dim = cfg.dimensions;
  var iHTML = dim.options[dim.selectedIndex].innerHTML;
  cfg.pattern.value = 1;
  cfg.pattern.max = parseInt(iHTML.split(':') [1]);
  var tab = location.hash.substring(1);
  var cols = iHTML.split(' ') [0].split('x') [0];
  var rows = iHTML.split(' ') [0].split('x') [1];
  cleanup(tab, 'hide', 'hide');
  cleanup(tab, 'show', 'show');
  for (var i = 0; i < 4; i++) {
    var colClass = tab + 'col' + ((i * 1) + 1);
    var rowClass = tab + 'row' + ((i * 1) + 1);
    var colElements = document.getElementsByClassName(colClass);
    var rowElements = document.getElementsByClassName(rowClass);
    for (var j = 0; j < 4; j++) {
      var c = colElements[j];
      var r = rowElements[j];
      c.className += ' ' + tab + (j < cols ? 'show' : 'hide');
      r.className += ' ' + tab + (j < rows ? 'show' : 'hide');
    }
  }
  cleanup(tab, 'hide', 'show');
  //cleanup(tab,"hide ");
  showPairTraversal(cfg);
}
function cleanup(tab, classToSearch, classToRemove)
{
  var replace = new RegExp(tab + classToRemove, 'g');
  var hiddenElements = document.getElementsByClassName(tab + classToSearch);
  for (i = hiddenElements.length; i > 0; ) {
    var e = hiddenElements[--i];
    e.className = e.className.replace(replace, '');
  }
}
function getForm()
{
  return location.hash == '#tab2' ? document.diagonalConfig : document.interleavedConfig;
}
function showFlanders() {
  // add to .bashrc on Windows something like:
  // export PATH=$PATH:/C/Program\ Files/Java/jre6/bin
  var cfg = document.flandersConfig;
  var io = '< input/flanders.svg > diagram.'
  + cfg.ext.value
  ;
  document.getElementById('sh').innerHTML
  = command + cfg.ext.value + ' \'3;2\n'
  + cfg.A1.value + ';'
  + cfg.B1.value + '\n'
  + cfg.A2.value + ';'
  + cfg.B2.value + '\n'
  + cfg.A3.value + ';'
  + cfg.B3.value + '\' ' + io
  ;
  document.getElementById('bat').innerHTML
  = command + cfg.ext.value + ' 3;2^\r\n\r\n'
  + cfg.A1.value + ';'
  + cfg.B1.value + '^\r\n\r\n'
  + cfg.A2.value + ';'
  + cfg.B2.value + '^\r\n\r\n'
  + cfg.A3.value + ';'
  + cfg.B3.value + ' ' + io + '\r\nPAUSE'
  ;
}
function showPairTraversal() {
  // add to .bashrc on Windows something like:
  // export PATH=$PATH:/C/Program\ Files/Java/jre6/bin
  var cfg = getForm();
  var templatePath = ' input/PairTraversal/'
  + cfg.traversalType.value + '/'
  + cfg.dimensions.value
  ;
  var io = ' <' + templatePath
  + cfg.diagramType.value + '.svg > diagram.'
  + cfg.ext.value
  ;
  var pattern = templatePath + '/'
  + cfg.dimensions.value + '_'
  + cfg.pattern.value + '.txt'
  ;
  var r1
  = cfg.A1.value + ';'
  + cfg.B1.value + ';'
  + cfg.C1.value + ';'
  + cfg.D1.value
  ;
  var r2
  = cfg.A2.value + ';'
  + cfg.B2.value + ';'
  + cfg.C2.value + ';'
  + cfg.D2.value
  ;
  var r3
  = cfg.A3.value + ';'
  + cfg.B3.value + ';'
  + cfg.C3.value + ';'
  + cfg.D3.value
  ;
  var r4
  = cfg.A4.value + ';'
  + cfg.B4.value + ';'
  + cfg.C4.value + ';'
  + cfg.D4.value
  ;
  document.getElementById('sh').innerHTML
  = command + cfg.ext.value + ' ' + cfg.options.value + ' \'4;4\n'
  + r1 + '\n'
  + r2 + '\n'
  + r3 + '\n'
  + r4 + '\'' + pattern + io
  ;
  document.getElementById('bat').innerHTML
  = command + cfg.ext.value + ' ' + cfg.options.value + ' 4;4^\r\n\r\n'
  + r1 + '^\r\n\r\n'
  + r2 + '^\r\n\r\n'
  + r3 + '^\r\n\r\n'
  + r4 + pattern + io + '\r\nPAUSE'
  ;
}
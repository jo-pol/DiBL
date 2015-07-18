<!-- saved from url=(0014)about:internet -->
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
function templateChanged(formField) {
  var cfg = formField.form;
  cfg.pattern.value = 0;
  var cols = formField.value.split('-') [1].split('x') [0];
  var rows = formField.value.split('-') [1].split('x') [1];
  cleanup("", 'hide', 'hide');
  cleanup('hide', 'hide');
  cleanup('show', 'show');
  for (var i = 0; i < 4; i++) {
    var colClass = 'col' + ((i * 1) + 1);
    var rowClass = 'row' + ((i * 1) + 1);
    var colElements = document.getElementsByClassName(colClass);
    var rowElements = document.getElementsByClassName(rowClass);
    for (var j = 0; j < 4; j++) {
      var c = colElements[j];
      var r = rowElements[j];
      c.className += ' ' + (j < cols ? 'show' : 'hide');
      r.className += ' ' + (j < rows ? 'show' : 'hide');
    }
  }
  cleanup('hide', 'show');
  
  if (formField.selectedOptions != undefined)
    cfg.pattern.max = parseInt(formField.selectedOptions[0].innerHTML.split(':') [1]);
  else // IE
    cfg.pattern.max = parseInt(formField[formField.selectedIndex].innerHTML.split(':') [1]);
}
function cleanup(classToSearch, classToRemove)
{
  var replace = new RegExp(classToRemove, 'g');
  var hiddenElements = document.getElementsByClassName(classToSearch);
  for (i = hiddenElements.length; i > 0; ) {
    var e = hiddenElements[--i];
    e.className = e.className.replace(replace, '');
  }
}

//  Copyright 2014, J. Pol
//
// This file is part of free software: you can redistribute it and/or modify it under the terms of the
// GNU General Public License as published by the Free Software Foundation.
// 
// This package is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
// the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
// 
// See the GNU General Public License for more details. A copy of the GNU General Public License is
// available at http://www.gnu.org/licenses/gpl.html
function templateChanged(formField) {
  var cfg = formField.form;
  cfg.pattern.value = 0;
  var rows = formField.value.split('-') [1].split('x') [0];
  var cols = formField.value.split('-') [1].split('x') [1];
  for (var i = 0; i < 4; i++) {
    for (var j = 0; j < 4; j++) {
      var id = 'row' + ((i * 1) + 1) + '-' + 'col' + ((j * 1) + 1);
      document.getElementById(id).disabled = i >= rows || j >= cols;
    }
  }
  
  if (formField.selectedOptions != undefined)
    cfg.pattern.max = parseInt(formField.selectedOptions[0].innerHTML.split(':') [1]);
  else // IE
    cfg.pattern.max = parseInt(formField[formField.selectedIndex].innerHTML.split(':') [1]);
}

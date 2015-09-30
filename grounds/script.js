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

function fillForm() {
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        var fields = document.getElementsByName(key);
        if (fields.length > 0) {
            fields[0].value = value;
        }
    });
}

function loadDiagram(content) {
    var xhr = new XMLHttpRequest();
    var generator = dibl.Ground().main(window.console, window.location.href);

    xhr.onreadystatechange = function() {
        if (xhr.readyState == XMLHttpRequest.DONE ) {
            if(xhr.status != 200)
                content.innerHTML  += generator.templateURI + ' status: ' + xhr.status;
            else {
                // adding/stripping <html><body> required for IE/Safari, it hardly hurts FF/Chrome
                var svgDocString = "<html><body>"+xhr.responseText+"</body></html>"
                var svgDoc = new DOMParser().parseFromString(svgDocString, "text/html");
                generator.apply(svgDoc);
                var newSvgDocString = svgDoc.getElementsByTagName("body")[0].innerHTML

                if (document.documentURI == undefined) // IE; TODO need another feature check
                    content.innerHTML  = newSvgDocString;
                else // Chrome, Firefox, safari:
                    content.innerHTML  = "<img src='data:image/svg+xml,"+encodeURIComponent(newSvgDocString)+"' download='diagram.svg'/>";
           }
        }
    }
    xhr.open("GET", generator.templateURI, true);
    xhr.send();
    content.innerHTML  += "loading diagram... "
}


function templateChanged(formField) {
  var rows = formField.value.split('-') [1].split('x') [0];
  var cols = formField.value.split('-') [1].split('x') [1];
  for (var i = 0; i < 4; i++) {
    for (var j = 0; j < 4; j++) {
      var id = 'row' + ((i * 1) + 1) + '-' + 'col' + ((j * 1) + 1);
      document.getElementById(id).disabled = i >= rows || j >= cols;
    }
  }
  
  var cfg = formField.form;
  if (formField.selectedOptions != undefined)
    cfg.pattern.max = parseInt(formField.selectedOptions[0].innerHTML.split(':') [1]);
  else // IE
    cfg.pattern.max = parseInt(formField[formField.selectedIndex].innerHTML.split(':') [1]);
  if (cfg.pattern.max*1 < cfg.pattern.value*1)
    cfg.pattern.value = 0;
  hideStitcheTypes(formField.value)
}

function hideStitcheTypes(template)
{
  var elements = document.getElementsByClassName("hide");
  for (i = elements.length; i > 0; ) {
    el = elements[--i]
    el.className = el.className.replace(" hide", "");
  }
  if (template.split('-')[0] == "flanders") {
    hide("pin");
    if (template.split('-')[2] == "pair")
      hide("flanders");
  } else {
    hide("flanders");
    if (template.split('-')[2] == "thread")
      hide("pair");
  }
}

function hide (className) {
  var elements = document.getElementsByClassName(className);
  for (i = elements.length; i > 0; ) {
    elements[--i].className+= " hide";
  }
}

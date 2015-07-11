var xhr = new XMLHttpRequest();
var content = document.getElementById("message");
var generator;
if (document.documentURI == undefined) // IE
    generator = dibl.Ground().main(window.console, ""+document.documentURI);
else // Chrome, Firefox, safari:
    generator = dibl.Ground().main(window.console, ""+window.location.search);

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

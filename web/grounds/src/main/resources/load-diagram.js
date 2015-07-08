var xhr = new XMLHttpRequest();
var content = document.getElementById("message");
var generator;
if (document.documentURI != undefined)
    generator = dibl.Ground().main(window.console, ""+document.documentURI);
else // IE:
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
           content.innerHTML  = "<p>To save the diagram as an SVG file for InkScape, Illustrator or whatever:<br/> "
                + "Firefox, Chrome, Safari: right-click, save link as;<br/> Internet explorer: right-click, save image as</p>"
                + "<a href='data:application/octet-stream,"+encodeURIComponent(newSvgDocString)+"' download='diagram.svg'>"
                + newSvgDocString
                + "</a>";
           // TODO omit link for IE but how to test for not supporting data-uri as href?
       }
    }
}
xhr.open("GET", generator.templateURI, true);
xhr.send();
content.innerHTML  += "loading diagram... "

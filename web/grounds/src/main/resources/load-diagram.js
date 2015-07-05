var xmlhttp = new XMLHttpRequest();
var content = document.getElementById("message");
var generator;
if (document.documentURI != undefined)
    generator = dibl.Ground().main(window.console, ""+document.documentURI);
else // IE:
    generator = dibl.Ground().main(window.console, ""+window.location.search);

//http://stackoverflow.com/questions/8567114/how-to-make-an-ajax-call-without-jquery
xmlhttp.onreadystatechange = function() {
    if (xmlhttp.readyState == XMLHttpRequest.DONE ) {
       content.innerHTML  += "configuring diagram... ";
       if(xmlhttp.status != 200)
           content.innerHTML  += generator.templateURI + ' status: ' + xmlhttp.status;
       else {
           var svgDoc = new DOMParser().parseFromString(xmlhttp.responseText, "image/svg+xml");
           generator.apply(svgDoc);
           if (svgDoc.documentElement.outerHTML != undefined) {
                // FF/Chrome
                content.innerHTML  = svgDoc.documentElement.outerHTML;
           } else {
               // safari/IE: fallback, a saved page will be an unmodified template
               document.write(xmlhttp.responseText)
               generator.apply(document) // IE now throws "access denied" when trying to log
               document.close()
           }
       }
}
}
xmlhttp.open("GET", generator.templateURI, true);
content.innerHTML  += "loading diagram... "
xmlhttp.send();

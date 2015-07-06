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
           // surrounding with <html><body> not required for FF/Chrome and mime-type text/svg+xsml
           var svgDoc = new DOMParser().parseFromString("<html><body>"+xmlhttp.responseText+"</body></html>", "text/html");
           generator.apply(svgDoc);
           content.innerHTML  = svgDoc.documentElement.innerHTML;
       }
    }
}
xmlhttp.open("GET", generator.templateURI, true);
content.innerHTML  += "loading diagram... "
xmlhttp.send();

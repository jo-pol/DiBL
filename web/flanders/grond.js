function cijferreeks (x) {

   window.location = 'grond/knipling/'
	+ ''+document.SlagenKeuzes.selA.selectedIndex
	+ ''+document.SlagenKeuzes.selB.selectedIndex
	+ ''+document.SlagenKeuzes.selC.selectedIndex
	+ ''+document.SlagenKeuzes.selD.selectedIndex
	+ ''+document.SlagenKeuzes.selE.selectedIndex
	+ ''+document.SlagenKeuzes.selF.selectedIndex
	+ '.lac'
	;

}
function ToonHerhalingen (s) {

   if (document.SlagenKeuzes.KleurCodes.value.length != 8) {
	alert ('NL: geef 8 tekens op die elk een kleur verbeelden\n'
		,'EN: enter 8 symbols, each representing a coulour\n'
		);
	return;
   }
   var n = document.SlagenKeuzes.KleurCodes.value;
   var x = 'XXXXXXXX';
   var l = '\\\\\\\\\\\\\\\\';
   var r = '////////';
   var s1 = n+n+n+n+n+n+n+n+n+n+n+n+n+n+n;
   var s2 = x+x+x+x+x+x+x+n+x+x+x+x+x+x+x;
   var s2 = l+l+l+l+l+l+l+n+r+r+r+r+r+r+r;
   var min = 0;
   var max = s1.length;
   //alert ( ((s1.length)) +' '+ ((s1.length/2)) +' '+ ((s1.length/2)-4) +' '+ ((s1.length/2)+4) );
   var slagen 
	= '00: '
	+ s1.substring((s1.length/2)-4,(s1.length/2)+4)+' '
	+ s2.substring((s1.length/2)-4,(s1.length/2)+4)
	;
   while ( min<max ) {
	//alert (min+' '+max+' '+s2);   
	var s1n = s1.substring(0,min);
	var s2n = s2.substring(0,min);
	var i;
	for ( i=min ; i < max ; i = (i*1)+8 ) {
	   var j;
	   for ( j=0 ; j < 8 ; j = j+1 ) {
		var p = s.substring(j,(j*1)+1);
		p = (p*1)+i-1;
		s1n = s1n + s1.substring(p,(p*1)+1);
		s2n = s2n + s2.substring(p,(p*1)+1);
	   }
	}
	var s1 = s1n + s1.substring(max);
	var s2 = s2n + s2.substring(max);
	min = (min*1)+4;
	max = (max*1)-4;
      var s1x = s1.substring((s1.length/2)-4,(s1.length/2)+4);
	var s2x = s2.substring((s1.length/2)-4,(s1.length/2)+4);
 
	var slagen = slagen + '\n';
	if ( (min/4 < 10) ) {
		slagen = slagen + '0';
	}
	slagen = slagen + (min/4) + ':';
	if ( (s1x == '12345678') ) {
		slagen = slagen + '*';
	} else {
		slagen = slagen + ' ';
      }
	slagen = slagen + s1x + ' ' + s2x;
   }
   return (slagen);
}
function MaakBerekeningen () {
   var s = MaakSlag ('12345678');
   document.SlagenKeuzes.ToonHerhalingen.value 
	= ToonHerhalingen( s.substring((s.length*1)-8) );
}
function MaakEnkelvoudigeSlag (SelectList, DraadVolgorde) {

   var SlagCode = SelectList.options[SelectList.selectedIndex].value;
   var i1 = DraadVolgorde.substring(0,1);
   var i2 = DraadVolgorde.substring(1,2);
   var i3 = DraadVolgorde.substring(2,3);
   var i4 = DraadVolgorde.substring(3,4);
   var i;
   for ( i=0 ; i < SlagCode.length ; i = i+1 ) {
	var s = SlagCode.substring(i,i+1);
	if ( s == 'k') {
	   var x = i2;
	   i2 = i3;
	   i3 = x;
	}
	if ( s == 'l' || s == 'b' ) {
	   var x = i1;
	   i1 = i2;
	   i2 = x;
	}
	if ( s == 'r' || s == 'b' ) {
	   var x = i3;
	   i3 = i4;
	   i4 = x;
	}
   }
   var resultaat = '' + i1 + i2 + i3 + i4;
   return resultaat;
}
function MaakSlag (s) {

   var s1 =
	( MaakEnkelvoudigeSlag (document.SlagenKeuzes.sela, s.substring(0,4))
	+ MaakEnkelvoudigeSlag (document.SlagenKeuzes.selc, s.substring(4,8))
	);
   var s2 
	= s1.substring(0,2)
	+ MaakEnkelvoudigeSlag (document.SlagenKeuzes.selb, s1.substring(2,6))
	+ s1.substring(6,8)
	;
   var s3 =
	( MaakEnkelvoudigeSlag (document.SlagenKeuzes.seld, s2.substring(0,4))
	+ MaakEnkelvoudigeSlag (document.SlagenKeuzes.sele, s2.substring(4,8))
	);
   var s4
	= s3.substring(0,2)
	+ MaakEnkelvoudigeSlag (document.SlagenKeuzes.self, s3.substring(2,6))
	+ s3.substring(6,8)
	;
   return (s+'\n'+s1+'\n'+s2+'\n'+s3+'\n'+s4);
}
function vb (Plaatje, VolgNr) {

   Plaatje.src = 'grond/voorbeeld-' + VolgNr + '.jpg';
}
function ChangeAllImages () {

   ChangeImages(document.SlagenKeuzes.sela);
   ChangeImages(document.SlagenKeuzes.selb);
   ChangeImages(document.SlagenKeuzes.selc);
   ChangeImages(document.SlagenKeuzes.seld);
   ChangeImages(document.SlagenKeuzes.sele);
   ChangeImages(document.SlagenKeuzes.self);
}
function ChangeImages (SelectList) {
	var ImageCode1   = SelectList.name.substring(3);
	if ( ImageCode1 == 'A' ) {
		document.SlagenKeuzes.sela.selectedIndex = SelectList.selectedIndex;
		ChangeImages (document.SlagenKeuzes.sela);
	} else if ( ImageCode1 == 'C' ) {
		document.SlagenKeuzes.selc.selectedIndex = SelectList.selectedIndex;
		ChangeImages (document.SlagenKeuzes.selc);
	} else {
		if ( ImageCode1 == 'a' ) {
			document.SlagenKeuzes.selA.selectedIndex = SelectList.selectedIndex;
		} else if ( ImageCode1 == 'c' ) {
			document.SlagenKeuzes.selC.selectedIndex = SelectList.selectedIndex;
		}
		
		var ImageCode2   = ImageCode1 + "x";
		var KlosSlagCode = SelectList.options[SelectList.selectedIndex].value;
		var i;
		for ( i=0 ; i < document.images.length ; i++ ) {
			if (document.images[i].name.indexOf (ImageCode2) == 0) {
				document.images[i].src = (ImageCode2 + "-" + KlosSlagCode + ".gif").toUpperCase();
			}
			else if (document.images[i].name.indexOf (ImageCode1) == 0) {
				document.images[i].src = (ImageCode1 + "-" + KlosSlagCode + ".gif").toUpperCase();
			}
		}
	}
}

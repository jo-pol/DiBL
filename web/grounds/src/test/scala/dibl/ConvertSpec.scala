/*
 Copyright 2015 Jo Pol
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program. If not, see http://www.gnu.org/licenses/gpl.html dibl
*/

package dibl

import org.scalatest._

import scala.collection.Map
import scala.collection.immutable.HashMap

class ConvertSpec extends FlatSpec with Matchers {

  "brick-4x4" should "converts back and forth" in {
    val m = Graphs.get("brick-4x4-pair",0,M(R("")))
    pack( m ) should be ("=:0g:::0wuuu=<:g")
    Graphs.unpack( pack( m ),4,4 ) should equal (m)
  }

  "flanders-3x2" should "convert back and forth" in {
    val m: M = Graphs.get("flanders-3x2-pair",0,M(R("")))
    pack(m) should be ("4epNg=")
    Graphs.unpack(pack(m), m.length, m(0).length ) should equal (m)
  }

  "convert" should "produce packed matrices" in {
    for ((key,value)<-Graphs.matrixMap) {
      // TODO transform short tuples to long ones for diagonal
      print('"'+key+'"'+" -> Array(")
      for (m <- value) {
        if (key.startsWith("diagonal"))
          for {
            r <- m.indices
            c <- m(0).indices
          }{
            val s = m(r)(c).split(",")
            m(r)(c) = s"${s(0)},${s(1)},0,${s(2)},${s(3)},${s(4)},0,${s(5)}"
          }

        print('"' + pack(m)+'"'+',')
      }
      println(s")")
    }
  }
  val matrices = HashMap {
    // saves 150KB of scala source code, the generated JavaScript code is currently 374KB:
    // https://github.com/jo-pol/DiBL/blob/gh-pages/grounds/dibl-ground-opt.js
    "diagonal-3x3" -> Array("XmFT<X> w","mmFT<V@kk","mFm:RT=w@","mkFy2T@kV","F=yyF==yF","m0y>RV=wF","m@mRFT=u<")
    "interleaved-2x2" -> Array("Af:4","=gX ","::::",";3c ","44pp","<<ii","eeNN")
    "brick-4x4" -> Array("=:0g:::0wuuu=<:g","=:gw<<00gi=:=guu","=:<g<g=2k=0giwui","=:<g<g=2m=F iwiu","=:<gR >0:0::uwuu","=:<gR >2=0:guwui","=:<gR >2>0R uwiu","=:<gR @0:0g=uwiu","=:<gR @0:F >ukuu","=:<gR @2=0gkuwii","=:<gR @2=F mukui","=<<g m=F0:0:uwuu","=:gw<<02miV =giu",">:R ::0:::::uwuu",">:R ::0<>:R uwiu",">:R ::2::R >ukuu",">:R :<0:R >:iwuu",">:R :<0<X X iwiu",">:R :<2<V mkiwii",">:R <:0: >:Ruwui",">:R <:2: X Xukui",">:R <:2<kV mikui",">:R <<2<kkkkikii","=:gw<<22kikk=gii",">:R wwkw2222ikii","><R  >0R0:::uwuu","><R  >2R0R >ukuu","=g=g:0:0::::uwuw","=g=g:0:2>:R uwiw","=g=g:0<0:R >ukuw","=g=g:0<2=R mukuk","=g=g:2:2=g=gukuk","=g=g:2:2X X iwiw","=g=g:2<0R m=iwiw","=:gwwwkk2022=gii","=g=g<0<0g=g=iwiw","=RXywVXm2FX>TX>g","=RXyyV@F@FmTTX>g","=RXyyV>F@F@RTmTm","=RXyyV@F@0FXTyTm","=RXyRX>0RX@:TmTy","=RXyRX@0R>F>TyTy","=RXyRX@2V>FmTyTm",">R>RyVyV@F@FTmTm","=FXywVXm2FX@TXmk","=::g:::0uuuu:<::","=FXyyV>F@F>RTXyk","=:gk::00::::wwuu","=:gk::02>:R wwiu","=:gk::20:R >wkuu","=:gk::22=:gkwwii","=:gk::22=R mwkui","=:gk:<00R >:kwuu","=:gk:<02=g=gwkui","=:gk:<02>gV wkiu","=:gk:<02V >gkwui","=:<gR >0uiuu:<::","=:gk:<02X X kwiu","=:gk:<20:gk=wkiu","=:gk<:00 >:Rwwui","=:gk<:20 >gVwwii","=:gk<:20 X Xwkui","=R m:0:0::::wwuu","=R m:0:2>:R wwiu","=R m:0<0:R >wkuu","=R m:0<2=R mwkui","=R m:2:0R >:kwuu","=<<g m=Fiuiu:<::","=R m:2:2>gV wkiu","=R m:2:2V >gkwui","=R m:2:2X X kwiu","=R m:2<0R m=kwiu","=R m:2<2V mkkwii","=R m<0:0 >:Rwwui","=R m<0<0gV >kkuu","=R m<0<0 >gVwwii","=R m<0<0 X Xwkui","=R m<0<2kV mkkui",">:R ::0:uuuu:<::","=R m<2:2mkV kkiu","=R m<2<0 mkVwkii","=R m<2<2kkkkkkii",">:FX:RX@VyVmVmTm",">:FX<RX@kTXmVyTm",">:FXywTX@F@RVmTm",">:FXywVX@0FXVyTm",">:FXRyT>RX@:VmTy",">:FXRyV>R>F>VyTy",">:FXRyV@V>FmVyTm","=g=g:0:0uuuu:<:<","><FX XXXFyV>VmTy","=RXmuTXm@RyVX@0F","=RXmuVXm@gTXX@:F","=RXmwTXmFXy=X@0R","=RXmwVXmFmT>X@:R","=RXmyT>FVXywX@0F",">R>FyVyVVmTmX@:F",">F>FyTyTyVyVF@F@",">F>FyTyVTXykR@F@",">F>FyVyVTmTmR@R@","=0<gR >0wiuu><R ",">F@FyiTXTXywR@F@","=RXmuTXm@<RXmV>F","=RXmwTXmF@R>mV>R","=RXmwVXm0FX>yV>R","=RXmyT>FV@RymV>F","=RXmyT@FX@gTmV>R","=RXmyV>F=FXyyV>F",">R>FyTyTR@R>mV>R",">R>FyTyVV@RmmV>F",">R@FyiTXV@RymV>F","=:0guuuk@:R =<0g","=RXyuTXm@:RX>R>F",">R@FRmV>yVmTmV>R","=RXmyT>FX@<RmkTX","=RXmyV>F>F@RykTX",">R>FR>R@T>RmywTX",">R>FyVyV>F@FykTX",">R@F:FX@TyVmykTX",">R@FykTX>F@RykTX",">F>FyVyV>F>FyVyV",">:RX:RX>ywVXFmT>",">:RX:RX@TyVmRmT>","=RXyuVXm@gVX>F>F",">:RX<RX@iTXmRyT>",">:RXyuTXX@<RFmT>",">:RXywTX>F@RRmT>",">:RXywVX>0FXRyT>",">:RXRyT>yV@RFmT>","><RX XXXmwVXFmT>",">RyTyV>R>F@RRmT>",">:RX<RX@mT>FXyuT","><RX XXXFyV@XmiT","XXXXXXXXXXXXXXXX","=RXywTXmF>R>>R>R",">FyTyV@R@FmTXXX ",":2:2 X XkukuX X ","<0<0X X wiwiX X ","=RXywVXmFmV>>F>R","=RXyyT>FV>Ry>R>F","=RXyyT@FX>gT>R>R","=RXyyV>FVmVy>F>F","=RXyyV@FXmkT>F>R","=RXyRX>0wTXy>R>F","=RXyRX@0yTmT>R>R","=:0guuwi<:g==<0g",">R>RyTyTR>R>>R>R",">R>RyTyVV>Rm>R>F",">R>RyVyVVmVm>F>F","=RXyyT>FX><R>gTX","=FXyuTXm>:RX:RX@","=FXyuVXmX XX0RX@","=FXywVXmmkVX0FX@","=FXyyT>FT>Ry:RX@","=FXywTXmF>R@>RmV","=FXywVXmFmV@>FmV","=:0guuwi<R >=2:g","=FXyyT>FX>:R>RyV","=FXyyT@FXX X>FyV","=:gk::00uuuu<<::","=R m:0:0uuuu<<::",">:FXyuTXV>Ry@R>F",">:FXywTXVmVy@F>F",">:FXRyT>wTXy@R>F",">:FXRyV>yTmT@R>R",">0FXyuTXT>Ry<RX@",">0FXywTXTmVy<FX@","=:0guwuk@gV =20g",">0FXyuVXXX X@FyV",">0FXRyT>yT>R@RyV",">0FXRyV>yTmV@RmV",">2FX XXXmuTX@RyV","::::::::::::uuuu",":::::<:<X X iuiu","::::<<<<kkkkiiii","::::wwww2222iiii",":<:< X X0:0:uuuu","<<<<0000wwwwiiii","=:0guwwi<gk==20g","<<<<2222iiiiuuuu","<<<<iiii<<<<iiii","=::g:::0::::uwuu","=::g:::2=::guwui","=::g:::2>:R uwiu","=::g::<0::g=uwiu","=::g::<0:R >ukuu","=::g::<2=:gkuwii","=::g::<2=R mukui","=::g:<:0:g=:ukuu","=:guww=g202<=gii","=::g:<:0R >:iwuu","=::g:<:2=g=gukui","=::g:<:2>gV ukiu","=::g:<:2V >giwui","=::g:<:2X X iwiu","=::g:<<0:gk=ukiu","=::g:<<0R m=iwiu","=::g:<<2=gkkukii","=::g:<<2V mkiwii","=::g<::0g=::iwuu","=:gw<:20gT >=guu","=::g<::0 >:Ruwui","=::g<::2k=:giwui","=::g<::2m=R iwiu","=::g<:<0g=g=iwiu","=::g<:<0gV >ikuu","=::g<:<0 >gVuwii","=::g<:<0 X Xukui","=::g<:<2k=gkiwii","=::g<:<2kV mikui","=::g<<:0gk=:ikuu","=:gw<:22kT m=gui","=::g<<:0 m=Rukui","=::g<<:2mkV ikiu","=::g<<<0 mkVukii","=::g<<<2kkkkikii","=::gwwwk2222ikii","=:<g:g=0::0:uwuu","=:<g:g=2=:0guwui","=:<g:g=2>:F uwiu","=:<g<g=0g=0:iwuu","=:<g<g=0 >0Ruwui")
    "brick-3x3" -> Array("=Fyy=F=Fy","=RywVm0F>","=RyR@2Tmi",">:R:R@Tyi","=RmwTmF@:",">:FyuTR@:","><F XXmwT","XXXXXXXXX")
    "flanders-3x2" -> Array("4epNg=")
    "interleaved-2x4" -> Array("A::f::;3","A4fkkA4f","=;gjgb 7","=4gp e N","A5hv  NN","AfAf;3;3","Af=g=3;g","Afpp;443","Afjq=44g","AHez?3HN","AQ r:4;3","A3:g=:;f","=g=g=g=g","Ag6gU ?g","AgpjS 83","AgjkV 8g","AIet?fLN","AR lR ?3","AI mV ?f","ASetErL7","::::::::","B:Q ::4:","A:4fQ C3",">:R :R >",":4:4 c c","B4Q  B4Q",">4R  d W",";434pa r","4444pppp","C4HeLCHr","?4I pb l","C;Se7HrK","CgL a 8g","A34gU Cf","CItUe NN","CJCJLrLr","CSDJNrjK","<<<<iiii","DSDSlKlK","DJza NNN","X X  X X","eeeeNNNN","A:fp:;43","=:gp:S 7","A;3ffA;3","A;fjgA43","A4fp C4H")
    "diagonal-4x4" -> Array("FXmmRXX>TRT<=y@k","mXFXX@TRTg@X@T y","RX@TRVm>yFT:>y>R","RX@TXVm@yFT >yXF","RX@TTRm@>RVk>ymF","RX@TVVm@mFTg>y>R","mmFXX@TRTg@V@Vkm","yF@T>RmX>RVV>wmF","yF@T@VmXmFTR>w>R","FTgmy@X>XFTR=y@V","FTRmy@X>RFVT=ym@","mmFXR@VTTgm@@VVm","XTgFy@@TXFiR>y@V","XTRFw@@TFFiy>yX@","XXFXR@VTTgi<>y@m","XT:Ry@@TXFkV>ymF","Fi:gu::::<>u= u<","Fi:gu:<=:<mi= u<","Fi:gw=::g2>u= u<","Fi:gw=<=g2mi= u<","Xi:<u:: :<Xi: u:","Ri: u:R0:<>u> u<","mi<Xy@FTX0yX@VTR","FXmmTRX@>RTg=y@V","Fi<ku:g0:<>u= u<","Fi<kw=g0g2>u= u<","Fi guR0::<>u= w@","Fi guR2=:<mi= w@","Fi gwV2=g2mi= w@","Ri  wVF0g2>u> w@","FVXmyFX>:RVT=ym@","XVFXy2VT>giR>y@X","mXXFTR@V:Rmi@Ty@","XkX2u : X2Xk: u ","mkV2yk0 y2T <kT ","XkV2wk0 m2Xk: u ","mkV2wk0 m2Vk<ki ","Fk=gug0::<>u= u<","XXmFTR@V>Rig>y@V","Fk=gug2=:<mi= u<","Fk=gwk0:g2>u= u<","Fk=gwk2=g2mi= u<","FV>yyFX>>RVV=ymF","XV>RyF@T>RkV>ymF","Fk>gu ::R2>u= u<","Fk>gu <=R2mi= u<","yk0 y2T :gT >wV2","XXXFTR@V:Rki>ym@","Rk> u R0R2>u> u<","yk0 w2T 0gVk>wk2","RV@TyFm>>RTR>y>R","RV@TyFm>>RVV>ymF","gk2iw0g=0:ug=w=<","XXFXT:RV>:ug>y@X","gV2iw0g=0:wm=u <","gk2iw0g=0:wk=wk2","gk2iw2k=0gig=w=<","gV2iw2k=0gkm=u <","gk2iw2k=0gkk=wk2","mk2ky2i :gT =wV2","Fk@ku g0R2>u= u<","mk2kw2i 0gTg=w=<","mmmFRX@TTRm@@VVk","XXFXT<VV>gig>y@X","mV2kw2i 0gVm=u <","mk2kw2i 0gVk=wk2","g=<iw0g=0:ug=u:<","g=<iw0g=0:wk=ug2","g=<iw2i:0g=w=ug2","g=<iw2k=0gkk=ug2","m=<ky2i :gT =uR2","m=<kw2i 0gTg=u:<","m=<kw2i 0gVk=ug2","FXXXVTR@i<Vk@mmm","m>gmX@XXXFTR=u<V","m>RmX@XXRFVT=ug@","X>RXT:RV>:wm>yFX","FXmmVVX@mFTg=y@V","m>RmV@XXFFTy=uR@","m0ym>RXX>RVX=wFV","FXXmVVX@gFVi=ym@","F0ug=u:<i<@k= gk","F0ug:u::u<@== gk","F0ug>u:<u<> = Rk","FXXXVVT<ig@k@Xmm","F0wk=ug2i<@k= gk","F0wk>ug2u<> = Rk","m>:yX@XXXFVV=ugF","FmXXVVV@igFm@@Ty","m0 w<Vi igV2=w2m","mmmFVV@VmFmm@VVV","m@TRRm@TTRm@@TRm","XXmFVV@VmFig>y@V","X@TRVk@VmFkm>yFX","XXmFRX@TTRi<>y@k","m2=w<ki igV2=w0g","mFmm>RXX>RTR=w@V","TRmF>R>y>RTR>y@V","TRXF>R>y:RVT>ym@","TRmF@V>ymFTR>y@V","mFXm>RXX:RVT=wm@","ig0g=::w0:ug=w=<","iR0g=::w0:wm=u <","ig0g=::w0:wk=wk2","ig0g=<=w0gig=w=<","iR0g=<=w0gkm=u <","TR>R>R>y>RVV>ymF","XFXX@TRXi<XV> ym","ig0g:::u::u:=w=<","ig0g:::u::w==wk2","iR0g:::u::w>=u <","ig0g>::w::u =wV2","ig0g:<=u:gk==wk2","iR0g:<=u:gk>=u <","mFXX@TRXi<VV@kmm","ig0g<=:ug0u:=w=<","uR0 ::Ri::w>>u <","ug0 >:Rk::u >wV2","uR0 :<Vi:gk>>u <","ug0 ><Vk:gi >wV2","ig2m:: u:Rk==wk2","ig2m>: w:Ri =wV2","iR2k=:gk0:wm=u <","TR@V>Rmm>RVV>ymF","ig2k::gi::w==wk2","iR2k::gi::w>=u <","iR2k:<ki:gk>=u <","FXXXXTR@u<T @mXm","mFXX@VTRig@V@Vmm","iw0<w0<i0<iw<iw0","uw20w20u20uw0uw2","mFXX<VVTigm@@VXm","i::g=::w0:ug=u:<","i::g=::w0:wk=ug2","i::g:::u::u:=u:<","i::g:::u::w==ug2","i::g>::w::u =uR2","RXFTR>y>TRT<>y@m","i::g:<=u:gi:=u:<","i::g:<=u:gk==ug2","i::g<=:ug0u:=u:<","u::::::u::u::u::","u:: ::Ri::u:>u:<","u:: >:Rk::u >uR2","u:: :<Vi:gi:>u:<","RXFTT:y@>RTg>y@X","i:<m:: u:Rk==ug2","yFFT>:yX>RTR>w@X","i:<k=:gk0:wk=ug2","i:<k::gi::u:=u:<","i:<k>:gk::u =uR2","i:<k:<ki:gi:=u:<","i:<k:<ki:gk==ug2","FXXVVTy2iR@k@Xmm","u:  :RFi::u:>u<@","u<V0<k0um0u<:u: ","FmTRTy@@TRFm@@Ty","FFig=u:<i<@m=  w","mFTR:y@TTRm@@VXm","i :gR0:u::w==wm2","i :gX0:w::u =wX2","i :gR2=u:gk==wm2","FXmmRVX>yFT:=y@V","FFig:u::u<@>=  w","i :gX2=w:gi =wX2","i :gV0:w0:wk=wm2","u : R2Vi:gi:>w><","RFi :uR0u<@>>  w","u : X2Vk:gi >wX2","u : V0Rk0:wk>wm2","i <mR0 u:Ri:=w><","i <mR0 u:Rk==wm2","i <mX0 w:Ri =wX2","i <mV0 w0Rig=w><","FmVXTyF@T:Rm@@Ty","i <kR0gi::u:=w><","i <kR0gi::w==wm2","i <kX0gk::u =wX2","FXVXTwF@y2Vi@mmX","XFVX>yFXT<XV> ym","wX0  :Ri0:wX>u <","wm0  :Ri0:wV>wk2","mFVX:yFTT:y@@VXm","kX2k :gi0:wX=u <","VTRFm@>yRFVT>ym@","FXVVTym2TR@k@Xmm","XXmFRV@TyFi:>y@V","FFkk=ug2i<@m=  w","VV>RmF>y>RVV>ymF","FFkk:ug0u<@>=  w","kV2mg0 u:Rk>=u <","FX>yRVX>yFV==ymF","k=:gg2=u:gk==ug2","XX>RRV@TyFi:>y>R","XmFXX>RXT<XV> wm","mX0yX@XXXFVX=u R","XX>RTR@V>Rig>y>R","k@@m   uFFkV=ug2","XX>RTR@V>Rkk>ymF","XX>RVV@VmFkk>ymF","XXFXR>RTT:u<>y@m","mF>y>RXX>RVV=wmF","mm@XRXFTT:y@@VTg","XX@XRXFTT:u<>y>g","Xm@XXVFXy2XT> uR","mmFmX@XXRFVT=wk@","XX@XTRFV>:ug>y>R","mm@XVVFVm0ym@VTR","XX@XVVFVm0ug>y>R")
    "interleaved-4x2" -> Array("::::::::","Af:3ww01","Af:4fA3;","Af:4 c:4","Af;3c 3;","Af;4pp34","Ag=f:3:;","Ag=g=f:4","Ag=gW 3;","Ag=hjp34","AgW 3::;",":::; c:3","AgW qw01","AgX f=3;","AgX  W:4","AgY pj34","Ah03vv34","Ah14oo:;","Ahio;;34","Ahip e04","Ahjoe 31","Ahjp33:;","::;;pp33","Ahjq6f:4","AhjqN 3;","Ahkp N:4","::::<<ii","::;;44oo","::;<P oi","::<<00uu","::<<kkii","::ww22ii",":; c<5ii",":; cwq00",":; e14oo",";;33;;oo",";;34 eio",";;4433uu",";;44qqii",";;45N ou",";;55jjoo",";;pp55ii",";;qq11oo",";<N 4;oo",":; dW 3:",";<O  Yio",";<P 30uu","<<00wwii","<<01 cuo","<<11ppoo","<<22iiuu","<<ii<<ii","Af:3<<ij","Af;444op","Af;56hip",":; ejp33","Af;5P oj","Af<4 Pip","Af<500uv","Af<5kkij","Afwq22ij","Ag=f<5ij","Ag=h14op","AgW 5<ij","AgY 41op","Ah03;;op",";;op e03","Ah04hAoj","Ah04 eip","Ah13e oj","Ah1433uv","Ah156fup","Ah15N ov","Ah24 Nup","4444pppp","44pp44pp","45O  Wvp",";;qqjj33","5511oovv","cceePPNN","eeNNeeNN",":4 c;3c ",":4 ejqN ",";3d  XW ",";444poc ",";4pp43c ",";4qp OW ",";5N pwN ","Af:3:::;",";5P pic ","=g=g=gX ","=gW 4:d ","=gX  XX ")
  }

  def fromTuple = Map() ++ Graphs.toTuple.map(_.swap)
  def pack(m: M): String = {
    var s = ""
    for {
      r <- m.indices
      c <- m(0).indices
    } s = s + fromTuple.getOrElse(m(r)(c),"?")
    s
  }

  "convert" should "print no question marks" in {

    val tupleMap = HashMap[String,Char](
      // ascii-art positions 43210
      "1,1,0,0,0" -> '0', // .../_
      "1,0,1,0,0" -> '1', // ..|._
      "1,0,0,1,0" -> '2', // .\.._
      "1,0,0,0,1" -> '3', // _..._
      "0,1,1,0,0" -> '4', // ..|/.
      "0,1,0,1,0" -> '5', // .\./.
      "0,1,0,0,1" -> '6', // _../.
      "0,0,1,1,0" -> '7', // .\|..
      "0,0,1,0,1" -> '8', // _.|..
      "0,0,0,1,1" -> '9', // _.|..
      "0,0,0,0,0" -> '-') // _\...

    Array("2x2", "2x4", "4x2").foreach { dim =>
      println((for (m <- Graphs.matrixMap(s"interleaved-$dim")) yield
        (for (tuple <- m.flatten) yield tupleMap.
          getOrElse(tuple.replaceAll("-1", "0").substring(0, 9), '?')).mkString
        ).mkString("\""+dim+"\" -> Array[String](\"", "\",\"", "\"),"))
    }
  }
}

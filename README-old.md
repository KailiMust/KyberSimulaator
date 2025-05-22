# Küberturvalisuse Stsenaariumi Simulaator - Projekti Kirjeldus

## Autorite nimed
- Kevin Laig
- Kaili Must

## Projekti põhjalik kirjeldus

### Programmi eesmärk
Küberturvalisuse Stsenaariumi Simulaator on tekstipõhine õppeprogramm, mis võimaldab kasutajal harjutada küberturvalisuse olukordades otsuste tegemist. Programm esitab kasutajale erinevaid küberturvalisuse stsenaariumeid (nt phishing e-kirjad, paroolide turvalisus), laseb kasutajal teha valikuid ja annab tagasisidet valikute tulemuslikkuse kohta.

Programmi peamine eesmärk on:
1. Õpetada küberturvalisuse põhimõtteid praktiliste stsenaariumite kaudu
2. Arendada kasutaja oskust tuvastada erinevaid küberohte
3. Harjutada turvaküsimustele reageerimist turvalisus keskkonnas
4. Tutvustada parimaid tavasid erinevate küberturvalisuse olukordade lahendamisel

### Programmi üldine tööpõhimõte
Programm töötab tekstipõhise simulatsioonina, kus:

1. Kasutajale kuvatakse peamenüü, kust ta saab valida:
    - Juhusliku stsenaariumi käivitamise
    - Stsenaariumi valimine kategooria järgi
    - Juhiste kuvamine
    - Programmist väljumine

2. Stsenaariumite käivitamisel:
    - Näidatakse stsenaariumite kirjeldust (nt "Oled IT-turvaspetsialist ja finantsosakonna töötaja edastab sulle kahtlase e-kirja")
    - Esitatakse küsimusi, millele kasutaja peab vastama
    - Igal küsimusel on mitu valikvastust
    - Kasutaja saab küsida lisaküsimusi või lisateavet, sisestades "?info [teema]"
    - Kasutaja valikule vastavalt kuvatakse tagajärg ja õpetlik selgitus

3. Pärast stsenaariumi läbimist:
    - Näidatakse kokkuvõtet
    - Jagatakse hariduslikku tagasisidet, sh parimaid tavasid
    - Pakutakse võimalust naasta peamenüüsse või käivitada uus stsenaarium

Programm kasutab Random klassi, et valida juhuslikke stsenaariumeid ja esitada valikud erinevas järjekorras.

### Kasutusjuhis
1. Programmi käivitamiseks:
   ```
   java KüberSimulaator
   ```

   Võimalik on kasutada ka käsurea argumente:
   ```
   java KüberSimulaator --joptionpane --kategooria=phishing
   ```
   ```
   java KüberSimulaator --joptionpane --kategooria=paroolid
   ```

2. Peamenüüs navigeerimiseks sisesta vastav number ja vajuta Enter:
    - 1: Alusta juhuslikku stsenaariumit
    - 2: Vali kategooria (Phishing, paroolid)
    - 3: Kuva juhised
    - 4: Välju

3. Stsenaariumites:
    - Loe hoolikalt stsenaariumi kirjeldust
    - Vasta küsimustele, sisestades valiku number
    - Lisateabe küsimiseks sisesta "?info [teema]" (nt "?info saatja")
    - Stsenaariumist väljumiseks sisesta "välju"

## Klasside kirjeldused

### KüberSimulaator
**Eesmärk**: Programmi peaklass, mis käivitab simulaatori ja koordineerib teiste klasside tööd.

**Olulisemad meetodid**:
- `main(String[] args)`: Programmi käivituspunkt, töötleb käsurea argumente
- `alusta()`: Initsialiseerib simulatsiooni ja näitab tervitust
- `näitaMenüü()`: Kuvab peamenüü valikud
- `käivitaStsenaarium(Stsenaarium stsenaarium)`: Käivitab valitud stsenaariumi
- `küsiSisend(String sõnum)`: Küsib kasutajalt sisendit kuvades teate

### KasutajaLiides
**Eesmärk**: Haldab kasutajaga suhtlemist.

**Olulisemad meetodid**:
- `küsiSisend(String küsimus)`: Küsib kasutajalt sisendit (kasutades kas Scanner või JOptionPane)
- `näitaTeade(String teade)`: Kuvab kasutajale teate
- `näitaViga(String veateade)`: Kuvab vea
- `seadistaSisendMeetod(boolean kasutaJOptionPane)`: Määrab, kas kasutada konsooli või hüpikaknaid

### Stsenaarium
**Eesmärk**: Esindab ühte terviklikku küberturvalisuse stsenaariumit koos kirjelduse ja küsimustega.

**Olulisemad meetodid**:
- `Stsenaarium(String pealkiri, String kirjeldus, int raskusaste, String kategooria)`: Konstruktor
- `lisaKüsimus(Küsimus küsimus)`: Lisab stsenaariumile küsimuse
- `esita(KasutajaLiides liides)`: Esitab stsenaariumi kasutajale ja töötleb küsimusi
- `näitaKokkuvõte()`: Näitab õpetlikku kokkuvõtet pärast stsenaariumi lõpetamist

### Küsimus
**Eesmärk**: Esindab stsenaariumis otsustuskohta, kus kasutaja peab valima mitme võimaluse vahel.

**Olulisemad meetodid**:
- `Küsimus(String küsimusTekst)`: Konstruktor
- `lisaValik(Valik valik)`: Lisab küsimusele vastusevariandi
- `lisaInfo(String võti, String väärtus)`: Lisab täiendava teabe, mida kasutaja saab pärida
- `esitaJaSaaValik(KasutajaLiides liides)`: Esitab küsimuse ja tagastab kasutaja valiku
- `näitaLisaInfot(String teema)`: Näitab küsitud lisateavet

### Valik
**Eesmärk**: Esindab ühte vastusevarianti küsimusele koos tagajärje ja selgitusega.

**Olulisemad meetodid**:
- `Valik(String tekst, String tagajärg, String selgitus, int turvalisusSkoor)`: Konstruktor
- `näitaTulemus()`: Näitab valiku tagajärge ja selgitust pärast valimist

### StsenaariumiHaldur
**Eesmärk**: Haldab kõiki saadaolevaid stsenaariumeid ja tegeleb nende valimiega.

**Olulisemad meetodid**:
- `StsenaariumiHaldur()`: Konstruktor, initsialiseerib stsenaariumite kogu
- `juhuslikStsenaarium()`: Tagastab juhusliku stsenaariumi
- `saaKategooriast(String kategooria)`: Tagastab stsenaariumi kategooria järgi
- `saaKategooriad()`: Tagastab kõik saadaolevad kategooriad

### PhishingStsenaariumid
**Eesmärk**: Loob eeldefineeritud phishingu stsenaariumeid.

**Olulisemad meetodid**:
- `looStsenaariumid()`: Loob kõik phishingu stsenaariumid
- `looKahtlaneEmailStsenaarium()`: Loob konkreetse phishingu stsenaariumi
- `looLunarahaManuseStsenaarium()`: Loob teise phishingu stsenaariumi

### ParooliStsenaariumid
**Eesmärk**: Loob paroolide tugevuse testimise stsenaariumeid.

**Olulisemad meetodid**:
- `looStsenaariumid()`: Loob kõik paroolide stsenaariumid
- `looParooliStsenaarium()`: Loob konkreetse paroolidega seonduva stsenaariumi

## Testimise metoodika

Projekti testimiseks mängisime ise mitmeid kordi erinevaid stsenaariume läbi.

## Projekti tegemise protsessi kirjeldus

### 1. Etapp: Kontseptsiooni loomine ja disainimine
Projekti alustasime kontseptsiooni loomisest, kus määratlesime programmi eesmärgid, struktuuri ja põhifunktsionaalsuse. Panime paika, et programm keskendub küberturvalisuse stsenaariumitele, võimaldades kasutajal harjutada otsuste tegemist erinevates olukordades.

Disainisime programmi põhiklassid ja nendevahelised seosed, järgides objektorienteeritud programmeerimise põhimõtteid. Määratlesime selgelt iga klassi vastutusala ja vajalikke meetodeid.

### 2. Etapp: Põhiklasside implementeerimine
Implementeerisime programmi põhiklassid (KüberSimulaator, KasutajaLiides, Stsenaarium, Küsimus, Valik, StsenaariumiHaldur). Lõime lihtsa tekstipõhise kasutajaliidese, mis võimaldab kasutajal navigeerida menüüdes ja vastata küsimustele.

Implementeerisime ka stsenaariumite juhusliku valimise funktsionaalsuse, kasutades Java Random klassi, ning käsurea argumentide töötlemist.

### 3. Etapp: Stsenaariumite loomine
Selles etapis keskendusime erinevate küberturvalisuse stsenaariumite loomisele. Alustasime phishingu stsenaariumitest, luues realistlikud olukorrad, kus kasutaja peab tuvastama ja reageerima phishingu katsetele.

Seejärel lõime stsenaariumid teiste kategooriate jaoks: paroolid (plaanis olid ka teisi, nt võrguturve jms). Iga stsenaarium sisaldab mitmeid küsimusi, kus iga küsimus pakub mitu vastusevarianti koos tagajärgede ja õpetlike selgitustega.

## Panus
Mõlemad osalesime igas etapis. Alati kui kumbki kirjutas koodi, siis tegime seda üksteisega nõu pidades. Koostöö sujus suurepäraselt.

Selle tõttu on raske määratleda, kes millise klassi kirjutas.

Ajakulu oli umbes 25 tundi, millest 15 tundi kulus idee arendamisele, küberturbe kohta uurimisele, projekti disainimisele ja planeerimisele.  
Ülejäänud 10 tundi kulus koodi kirjutamisele, läbimängimisele ja erinevate IntelliJ seadete muutmisele/uurimisele.

Töötasime vahepeal korraga sama faili kallal, seega see lisas ajakulu.

## Tegemise mured
Ajaplaneerimine oli meeletu probleem. Tehnilisi probleeme väga ette ei tulnud, programm ise on üpriski lihtne.

Põhiline tehniline probleem oli joptionpane 'parandamine' nii, et see ei näita igat rida teksti uues aknas.

Mõtlesime kasutada liideseid ja muud, mida kursusel õppisime, aga loogilisem oli antud juhul kasutada lihtsalt klasse.

## Hinnang
Meie arust on kogu projekt hästi õnnestunud. See on hariv ja mängimine on lõbus.

Põhiline puudus on stsenaariumite ja kategooriate vähesus, mis on otseselt ajaplaneerimise probleem.

## Tuleviku plaanid
Kavatseme seda edasi arendada järgmises rühmatöös. Teha adekvaatse kasutajaliidese, lisada erinevaid kategooriaid ja stsenaariume ning erinevat võimekust nagu skoori pikaajaline salvestamine, edetabel jne.
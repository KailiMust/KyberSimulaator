# Küberturvalisuse Simulaator - JavaFX Versioon

## Autorite nimed
- Kevin Laig
- Kaili Must

## Projekti põhjalik kirjeldus

### Programmi eesmärk
Küberturvalisuse Simulaator on JavaFX-põhine õppeprogramm, mis võimaldab kasutajal harjutada küberturvalisuse olukordades otsuste tegemist läbi interaktiivsete stsenaariumite. Programm esitab kasutajale erinevaid küberturvalisuse stsenaariumeid (nt phishing e-kirjad, paroolide turvalisus), laseb kasutajal teha valikuid ja annab tagasisidet valikute tulemuslikkuse kohta.

Programmi peamine eesmärk on:
1. Õpetada küberturvalisuse põhimõtteid praktiliste stsenaariumite kaudu
2. Arendada kasutaja oskust tuvastada erinevaid küberohte
3. Harjutada turvaküsimustele reageerimist turvalisus keskkonnas
4. Tutvustada parimaid tavasid erinevate küberturvalisuse olukordade lahendamisel
5. Pakkuda graafilist kasutajaliidest parema kasutajakogemuse jaoks

### Programmi üldine tööpõhimõte
Programm töötab JavaFX graafilise kasutajaliidese kaudu, kus:

1. Kasutaja loob endale profiili või valib olemasoleva
2. Peamenüüs saab kasutaja valida:
   - Juhusliku stsenaariumi käivitamise
   - Stsenaariumi valimine kategooria järgi
   - Statistika vaatamine
   - Juhiste kuvamine
   - Kasutaja vahetamine või programmist väljumine

3. Stsenaariumite käivitamisel:
   - Näidatakse stsenaariumite kirjeldust graafiliselt
   - Esitatakse küsimusi koos mitme valikvastusega
   - Kasutaja saab vaadata lisainfot
   - Pärast vastuse valimist kuvatakse tagajärg ja õpetlik selgitus

4. Pärast stsenaariumi läbimist:
   - Näidatakse kokkuvõtet ja õpetlikku tagasisidet
   - Salvestatakse tulemused kasutaja profiili
   - Pakutakse võimalust naasta peamenüüsse või käivitada uus stsenaarium

### Kasutusjuhis

#### Projekti käivitamine Windowsis
1. Projekti juurkaustas on juba olemas Maven Wrapper failid
2. Ava käsurida (Command Prompt või PowerShell)
3. Navigeeri projekti kausta:
   ```
   cd C:\Users\kasutajanimi\Documents\GitHub\KyberSimulaator
   ```
4. Käivita projekt Maven Wrapper abil:
   ```
   .\mvnw.cmd clean javafx:run
   ```

#### Programmi kasutamine
1. **Kasutaja loomine/valimine**: Esmakordsel käivitamisel loo endale kasutaja või vali olemasolev
2. **Peamenüüs navigeerimine**: Vali soovitud tegevus nuppudega
3. **Stsenaariumites**: 
   - Loe hoolikalt stsenaariumi kirjeldust
   - Vali vastus valikute hulgast
   - Kasuta "Lisainfo" nuppu täiendava teabe saamiseks
   - Jätka järgmise küsimusega või välju stsenaariumist
4. **Klaviatuuri kasutamine**:
   - Enter: kinnita valik
   - Escape: tagasi/tühista
   - Tab: liigu elementide vahel

## Klasside kirjeldused

### KüberSimulaatorFX
**Eesmärk**: Programmi peaklass, mis käivitab JavaFX rakenduse ja koordineerib kõigi komponentide tööd.

**Olulisemad meetodid**:
- `main(String[] args)`: Programmi käivituspunkt
- `start(Stage primaryStage)`: JavaFX rakenduse käivitamine
- `seadistaPeamineLava()`: Peamise akna seadistamine
- `laadiRakendusIkoon()`: Rakenduse ikooni laadimine
- `looAndmeteKaust()`: Andmete kausta loomine
- `näitaKasutajaValikEkraan()`: Kasutaja valiku ekraani kuvamine
- `looKasutajaValikEkraan()`: Kasutaja valiku ekraani loomine
- `looKasutajaValikSisu()`: Kasutaja valiku sisu loomine
- `looKasutajateLoend(List<String>, VBox)`: Kasutajate loendi loomine
- `valiKasutaja(String)`: Kasutaja valimine ja profiili laadimine
- `looUusKasutaja()`: Uue kasutaja loomise dialoogi avamine
- `kustutaKasutaja()`: Kasutaja kustutamise dialoogi avamine
- `näitaPeamenüü()`: Peamenüü ekraani kuvamine
- `looPeamenüüEkraan()`: Peamenüü ekraani loomine
- `looTervitusePäis()`: Tervituse päise loomine
- `looMenüüPaneel()`: Menüü nuppude paneeli loomine
- `näitaKategooriadEkraan()`: Kategooriate valiku ekraani kuvamine
- `looKategooriadEkraan()`: Kategooriate valiku ekraani loomine
- `looKategooriadPaneel()`: Kategooriate paneeli loomine
- `looKategooriaKaart(String, String)`: Üksiku kategooria kaardi loomine
- `getKategoriaKirjeldused()`: Kategooriate kirjelduste tagastamine
- `näitaStatistika()`: Statistika ekraani kuvamine
- `näitaJuhisedEkraan()`: Juhiste ekraani kuvamine
- `looJuhisedEkraan()`: Juhiste ekraani loomine
- `looJuhisedSisu()`: Juhiste sisu loomine
- `looJuhisedSektsioon(String, String[])`: Juhiste sektsiooni loomine
- `käivitaJuhuslikStsenaarium()`: Juhusliku stsenaariumi käivitamine
- `käivitaKategooriaStsenaarium(String)`: Kategooria stsenaariumi käivitamine
- `näitaStsenaariumiEkraan(Stsenaarium)`: Stsenaariumi ekraani kuvamine
- `looMenüüNupp(String, VBox, Consumer<Void>)`: Menüü nupu loomine
- `looKohandatudNupp(String, int, VBox, Consumer<Void>)`: Kohandatud nupu loomine
- `looStandardNupp(String, int, Consumer<Void>)`: Standardse nupu loomine
- `looSubtiitel(String)`: Subtiitli labeli loomine
- `looKeskendatudPaneel(javafx.scene.Node)`: Keskendatud paneeli loomine
- `looStseenKlaviatuuriTuega(javafx.scene.Parent, Runnable)`: Stseeni loomine klaviatuuri toega
- `lisaCSS(Scene)`: CSS stiilide lisamine stseenile
- `näitaViga(String)`: Veateate kuvamine
- `logi(String)`: Sündmuse logimine
- `logiSessioonLõpp()`: Sessiooni lõpu logimine

### StsenaariumiEkraan
**Eesmärk**: Haldab stsenaariumi kuvamist JavaFX kasutajaliideses ja kasutaja interaktsiooni stsenaariumiga.

**Olulisemad meetodid**:
- `StsenaariumiEkraan(Stage, Stsenaarium, String, Runnable)`: Konstruktor
- `näita()`: Stsenaariumi ekraani kuvamine
- `näitaJärgmineKüsimus()`: Järgmise küsimuse kuvamine
- `näitaLisainfo()`: Lisainfo dialoogi kuvamine
- `töötleValik(Valik)`: Kasutaja valiku töötlemine
- `näitaTulemusEkraan()`: Tulemuste ekraani kuvamine
- `katkestaStsenaarium()`: Stsenaariumi katkestamine
- `näitaViga(String)`: Veateate kuvamine

### StatistikaEkraan
**Eesmärk**: Haldab kasutaja statistika kuvamist JavaFX kasutajaliideses.

**Olulisemad meetodid**:
- `StatistikaEkraan(Stage, String, Runnable)`: Konstruktor
- `näita()`: Statistika ekraani kuvamine
- `looStatistikaEkraan()`: Statistika ekraani loomine
- `looInfoLabel(String, String)`: Info labeli loomine
- `lisaCSS(Scene)`: CSS stiilide lisamine

### KasutajaLoomisDialoog
**Eesmärk**: Kuvab dialoogi uue kasutaja loomiseks ja valideerib sisestatud andmeid.

**Olulisemad meetodid**:
- `näitaDialoog(Stage)`: Dialoogi kuvamine
- `valideerNimi(String)`: Kasutaja nime valideerimine

### KasutajaKustutamiseDialoog
**Eesmärg**: Kuvab dialoogi kasutaja turvaliseks kustutamiseks ja nõuab kinnitust.

**Olulisemad meetodid**:
- `näitaDialoog(Stage)`: Dialoogi kuvamine
- `kinnitaKustutamine(String)`: Kustutamise kinnituse küsimine
- `näitaTeade(String, Alert.AlertType)`: Teate kuvamine

### KasutajaLiides
**Eesmärk**: Haldab kasutajaga suhtlemist nii konsooli kui ka JOptionPane kaudu (tagasiühilduvuse jaoks).

**Olulisemad meetodid**:
- `KasutajaLiides(boolean)`: Konstruktor
- `küsiSisend(String)`: Kasutajalt sisendi küsimine
- `näitaTeade(String)`: Teate kuvamine
- `näitaViga(String)`: Veateate kuvamine
- `puhastaPuhver()`: Sõnumipuhvri puhastamine

### Stsenaarium
**Eesmärk**: Esindab ühte terviklikku küberturvalisuse stsenaariumit koos kirjelduse ja küsimustega.

**Olulisemad meetodid**:
- `Stsenaarium(String, String, int, String)`: Konstruktor
- `lisaKüsimus(Küsimus)`: Küsimuse lisamine stsenaariumile
- `esita(KasutajaLiides)`: Stsenaariumi esitamine (konsooli jaoks)
- `näitaKokkuvõte(KasutajaLiides)`: Kokkuvõtte kuvamine
- `getPealkiri()`: Pealkirja tagastamine
- `getKategooria()`: Kategooria tagastamine
- `getKirjeldus()`: Kirjelduse tagastamine
- `getKokkuvõte()`: Kokkuvõtte tagastamine
- `getKüsimused()`: Küsimuste loendi tagastamine

### Küsimus
**Eesmärk**: Esindab stsenaariumis otsustuskohta, kus kasutaja peab valima mitme võimaluse vahel.

**Olulisemad meetodid**:
- `Küsimus(String)`: Konstruktor
- `lisaValik(Valik)`: Vastusevariandi lisamine
- `lisaInfo(String, String)`: Lisateabe lisamine
- `esitaJaSaaValik(KasutajaLiides)`: Küsimuse esitamine ja vastuse saamine
- `näitaKüsimusJaValikud(KasutajaLiides, List<Valik>)`: Küsimuse ja valikute kuvamine
- `püüaValidaJaTagastaValik(String, List<Valik>, KasutajaLiides)`: Sisendi valideerimine
- `näitaLisaInfot(String, KasutajaLiides)`: Lisateabe kuvamine
- `getKüsimusTekst()`: Küsimuse teksti tagastamine
- `getValikud()`: Valikute loendi tagastamine

### Valik
**Eesmärk**: Esindab ühte vastusevarianti küsimusele koos tagajärje ja selgitusega.

**Olulisemad meetodid**:
- `Valik(String, String, String, int)`: Konstruktor
- `näitaTulemus(KasutajaLiides)`: Tulemuse kuvamine
- `getTekst()`: Valiku teksti tagastamine
- `getSkoor()`: Skoori tagastamine
- `getTagajärg()`: Tagajärje tagastamine
- `getSelgitus()`: Selgituse tagastamine
- `getTurvalisusSkoor()`: Turvalisusstkoori tagastamine

### StsenaariumiHaldur
**Eesmärk**: Haldab kõiki saadaolevaid stsenaariumeid ja tegeleb nende valimiega.

**Olulisemad meetodid**:
- `StsenaariumiHaldur()`: Konstruktor
- `laadiStsenaariumid()`: Kõikide stsenaariumite laadimine
- `juhuslikStsenaarium()`: Juhusliku stsenaariumi tagastamine
- `saaKategooriast(String)`: Kategooria stsenaariumi tagastamine
- `saaKategooriad()`: Kõikide kategooriate tagastamine

### PhishingStsenaariumid
**Eesmärk**: Loob eeldefineeritud phishingu stsenaariumeid.

**Olulisemad meetodid**:
- `looStsenaariumid()`: Kõikide phishingu stsenaariumite loomine
- `looKahtlaneEmailStsenaarium()`: Kahtlase e-kirja stsenaariumi loomine
- `looLunarahaManuseStsenaarium()`: Lunaraha manuse stsenaariumi loomine

### ParooliStsenaariumid
**Eesmärk**: Loob paroolide tugevuse testimise stsenaariumeid.

**Olulisemad meetodid**:
- `looStsenaariumid()`: Kõikide paroolide stsenaariumite loomine
- `looparooliTugevuseStsenaarium()`: Parooli tugevuse stsenaariumi loomine

### KasutajaProfiil
**Eesmärk**: Haldab kasutaja profiili andmeid ja nende salvestamist/laadimist failisüsteemist.

**Olulisemad meetodid**:
- `KasutajaProfiil(String)`: Konstruktor
- `getNimi()`: Kasutaja nime tagastamine
- `getKoguSkoor()`: Koguskoori tagastamine
- `getLäbitudStsenaariumiteArv()`: Läbitud stsenaariumite arvu tagastamine
- `getEbaõnnestunudStsenaariumiteArv()`: Ebaõnnestunud stsenaariumite arvu tagastamine
- `lisaLäbitudStsenaarium(String, int)`: Läbitud stsenaariumi lisamine
- `lisaEbaõnnestunudStsenaarium(String)`: Ebaõnnestunud stsenaariumi lisamine
- `salvestaProfiil()`: Profiili salvestamine failisüsteemi
- `laeProfiil(String)`: Profiili laadimine failisüsteemist (staatiline)
- `saaOlemasolevadKasutajadSorteeritult()`: Kõikide kasutajate nimekirja tagastamine (staatiline)
- `kustutaProfiil(String)`: Kasutajaprofiili kustutamine (staatiline)
- `kustutaKaustRekursiivselt(File)`: Kausta rekursiivne kustutamine (staatiline)

### LogiHaldur
**Eesmärk**: Haldab kasutaja tegevuste logimist failisüsteemi.

**Olulisemad meetodid**:
- `LogiHaldur(String)`: Konstruktor
- `logiSündmus(String)`: Sündmuse logimine
- `logiValik(String, String, String)`: Kasutaja valiku logimine
- `logiStsenaariumiLõpp(String, int, boolean)`: Stsenaariumi lõpetamise logimine
- `logiSessioonLõpp(int, int)`: Sessiooni lõpu logimine

### KüberSimulaator
**Eesmärk**: Vana konsooli-põhine peaklass (tagasiühilduvuse jaoks).

**Olulisemad meetodid**:
- `main(String[])`: Programmi käivituspunkt
- `alusta(String)`: Simulaatori alustamine
- `näitaMenüü()`: Peamenüü kuvamine
- `näitaKategooriad()`: Kategooriate kuvamine
- `käivitaStsenaarium(Stsenaarium)`: Stsenaariumi käivitamine
- `näitaJuhised()`: Juhiste kuvamine

## Võimalikud refaktorimise kohad ja probleemid

### Koodidubleerimine
- **KüberSimulaatorFX vs KüberSimulaator**: Kaks erinevat implementatsiooni samale funktsionaalsusele
- **CSS ja stiilide rakendamine**: Korduvad meetodid CSS lisamiseks
- **Nupu loomise meetodid**: Mitu sarnast meetodit nuppude loomiseks

### Pikad meetodid
- **KüberSimulaatorFX.start()**: Võiks jagada väiksemateks meetoditeks
- **StsenaariumiEkraan.näitaJärgmineKüsimus()**: Väga pikk meetod, vajab jagamist
- **KasutajaProfiil.laeProfiil()**: Keeruline faili parsimise loogika

### Tugevad seosed
- **StsenaariumiEkraan** sõltub tugevalt **Stage**-ist ja **KasutajaLiides**-est
- **KüberSimulaatorFX** teeb liiga palju erinevaid asju (kasutajaliides + äriloogika)

### Vigade käsitlus
- Paljud meetodid printserdavad vead konsoolile, kuid ei käsitle neid korralikult
- Puudub ühtne vigu käsitlemise strateegia

### Konfiguratsioon
- Kõvakodeeritud konstanted (failide teed, akna suurused) võiksid olla eraldi konfiguratsioonifailis
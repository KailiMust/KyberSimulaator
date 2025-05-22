package model;
import java.util.ArrayList;
import java.util.List;

/**
 * PhishingStsenaariumid klass loob eeldefineeritud phishingu stsenaariumeid.
 * See klass sisaldab erinevaid phishing-tüüpi stsenaariumeid, mida
 * kasutaja saab simulaatoris läbida.
 *
 * @author Kevin Laig, Kaili Must
 */
public class PhishingStsenaariumid {
    /**
     * Loob kõik phishingu stsenaariumid.
     *
     * @return phishingu stsenaariumite loend
     */
    public List<Stsenaarium> looStsenaariumid() {
        List<Stsenaarium> stsenaariumid = new ArrayList<>();

        // Lisame stsenaariumid loetellu
        stsenaariumid.add(looKahtlaneEmailStsenaarium());
        stsenaariumid.add(looLunarahaManuseStsenaarium());

        return stsenaariumid;
    }

    /**
     * Loob kahtlase e-kirja stsenaariumit.
     *
     * @return kahtlase e-kirja stsenaarium
     */
    private Stsenaarium looKahtlaneEmailStsenaarium() {
        Stsenaarium stsenaarium = new Stsenaarium(
                "Kahtlane e-kiri",
                "Oled IT-turvaspetsialist firmas. Finantsosakonna töötaja Marju Tamm edastab sulle e-kirja, \n" +
                        "mille ta sai väidetavalt firma tegevdirektorilt. E-kiri palub kiiresti teha pangaülekande \n" +
                        "30,000€ uuele tarnijale. E-kirja saatja aadress on ceo@milren.com (firma tegelik \n" +
                        "domeen on milrem.com). Marju küsib, kas ta peaks ülekande tegema.",
                2,
                "Phishing"
        );

        // Loome esimese küsimuse
        Küsimus küsimus1 = new Küsimus(
                "Mida soovitad Marjul teha?"
        );

        // Lisame valikud
        küsimus1.lisaValik(new Valik(
                "Soovita teha ülekanne kiiresti, kuna tegevdirektor ootab",
                "Firma kaotas 30,000€ petturitele!",
                "See oli phishing-kiri. Saatja domeen on sarnane, kuid erinev firma \n" +
                        "tegelikust domeenist. Lisaks peaks suured ülekanded alati läbima \n" +
                        "tavapärase kinnitusprotsessi, mitte kiirjuhiste alusel tehtud saama.",
                -10
        ));

        küsimus1.lisaValik(new Valik(
                "Soovita kontrollida tegevdirektoriga otse telefoni teel",
                "Pettus avastati! Tegevdirektor kinnitas, et tema sellist e-kirja ei saatnud.",
                "Hästi tehtud! Kahtlaste finantstehingute puhul on alati hea mõte kontrollida otse isikuga, \n" +
                        "kes väidetavalt korralduse andis. Telefoni teel või näost-näkku suhtlus on palju turvalisem \n" +
                        "kui lihtsalt e-kirjadele toetumine.",
                10
        ));

        küsimus1.lisaValik(new Valik(
                "Palu Marjul saata kiri edasi finantsjuhile otsustamiseks",
                "Finantsjuht suunas kahtlase e-kirja sinule tagasi, kuna see on turvaküsimus.",
                "See polnud parim lahendus. Vastutuse delegeerimine ei lahenda probleemi ja võib viia \n" +
                        "otsustusahelate tekkimiseni, kus keegi lõpuks ikkagi võib pettuse ohvriks langeda. \n" +
                        "IT-turvaspetsialistina on sinu ülesanne aidata tuvastada võimalikke ohte.",
                -5
        ));

        // Lisame lisainfo
        küsimus1.lisaInfo("saatja",
                "E-kirja saatja aadress on ceo@milren.com\n" +
                        "Firma tegelik domeen on milrem.com\n" +
                        "Märkad 'n' ja 'm' tähtede erinevust domeenis!");

        küsimus1.lisaInfo("kiri",
                "Kiri on inglise keeles, kuigi tegevdirektor tavaliselt kirjutab eesti keeles.\n" +
                        "Kiri on täis kirjavigu ja on üldiselt halvasti vormistatud.\n" +
                        "Kirjas väidetakse, et tehing on väga kiire ja salajane.");

        küsimus1.lisaInfo("tarnija",
                "Kirjas mainitud tarnija kohta pole ühtegi varasemat dokumenti.\n" +
                        "Tarnija pangakonto on registreeritud välismaal, mitte Eestis.");

        // Lisame küsimuse
        stsenaarium.lisaKüsimus(küsimus1);

        // Loome teise küsimuse
        Küsimus küsimus2 = new Küsimus(
                "Marju teavitas sind, et sarnaseid e-kirju on saanud teisedki finantsosakonna töötajad. Mida teeksid järgmiseks?"
        );

        // Lisame valikud
        küsimus2.lisaValik(new Valik(
                "Blokeeri kirja saatja aadress tulemüüris ja liigu edasi",
                "Uued pettukirjad tulid veidi muudetud aadressidelt, probleem jäi lahendamata.",
                "Ainult ühe konkreetse saatja blokeerimine ei ole piisav lahendus. Petturid võivad kergesti \n" +
                        "kasutada uusi aadresse. Lisaks ei tee see midagi töötajate teadlikkuse tõstmiseks, mis on \n" +
                        "võtmetähtsusega phishing-rünnakute vastu.",
                -5
        ));

        küsimus2.lisaValik(new Valik(
                "Saada kogu ettevõttele informatsioonimeil phishing-ohu kohta",
                "Töötajad on nüüd teadlikumad, kuid paljud ei võta seda tõsiselt.",
                "Informatsiooni jagamine on hea esimene samm, kuid lihtsast e-kirjast ei pruugi piisata, \n" +
                        "eriti kui neil puudub kontekst või konkreetsed näited. Lisaks on vaja praktilisi juhiseid \n" +
                        "ja koolitusi, et töötajad oskaksid ohte tuvastada.",
                5
        ));

        küsimus2.lisaValik(new Valik(
                "Korralda kiiresti phishing-turvalisuse koolitus ja saada samas konkreetsed juhised praeguse rünnaku kohta",
                "Suurepärane! Töötajad said koolitust ja konkreetseid juhiseid. Tulevased katsed avastati kiiresti.",
                "See on ideaalne lahendus. Korraldasid kohese reageerimise, mis sisaldas nii pikaajalisemat \n" +
                        "teadlikkuse tõstmist (koolitus) kui ka koheseid praktilisi juhiseid praeguse ohu kohta. \n" +
                        "Turvalisuse koolitus, eriti koos reaalse näitega, aitab töötajatel paremini tuvastada ja \n" +
                        "vältida phishing-rünnakuid.",
                10
        ));

        // Lisame küsimuse
        stsenaarium.lisaKüsimus(küsimus2);

        return stsenaarium;
    }

    /**
     * Loob lunaraha manuse stsenaariumit.
     *
     * @return lunaraha manuse stsenaarium
     */
    private Stsenaarium looLunarahaManuseStsenaarium() {
        Stsenaarium stsenaarium = new Stsenaarium(
                "Kahtlane manus e-kirjas",
                "Oled keskmise suurusega ettevõtte IT-administraator. Mitmed töötajad said e-kirja väidetavalt \n" +
                        "maksuametilt, milles on lisatud Excel fail nimega \"Maksuvõlg_tasuda.xlsx\". E-kiri nõuab faili \n" +
                        "avamist ja makro lubamist, et näha väidetavat maksuvõlga. Üks töötaja on juba faili avanud ja \n" +
                        "makrod lubanud, nüüd on tema arvutiekraanil teade, et kõik failid on krüpteeritud ja ta peab \n" +
                        "maksma 500€ bitcoinides, et saada dekrüpteerimisvõti.",
                3,
                "Phishing"
        );

        // Loome esimese küsimuse
        Küsimus küsimus1 = new Küsimus(
                "Mida peaksid tegema nakatunud arvutiga?"
        );

        // Lisame valikud
        küsimus1.lisaValik(new Valik(
                "Maksa lunaraha, et saada failid tagasi, kuna need võivad olla olulised",
                "Maksid 500€, kuid dekrüpteerimisvõtit ei saadetud ja lunavara levib võrgus edasi.",
                "Lunaraha maksmine ei garanteeri andmete taastamist ja julgustab kurjategijaid jätkama \n" +
                        "oma tegevust. Lisaks on maksnud ettevõtted sageli edaspidi korduvate rünnakute sihtmärgid, \n" +
                        "kuna neid nähakse kui 'maksvaid' ohvreid.",
                -10
        ));

        küsimus1.lisaValik(new Valik(
                "Eralda arvuti koheselt võrgust ja käivita antiviiruseprogramm",
                "Hea esimene samm, kuid lunavara jäi endiselt arvutisse ja failid on krüpteeritud.",
                "Võrgust eemaldamine on õige esimene samm, et takistada lunavara levikut. Kuid enamik \n" +
                        "antiviiruseprogramme ei suuda juba aktiveeritud lunavara eemaldada ega dekrüpteerida faile. \n" +
                        "Vajalik on põhjalikum lähenemine.",
                5
        ));

        küsimus1.lisaValik(new Valik(
                "Eralda arvuti võrgust, puhasta see ja taasta andmed varukoopiast",
                "Suurepärane! Arvuti puhastati täielikult ja andmed taastati edukalt varukoopiast.",
                "See on ideaalne lahendus. Esiteks takistasid lunavara levikut võrgus, puhastasid nakatunud \n" +
                        "arvuti (võimalik, et see tähendab kõvaketta täielikku puhastamist ja opsüsteemi taasinstallimist) \n" +
                        "ja taastasid andmed varukoopiast. See näitab regulaarsete varunduste olulisust.",
                10
        ));

        // Lisame lisainfo
        küsimus1.lisaInfo("lunavara",
                "Lunavara (ransomware) on pahavara, mis krüpteerib ohvri andmed ja nõuab lunaraha nende dekrüpteerimiseks.\n" +
                        "Enamik turvaspetsialiste ei soovita lunaraha maksta, kuna see ei garanteeri andmete taastamist.\n" +
                        "Parim kaitse lunavara vastu on regulaarne andmete varundamine ja töötajate koolitus.");

        küsimus1.lisaInfo("manus",
                "Excel fail nõuab makro lubamist, mis on tüüpiline lunavara aktiveerimise meetod.\n" +
                        "Faili nimi ja teema on loodud tekitama kiireloomulisust ja hirmu (maksuvõlg), mis on tüüpiline sotsiaalse manipulatsiooni võte.");

        küsimus1.lisaInfo("varundus",
                "Ettevõttel on olemas igaöised varundused, mis hoitakse eraldi võrgukettal ja pilves.\n" +
                        "Viimane varundus tehti eelmisel õhtul, seega andmete kadu oleks minimaalne.");

        // Lisame küsimuse
        stsenaarium.lisaKüsimus(küsimus1);

        // Loome teise küsimuse
        Küsimus küsimus2 = new Küsimus(
                "Kuidas peaksid käituma teiste töötajate ja nende arvutitega?"
        );

        // Lisame valikud
        küsimus2.lisaValik(new Valik(
                "Palun kõigil kontrollida oma arvuteid antiviiruse programmiga",
                "Mõned seadmed jäid kontrollimata ja lunavara levis edasi.",
                "Lihtsalt antiviiruse kontrolli palumine ei ole piisavalt süsteemne lähenemine. See jätab \n" +
                        "liiga palju töötajate endi otsustada ja ei taga, et kõik seadmed kontrollitakse. Lisaks ei \n" +
                        "pruugi antiviirus tuvastada kõiki lunavara variante.",
                -5
        ));

        küsimus2.lisaValik(new Valik(
                "Välista ainult maksuameti kirjad e-posti filtris",
                "Järgmine rünnak tuli 'panga' nimel ja see polnud blokeeritud.",
                "Ainult ühe konkreetse saatja blokeerimine ei ole tõhus lahendus. Phishing-kirjad võivad tulla \n" +
                        "paljudelt erinevatelt väidetavatelt saatjatelt. See on reaktiivne, mitte ennetav lähenemine.",
                0
        ));

        küsimus2.lisaValik(new Valik(
                "Keela ajutiselt kõik e-posti manused, tee süsteemne kontroll kõigile seadmetele ja korralda kiirkoolitus",
                "Eeskujulik tegutsemine! Lunavara levikut peatati, kõik seadmed kontrolliti ja töötajad said koolitust.",
                "See on terviklik lähenemine probleemile. Ajutine manuste keelamine peatab kohese ohu, süsteemne \n" +
                        "kontroll tagab, et kõik seadmed on turvalised, ja koolitus aitab vältida tulevasi intsidente. See \n" +
                        "on nii reaktiivne (praeguse probleemi lahendamine) kui ka ennetav (tulevaste probleemide vältimine).",
                10
        ));

        // Lisame küsimuse
        stsenaarium.lisaKüsimus(küsimus2);

        return stsenaarium;
    }
}
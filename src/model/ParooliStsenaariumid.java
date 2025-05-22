package model;
import java.util.ArrayList;
import java.util.List;

/**
 * ParooliStsenaariumid klass loob paroolide tugevuse testimise stsenaariumeid.
 * See klass sisaldab erinevaid paroolidega seonduvaid stsenaariumeid, mida
 * kasutaja saab simulaatoris läbida.
 *
 * @author Kevin Laig, Kaili Must
 */

public class ParooliStsenaariumid {
    /**
     * Loob kõik paroolide stsenaariumid.
     *
     * @return parooli stsenaariumite loend
     */
    public List<Stsenaarium> looStsenaariumid() {
        List<Stsenaarium> stsenaariumid = new ArrayList<>();

        // Lisame stsenaariumid loetellu
        stsenaariumid.add(looparooliTugevuseStsenaarium());

        return stsenaariumid;
    }

    private Stsenaarium looparooliTugevuseStsenaarium() {
        Stsenaarium stsenaarium = new Stsenaarium(
                "Sotsiaalmeedia kontoloomine",
                "Hakkad looma uut sotsiaalmeedia kontot.",
                2,
                "Paroolid"
        );

        // Loome esimese küsimuse
        Küsimus küsimus1 = new Küsimus(
                "Oled valimas loodud kontole parooli. Milline alljärgnevatest paroolidest on kõige turvalisem valik?"
        );

        // Lisame valikud
        küsimus1.lisaValik(new Valik(
                "123456",
                "Liiga kerge parool. Sinu konto võetu üle ning sul puudub sellele ligipääs.",
                "Taolised paroolid nagu 0000, 123456, parool, parool123 on enimkasutatavad ning väga lihtsasti äraarvatavad.",
                -10
        ));

        küsimus1.lisaValik(new Valik(
                "Karm4!Par00l2025",
                "Väga hea! Sinu konto on kaitstud turvalise parooliga ning rünnaku korral jäi sinu konto alles.",
                "Hästi tehtud! Sinu konto on kaitstud!",
                10
        ));

        küsimus1.lisaValik(new Valik(
                "password",
                "Liiga kerge parool. Sinu konto võetu üle ning sul puudub sellele ligipääs.",
                "Taolised paroolid nagu parool, parool123, password on enimkasutatavad ning väga lihtsasti äraarvatavad.",
                -5
        ));

        // Lisame küsimuse stsenaariumile
        stsenaarium.lisaKüsimus(küsimus1);

        // Loome teise küsimuse
        Küsimus küsimus2 = new Küsimus(
                "Konto loomisel pakutakse võimalust valida turvaküsimus, mille korral saad parooli unustamise korral määrata uue parooli. Millise turvaküsimuse valid?"
        );

        // Lisame valikud
        küsimus2.lisaValik(new Valik(
                "Mis on minu ema nimi?",
                "Sinu tuttav proovis pääseda sinu kontole ligi. Ta teadis sinu ema nime ning sai läbi turvaküsimusest. Sinu tuttav määras uue parooli ning pääses su kontole ligi. Ta luges läbi kõik sinu kirjavahetused.",
                "Turvaküsimuseks tuleks valida küsimus, mille vastust tead vaid sina.",
                -10
        ));

        küsimus2.lisaValik(new Valik(
                "Mis linnas ma sündisin?",
                "Sinu tuttav proovis pääseda sinu kontole ligi. Ta teadis sinu sünnilinna ning sai läbi turvaküsimusest. Sinu tuttav määras uue parooli ning pääses su kontole ligi. Ta luges läbi kõik sinu kirjavahetused.",
                "Turvaküsimuseks tuleks valida küsimus, mille vastust tead vaid sina.",
                -5
        ));

        küsimus2.lisaValik(new Valik(
                "Mis on minu õnnenumber?",
                "Suurepärane! Sinu turvaküsimus on unikaalne ning selle vastust tead tõenäoliselt vaid sina.",
                "Turvaküsimuse määramine ei ole tegelikult kõige parem viis parooli lähtestamiseks. Kõige lähedasemad inimesed võivad turvaküsimustest lihtsa vaevaga mööda pääseda. Kui aga siiski soovid kasutada turvaküsimust, tuleb selleks valida unikaalne küsimus, mille vastust tead vaid sina.",
                10
        ));

        // Lisame küsimuse stsenaariumile
        stsenaarium.lisaKüsimus(küsimus2);

        // Loome kolmanda küsimuse
        Küsimus küsimus3 = new Küsimus(
                "Oled unustanud enda parooli ega pääse enam sotsiaalmeedia kontosse. Mida sa teed?"
        );

        // Lisame valikud
        küsimus3.lisaValik(new Valik(
                "Saadan parooli taastamise lingi e-posti aadressile ja kasutan seda, et luua uus parool.",
                "Sinu parooli taastamiseks saadeti e-postile link, mille abil saad valida uue parooli.",
                "Kui oled unustanud mõne konto parooli, on alati kõige turvalisem saata enda e-posti aadressile link, mille abil on võimalik määrata enda kontole uus parool.",
                10
        ));

        küsimus3.lisaValik(new Valik(
                "Küsin parooli taastamisel abi tuttava käest. Tuttav aitab luua uue parooli.",
                "Läksite tuttavaga tülli. Teie tuttav otsustas maksta kätte ning logis teie parooliga teie kontosse sisse ja otsustas teie nimel edastada teie sõpradele sobimatu sisuga linke. Mitu sõpra avasid lingi ning said teie peale pahaseks.",
                "Tuttava abil uue parooli loomine on ohtlik, sest püsib oht, et teie tuttav võib teie parooli teadmisel teie kontot kuritarvitada.",
                -5
        ));

        küsimus3.lisaValik(new Valik(
                "Leidsin internetist kontaktid Aasias tegutsevast ettevõttest, kes aitavad TASUTA parooli taastada",
                "SUUR VIGA! Aasias tegutsevat ettevõtet ei eksisteeri. Ettevõtte taga seisid kurjategijad, kes võtsid sinu konto üle ja alustasid phishinguga. Kaks sinu sõpra jäid neid uskuma ning kaotasid enda pangakontolt raha.",
                "Enda andmeid ei tohiks kunagi jagada. Internetis tasuta parooli taastamise teenuseid pakkuvad ettevõtted tegutsevad seadusvastaselt ning nende heasüdamlik eesmärk ei ole eluliselt usutav.",
                -10
        ));

        // Lisame küsimuse stsenaariumile
        stsenaarium.lisaKüsimus(küsimus3);

        return stsenaarium;
    }
}
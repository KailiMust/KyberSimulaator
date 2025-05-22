package model;
import java.util.ArrayList;
import java.util.List;

import view.KasutajaLiides;

/**
 * Stsenaarium klass esindab ühte terviklikku küberturvalisuse stsenaariumit
 * koos kirjelduse ja küsimustega. Klass haldab stsenaariumi esitamist kasutajale
 * ja tulemuste arvutamist.
 *
 * @author Kevin Laig, Kaili Must
 */
public class Stsenaarium {
    /** Stsenaariumi pealkiri */
    private String pealkiri;

    /** Stsenaariumi pikk kirjeldus */
    private String kirjeldus;

    /** Stsenaariumi raskusaste skaalal 1-5 */
    private int raskusaste;

    /** Stsenaariumi kategooria (nt "Phishing") */
    private String kategooria;

    /** Stsenaariumi küsimuste loend */
    private List<Küsimus> küsimused;

    /** Kasutaja teenitud skoor stsenaariumis */
    private int skoor;

    /**
     * Konstruktor, mis loob uue stsenaariumi määratud parameetritega.
     *
     * @param pealkiri stsenaariumi pealkiri
     * @param kirjeldus stsenaariumi kirjeldus
     * @param raskusaste stsenaariumi raskusaste skaalal 1-5
     * @param kategooria stsenaariumi kategooria (nt "Phishing")
     */
    public Stsenaarium(String pealkiri, String kirjeldus, int raskusaste, String kategooria) {
        this.pealkiri = pealkiri;
        this.kirjeldus = kirjeldus;
        this.raskusaste = raskusaste;
        this.kategooria = kategooria;
        this.küsimused = new ArrayList<>();
        this.skoor = 0;
    }

    /**
     * Lisab stsenaariumile küsimuse.
     *
     * @param küsimus lisatav küsimus
     */
    public void lisaKüsimus(Küsimus küsimus) {
        küsimused.add(küsimus);
    }

    /**
     * Esitab stsenaariumi kasutajale ja töötleb vastused.
     *
     * @param kasutajaLiides kasutajaliides suhtlemiseks
     * @return kasutaja teenitud skoor
     */
    public int esita(KasutajaLiides kasutajaLiides) {
        // Lähtestame skoori
        skoor = 0;

        // Näitame stsenaariumi pealkirja ja kirjeldust
        kasutajaLiides.näitaTeade("\n===== " + pealkiri + " =====");
        kasutajaLiides.näitaTeade("Raskusaste: " + raskusaste + "/5");
        kasutajaLiides.näitaTeade("\n" + kirjeldus + "\n");

        // Esitame iga küsimuse ja töötleme vastuse
        for (Küsimus küsimus : küsimused) {
            Valik valik = küsimus.esitaJaSaaValik(kasutajaLiides);

            // Kui valik on null, siis kasutaja tahtis stsenaariumist väljuda
            if (valik == null) {
                kasutajaLiides.näitaTeade("Väljud stsenaariumist...");
                return skoor;
            }

            // Lisame valiku skoori koguskoorile
            skoor += valik.getSkoor();
        }

        // Näitame kokkuvõtet
        näitaKokkuvõte(kasutajaLiides);

        return skoor;
    }

    /**
     * Näitab stsenaariumist õpitud kokkuvõtet pärast läbimist.
     *
     * @param kasutajaLiides kasutajaliides suhtlemiseks
     */
    private void näitaKokkuvõte(KasutajaLiides kasutajaLiides) {
        kasutajaLiides.näitaTeade("\n===== KOKKUVÕTE =====");
        kasutajaLiides.näitaTeade("Oled läbinud stsenaariumi: " + pealkiri);
        kasutajaLiides.näitaTeade("Sinu lõplik skoor: " + skoor + " punkti");

        // Lisame siia kokkuvõtliku hariduslikku sisu sõltuvalt kategooriast
        switch (kategooria.toLowerCase()) {
            case "phishing":
                kasutajaLiides.näitaTeade("\nÕPITUD:\n" +
                        "Phishing on üks levinumaid küberohte, kus petturid püüavad inimesi petta, et saada " +
                        "ligipääsu tundlikele andmetele, kasutajanimedele või paroolidele.\n\n" +
                        "PARIMAD TAVAD:\n" +
                        "- Kontrolli alati e-kirja saatja aadress hoolikalt\n" +
                        "- Ole ettevaatlik kahtlaste linkide ja manustega\n" +
                        "- Ära kunagi avalda tundlikku infot ilma saatja identiteeti kontrollimata");
                break;

            case "võrguturve":
                kasutajaLiides.näitaTeade("\nÕPITUD:\n" +
                        "Võrguturve hõlmab meetmeid, mis tagavad sinu arvutivõrgu ja andmete kaitse " +
                        "volitamata juurdepääsu ja rünnakute eest.\n\n" +
                        "PARIMAD TAVAD:\n" +
                        "- Kasuta alati tugevat WiFi parooli ja WPA2/WPA3 krüpteeringut\n" +
                        "- Hoia kõik võrguseadmed (ruuterid jms) uuendatuna\n" +
                        "- Kasuta VPN-i avalikes WiFi võrkudes");
                break;

            case "sotsiaalne manipulatsioon":
                kasutajaLiides.näitaTeade("\nÕPITUD:\n" +
                        "Sotsiaalne manipulatsioon kasutab psühholoogilisi võtteid, et meelitada inimesi " +
                        "avaldama tundlikku infot või teostama tegevusi, mis kompromiteerivad turvalisust.\n\n" +
                        "PARIMAD TAVAD:\n" +
                        "- Kontrolli alati isikutuvastust, kui keegi küsib tundlikku infot\n" +
                        "- Ole kahtlustav ootamatute kõnede või külastuste suhtes\n" +
                        "- Järgi alati ettevõtte turvaprotokolle");
                break;

            case "süsteemi turvalisus":
                kasutajaLiides.näitaTeade("\nÕPITUD:\n" +
                        "Süsteemi turvalisus hõlmab teie arvuti ja tarkvara kaitsmist pahavara, viiruste ja " +
                        "teiste ohtude eest.\n\n" +
                        "PARIMAD TAVAD:\n" +
                        "- Hoia operatsioonisüsteem ja tarkvara alati uuendatuna\n" +
                        "- Kasuta tugevat viirusetõrjet ja pahavaratõrjet\n" +
                        "- Varunda regulaarselt olulised andmed");
                break;

            default:
                kasutajaLiides.näitaTeade("\nÕPITUD:\n" +
                        "Küberturvalisus on iga kasutaja vastutus. Hoides end kursis ohtude ja " +
                        "kaitsevahenditega, saad kaitsta ennast ja oma organisatsiooni.");
        }
    }

    /**
     * Tagastab stsenaariumi pealkirja.
     *
     * @return stsenaariumi pealkiri
     */
    public String getPealkiri() {
        return pealkiri;
    }

    /**
     * Tagastab stsenaariumi kategooria.
     *
     * @return stsenaariumi kategooria
     */
    public String getKategooria() {
        return kategooria;
    }

    /**
     * Tagastab stsenaariumi kirjelduse.
     *
     * @return stsenaariumi kirjeldus
     */
    public String getKirjeldus() {
        return kirjeldus;
    }

    /**
     * Tagastab stsenaariumi kokkuvõtte.
     *
     * @return stsenaariumi kokkuvõte
     */
    public String getKokkuvõte() {
        // Loome kokkuvõtte sõltuvalt kategooriast
        StringBuilder kokkuvõte = new StringBuilder();

        switch (kategooria.toLowerCase()) {
            case "phishing":
                kokkuvõte.append("Phishing on üks levinumaid küberohte, kus petturid püüavad inimesi petta, et saada ")
                        .append("ligipääsu tundlikele andmetele, kasutajanimedele või paroolidele.\n\n")
                        .append("PARIMAD TAVAD:\n")
                        .append("- Kontrolli alati e-kirja saatja aadress hoolikalt\n")
                        .append("- Ole ettevaatlik kahtlaste linkide ja manustega\n")
                        .append("- Ära kunagi avalda tundlikku infot ilma saatja identiteeti kontrollimata");
                break;

            case "võrguturve":
                kokkuvõte.append("Võrguturve hõlmab meetmeid, mis tagavad sinu arvutivõrgu ja andmete kaitse ")
                        .append("volitamata juurdepääsu ja rünnakute eest.\n\n")
                        .append("PARIMAD TAVAD:\n")
                        .append("- Kasuta alati tugevat WiFi parooli ja WPA2/WPA3 krüpteeringut\n")
                        .append("- Hoia kõik võrguseadmed (ruuterid jms) uuendatuna\n")
                        .append("- Kasuta VPN-i avalikes WiFi võrkudes");
                break;

            case "sotsiaalne manipulatsioon":
                kokkuvõte.append("Sotsiaalne manipulatsioon kasutab psühholoogilisi võtteid, et meelitada inimesi ")
                        .append("avaldama tundlikku infot või teostama tegevusi, mis kompromiteerivad turvalisust.\n\n")
                        .append("PARIMAD TAVAD:\n")
                        .append("- Kontrolli alati isikutuvastust, kui keegi küsib tundlikku infot\n")
                        .append("- Ole kahtlustav ootamatute kõnede või külastuste suhtes\n")
                        .append("- Järgi alati ettevõtte turvaprotokolle");
                break;

            case "süsteemi turvalisus":
                kokkuvõte.append("Süsteemi turvalisus hõlmab teie arvuti ja tarkvara kaitsmist pahavara, viiruste ja ")
                        .append("teiste ohtude eest.\n\n")
                        .append("PARIMAD TAVAD:\n")
                        .append("- Hoia operatsioonisüsteem ja tarkvara alati uuendatuna\n")
                        .append("- Kasuta tugevat viirusetõrjet ja pahavaratõrjet\n")
                        .append("- Varunda regulaarselt olulised andmed");
                break;

            default:
                kokkuvõte.append("Küberturvalisus on iga kasutaja vastutus. Hoides end kursis ohtude ja ")
                        .append("kaitsevahenditega, saad kaitsta ennast ja oma organisatsiooni.");
        }

        return kokkuvõte.toString();
    }

    /**
     * Tagastab stsenaariumi küsimused.
     *
     * @return stsenaariumi küsimuste loend
     */
    public List<Küsimus> getKüsimused() {
        return küsimused;
    }
}
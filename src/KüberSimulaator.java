/**
 * KüberSimulaator on programmi peaklass, mis käivitab küberturvalisuse
 * stsenaariumi simulaatori ja koordineerib teiste klasside tööd.
 * See klass haldab kasutajaga suhtlemist, programmi peamenüüd ja 
 * stsenaariumite käivitamist.
 *
 * @author Kevin Laig, Kaili Must
 */
public class KüberSimulaator {
    /** StsenaariumiHaldur stsenaariumite laadimiseks ja valimiseks */
    private static StsenaariumiHaldur haldur;

    /** KasutajaLiides kasutajaga suhtlemiseks */
    private static KasutajaLiides kasutajaLiides;

    /**
     * Programmi käivituspunkt. Töötleb käsurea argumente ja käivitab simulaatori.
     *
     * @param args käsurea argumendid (nt. --joptionpane, --kategooria=phishing)
     */
    public static void main(String[] args) {
        // Töötleme käsurea argumendid
        boolean kasutaJOptionPane = false;
        String kategooria = null;

        for (String arg : args) {
            if (arg.equals("--joptionpane")) {
                kasutaJOptionPane = true;
            } else if (arg.startsWith("--kategooria=")) {
                kategooria = arg.substring(13);
            }
        }

        // Initsialiseerime kasutajaliidese
        kasutajaLiides = new KasutajaLiides(kasutaJOptionPane);

        // Initsialiseerime stsenaariumite halduri
        haldur = new StsenaariumiHaldur();

        // Käivitame simulaatori
        alusta(kategooria);
    }

    /**
     * Alustab simulaatorit, näitab tervitust ja käivitab peamenüü.
     *
     * @param kategooria soovitud stsenaariumite kategooria (võib olla null)
     */
    private static void alusta(String kategooria) {
        // Näitame tervitust
        kasutajaLiides.näitaTeade("===================================");
        kasutajaLiides.näitaTeade("  KÜBERTURVALISUSE SIMULAATOR");
        kasutajaLiides.näitaTeade("===================================");
        kasutajaLiides.näitaTeade("Tere tulemast küberturvalisuse stsenaariumite simulaatorisse!");
        kasutajaLiides.näitaTeade("Siin saad harjutada reageerimist erinevatele küberturvalisuse olukordadele.");

        // Kui kategooria on määratud, käivitame kohe selle kategooria stsenaariumi
        if (kategooria != null) {
            Stsenaarium stsenaarium = haldur.saaKategooriast(kategooria);
            if (stsenaarium != null) {
                käivitaStsenaarium(stsenaarium);
            } else {
                kasutajaLiides.näitaViga("Kategooriat '" + kategooria + "' ei leitud!");
                näitaMenüü();
            }
        } else {
            // Näitame peamenüüd (puhastame puhvri enne seda)
            kasutajaLiides.puhastaPuhver();
            näitaMenüü();
        }
    }

    /**
     * Näitab peamenüüd ja töötleb kasutaja valikuid.
     */
    private static void näitaMenüü() {
        boolean jätkame = true;

        while (jätkame) {
            kasutajaLiides.näitaTeade("\nPEAMENÜÜ:");
            kasutajaLiides.näitaTeade("1. Alusta juhuslikku stsenaariumit");
            kasutajaLiides.näitaTeade("2. Vali kategooria");
            kasutajaLiides.näitaTeade("3. Kuva juhised");
            kasutajaLiides.näitaTeade("4. Välju");

            String valik = kasutajaLiides.küsiSisend("Vali tegevus (1-4):");

            switch (valik) {
                case "1":
                    // Käivitame juhusliku stsenaariumi
                    Stsenaarium juhuslik = haldur.juhuslikStsenaarium();
                    if (juhuslik != null) {
                        käivitaStsenaarium(juhuslik);
                    } else {
                        kasutajaLiides.näitaViga("Stsenaariumite laadimine ebaõnnestus!");
                    }
                    break;

                case "2":
                    // Näitame kategooriate valikut
                    näitaKategooriad();
                    break;

                case "3":
                    // Kuvame juhised
                    näitaJuhised();
                    break;

                case "4":
                    // Väljume programmist
                    jätkame = false;
                    kasutajaLiides.näitaTeade("Täname kasutamast! Head päeva!");
                    break;

                default:
                    kasutajaLiides.näitaViga("Palun vali number 1-4");
            }
        }
    }

    /**
     * Näitab kategooriate valikumenüüd ja käivitab valitud kategooria stsenaariumi.
     */
    private static void näitaKategooriad() {
        String[] kategooriad = haldur.saaKategooriad();

        kasutajaLiides.näitaTeade("\nVALI KATEGOORIA:");
        for (int i = 0; i < kategooriad.length; i++) {
            kasutajaLiides.näitaTeade((i+1) + ". " + kategooriad[i]);
        }
        kasutajaLiides.näitaTeade((kategooriad.length+1) + ". Tagasi peamenüüsse");

        int valik = 0;
        try {
            String sisend = kasutajaLiides.küsiSisend("Vali kategooria (1-" + (kategooriad.length+1) + "):");
            valik = Integer.parseInt(sisend);
        } catch (NumberFormatException e) {
            kasutajaLiides.näitaViga("Palun sisesta number!");
            return;
        }

        // Kontrollime, kas valik on kehtiv
        if (valik > 0 && valik <= kategooriad.length) {
            Stsenaarium stsenaarium = haldur.saaKategooriast(kategooriad[valik-1]);
            if (stsenaarium != null) {
                käivitaStsenaarium(stsenaarium);
            } else {
                kasutajaLiides.näitaViga("Valitud kategoorias pole stsenaariumeid!");
            }
        } else if (valik == kategooriad.length + 1) {
            // Tagasi peamenüüsse
            return;
        } else {
            kasutajaLiides.näitaViga("Palun vali õige number!");
        }
    }

    /**
     * Käivitab valitud stsenaariumi ja näitab selle tulemusi.
     *
     * @param stsenaarium käivitatav stsenaarium
     */
    private static void käivitaStsenaarium(Stsenaarium stsenaarium) {
        if (stsenaarium == null) {
            kasutajaLiides.näitaViga("Stsenaariumit ei leitud!");
            return;
        }

        // Puhastame sõnumipuhvri enne uue stsenaariumi käivitamist
        kasutajaLiides.puhastaPuhver();

        // Käivitame stsenaariumi
        int skoor = stsenaarium.esita(kasutajaLiides);

        // Näitame tulemust
        kasutajaLiides.näitaTeade("\nStsenaarium lõpetatud!");
        kasutajaLiides.näitaTeade("Sinu skoor: " + skoor + " punkti");

        // Küsime, kas soovib uuesti mängida
        String valik = kasutajaLiides.küsiSisend("\nKas soovid veel ühte stsenaariumit proovida? (jah/ei)");
        if (valik.equalsIgnoreCase("jah") || valik.equalsIgnoreCase("j")) {
            Stsenaarium uus = haldur.juhuslikStsenaarium();
            käivitaStsenaarium(uus);
        }
    }

    /**
     * Näitab programmi kasutusjuhiseid.
     */
    private static void näitaJuhised() {
        kasutajaLiides.näitaTeade("\nKASUTUSJUHIS:");
        kasutajaLiides.näitaTeade("====================================");
        kasutajaLiides.näitaTeade("Küberturvalisuse simulaator võimaldab sul harjutada reageerimist");
        kasutajaLiides.näitaTeade("erinevatele küberturvalisuse olukordadele turvalisel viisil.");
        kasutajaLiides.näitaTeade("");
        kasutajaLiides.näitaTeade("STSENAARIUMITES NAVIGEERIMINE:");
        kasutajaLiides.näitaTeade("- Loe hoolikalt stsenaariumi kirjeldust");
        kasutajaLiides.näitaTeade("- Vasta küsimustele, sisestades valiku numbri");
        kasutajaLiides.näitaTeade("- Lisateabe küsimiseks sisesta \"?info [teema]\" (nt \"?info saatja\")");
        kasutajaLiides.näitaTeade("- Stsenaariumist väljumiseks sisesta \"välju\"");
        kasutajaLiides.näitaTeade("");
        kasutajaLiides.näitaTeade("IGA STSENAARIUM ÕPETAB SULLE MIDAGI KÜBERTURVALISUSE KOHTA!");
        kasutajaLiides.näitaTeade("====================================");

        kasutajaLiides.küsiSisend("\nVajuta Enter, et jätkata...");
    }
}
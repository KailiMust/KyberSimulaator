package controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import model.ParooliStsenaariumid;
import model.PhishingStsenaariumid;
import model.Stsenaarium;

/**
 * StsenaariumiHaldur klass haldab kõiki saadaolevaid stsenaariumeid ja
 * tegeleb nende valimisega. Klass võimaldab valida juhuslikku stsenaariumit
 * või leida stsenaariumit kategooria järgi.
 *
 * @author Kevin Laig, Kaili Must
 */
public class StsenaariumiHaldur {
    /** Kõikide stsenaariumite loend */
    private List<Stsenaarium> stsenaariumid;

    /** Juhuslike valikute tegemiseks */
    private Random juhuslik;

    /**
     * Konstruktor, mis initsialiseerib stsenaariumite kogu.
     */
    public StsenaariumiHaldur() {
        stsenaariumid = new ArrayList<>();
        juhuslik = new Random();

        // Laadime kõik stsenaariumid
        laadiStsenaariumid();
    }

    /**
     * Laadib kõik stsenaariumid erinevatest kategooriatest.
     */
    private void laadiStsenaariumid() {
        try {
            PhishingStsenaariumid phishing = new PhishingStsenaariumid();
            stsenaariumid.addAll(phishing.looStsenaariumid());

            ParooliStsenaariumid paroolid = new ParooliStsenaariumid();
            stsenaariumid.addAll(paroolid.looStsenaariumid());
        } catch (Exception e) {
            System.err.println("Viga stsenaariumite laadimisel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Tagastab juhusliku stsenaariumi.
     *
     * @return juhuslik stsenaarium või null, kui stsenaariumeid pole
     */
    public Stsenaarium juhuslikStsenaarium() {
        if (stsenaariumid.isEmpty()) {
            return null;
        }

        int indeks = juhuslik.nextInt(stsenaariumid.size());
        return stsenaariumid.get(indeks);
    }

    /**
     * Tagastab stsenaariumi kategooria järgi.
     *
     * @param kategooria soovitud kategooria
     * @return kategooria stsenaarium või null, kui kategooriat pole
     */
    public Stsenaarium saaKategooriast(String kategooria) {
        // Filtreerime kategooria järgi
        List<Stsenaarium> kategooriaStsenaariumid = stsenaariumid.stream()
                .filter(s -> s.getKategooria().equalsIgnoreCase(kategooria))
                .collect(Collectors.toList());

        if (kategooriaStsenaariumid.isEmpty()) {
            return null;
        }

        // Valime juhusliku stsenaariumi kategooriast
        int indeks = juhuslik.nextInt(kategooriaStsenaariumid.size());
        return kategooriaStsenaariumid.get(indeks);
    }

    /**
     * Tagastab kõik saadaolevad kategooriad.
     *
     * @return kategooriate massiiv
     */
    public String[] saaKategooriad() {
        // Kogume kõik unikaalsed kategooriad
        List<String> kategooriad = stsenaariumid.stream()
                .map(Stsenaarium::getKategooria)
                .distinct()
                .collect(Collectors.toList());

        return kategooriad.toArray(new String[0]);
    }
}
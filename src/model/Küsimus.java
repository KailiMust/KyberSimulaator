package model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import view.KasutajaLiides;

/**
 * Küsimus klass esindab stsenaariumis otsustuskohta, kus kasutaja peab
 * valima mitme võimaluse vahel. Optimeeritud versioon vähem koodiridadega.
 *
 * @author Kevin Laig, Kaili Must
 */
public class Küsimus {
    private final String küsimusTekst;
    private final List<Valik> valikud;
    private final Map<String, String> lisaInfo;

    /**
     * Konstruktor, mis loob uue küsimuse.
     */
    public Küsimus(String küsimusTekst) {
        this.küsimusTekst = küsimusTekst;
        this.valikud = new ArrayList<>();
        this.lisaInfo = new HashMap<>();
    }

    /**
     * Lisab küsimusele vastusevariandi.
     */
    public void lisaValik(Valik valik) {
        valikud.add(valik);
    }

    /**
     * Lisab täiendava teabe, mida kasutaja saab pärida ?info käsuga.
     */
    public void lisaInfo(String võti, String väärtus) {
        lisaInfo.put(võti.toLowerCase(), väärtus);
    }

    /**
     * Esitab küsimuse ja tagastab kasutaja valiku.
     * Valikud esitatakse juhuslikus järjekorras.
     */
    public Valik esitaJaSaaValik(KasutajaLiides kasutajaLiides) {
        List<Valik> segatud = new ArrayList<>(valikud);
        Collections.shuffle(segatud);

        while (true) {
            näitaKüsimusJaValikud(kasutajaLiides, segatud);
            String sisend = kasutajaLiides.küsiSisend("\nSinu valik (1-" + segatud.size() + "):");

            // Kontrolli erijuhud
            if (sisend.startsWith("?info")) {
                näitaLisaInfot(sisend.substring(5).trim().toLowerCase(), kasutajaLiides);
                continue;
            }
            
            if (sisend.equalsIgnoreCase("välju")) {
                return null;
            }

            // Proovi tõlgendada numbrina
            Valik valitud = püüaValidaJaTagastaValik(sisend, segatud, kasutajaLiides);
            if (valitud != null) {
                valitud.näitaTulemus(kasutajaLiides);
                return valitud;
            }
        }
    }

    /**
     * Näitab küsimuse teksti ja valikuvariante.
     */
    private void näitaKüsimusJaValikud(KasutajaLiides liides, List<Valik> segatud) {
        liides.näitaTeade("\n" + küsimusTekst);
        
        for (int i = 0; i < segatud.size(); i++) {
            liides.näitaTeade((i + 1) + ". " + segatud.get(i).getTekst());
        }
        
        if (!lisaInfo.isEmpty()) {
            liides.näitaTeade("\nLisainfo küsimiseks kirjuta: ?info [teema]");
            liides.näitaTeade("Saadaval teemad: " + String.join(", ", lisaInfo.keySet()));
        }
    }

    /**
     * Püüab validada sisendi ja tagastada vastava valiku.
     */
    private Valik püüaValidaJaTagastaValik(String sisend, List<Valik> segatud, KasutajaLiides liides) {
        try {
            int valikuIndeks = Integer.parseInt(sisend) - 1;
            
            if (valikuIndeks >= 0 && valikuIndeks < segatud.size()) {
                return segatud.get(valikuIndeks);
            } else {
                liides.näitaViga("Palun vali kehtiv number 1-" + segatud.size());
            }
        } catch (NumberFormatException e) {
            liides.näitaViga("Palun sisesta number või ?info [teema]");
        }
        return null;
    }

    /**
     * Näitab küsitud lisateavet.
     */
    private void näitaLisaInfot(String teema, KasutajaLiides kasutajaLiides) {
        if (lisaInfo.containsKey(teema)) {
            kasutajaLiides.näitaTeade("\nLISAINFO - " + teema.toUpperCase() + ":\n" + lisaInfo.get(teema));
        } else {
            kasutajaLiides.näitaViga("Teemat '" + teema + "' ei leitud. Saadaval teemad: " +
                    String.join(", ", lisaInfo.keySet()));
        }
    }
    
    // Getterid
    public String getKüsimusTekst() { return küsimusTekst; }
    public List<Valik> getValikud() { return valikud; }
}
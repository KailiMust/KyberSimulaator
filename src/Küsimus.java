import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Küsimus klass esindab stsenaariumis otsustuskohta, kus kasutaja peab
 * valima mitme võimaluse vahel. Klass haldab küsimuse esitamist ja
 * kasutaja vastuse töötlemist.
 *
 * @author Kevin Laig, Kaili Must
 */
public class Küsimus {
    /** Küsimuse tekst */
    private String küsimusTekst;

    /** Vastusevariandid */
    private List<Valik> valikud;

    /** Täiendav teave, mida kasutaja saab pärida */
    private Map<String, String> lisaInfo;

    // Removed unused Random field

    /**
     * Konstruktor, mis loob uue küsimuse.
     *
     * @param küsimusTekst küsimuse tekst
     */
    public Küsimus(String küsimusTekst) {
        this.küsimusTekst = küsimusTekst;
        this.valikud = new ArrayList<>();
        this.lisaInfo = new HashMap<>();
    }

    /**
     * Lisab küsimusele vastusevariandi.
     *
     * @param valik lisatav valik
     */
    public void lisaValik(Valik valik) {
        valikud.add(valik);
    }

    /**
     * Lisab täiendava teabe, mida kasutaja saab pärida ?info käsuga.
     *
     * @param võti teabe võti (nt "saatja")
     * @param väärtus teabe väärtus (nt info saatja kohta)
     */
    public void lisaInfo(String võti, String väärtus) {
        lisaInfo.put(võti.toLowerCase(), väärtus);
    }

    /**
     * Esitab küsimuse ja tagastab kasutaja valiku.
     * Valikud esitatakse juhuslikus järjekorras.
     *
     * @param kasutajaLiides kasutajaliides suhtlemiseks
     * @return kasutaja tehtud valik või null, kui kasutaja tahab väljuda
     */
    public Valik esitaJaSaaValik(KasutajaLiides kasutajaLiides) {
        // Segame valikud juhuslikus järjekorras
        List<Valik> segatud = new ArrayList<>(valikud);
        Collections.shuffle(segatud);

        while (true) {
            // Näitame küsimuse teksti
            kasutajaLiides.näitaTeade("\n" + küsimusTekst);

            // Näitame valikuvariante
            for (int i = 0; i < segatud.size(); i++) {
                kasutajaLiides.näitaTeade((i+1) + ". " + segatud.get(i).getTekst());
            }

            // Näitame infot lisainfo küsimise kohta, kui see on saadaval
            if (!lisaInfo.isEmpty()) {
                kasutajaLiides.näitaTeade("\nLisainfo küsimiseks kirjuta: ?info [teema]");
                kasutajaLiides.näitaTeade("Saadaval teemad: " + String.join(", ", lisaInfo.keySet()));
            }

            // Küsime kasutaja vastust
            String sisend = kasutajaLiides.küsiSisend("\nSinu valik (1-" + segatud.size() + "):");

            // Kontrollime, kas kasutaja tahab lisainfot
            if (sisend.startsWith("?info")) {
                String teema = sisend.substring(5).trim().toLowerCase();
                näitaLisaInfot(teema, kasutajaLiides);
                continue;
            }

            // Kontrollime, kas kasutaja tahab väljuda
            if (sisend.equalsIgnoreCase("välju")) {
                return null;
            }

            // Proovime tõlgendada sisendi numbrina
            try {
                int valikuIndeks = Integer.parseInt(sisend) - 1;

                // Kontrollime, kas valik on kehtiv
                if (valikuIndeks >= 0 && valikuIndeks < segatud.size()) {
                    Valik valitud = segatud.get(valikuIndeks);

                    // Näitame valiku tulemust
                    valitud.näitaTulemus(kasutajaLiides);

                    return valitud;
                } else {
                    kasutajaLiides.näitaViga("Palun vali kehtiv number 1-" + segatud.size());
                }
            } catch (NumberFormatException e) {
                kasutajaLiides.näitaViga("Palun sisesta number või ?info [teema]");
            }
        }
    }

    /**
     * Näitab küsitud lisateavet.
     *
     * @param teema soovitud teema
     * @param kasutajaLiides kasutajaliides suhtlemiseks
     */
    private void näitaLisaInfot(String teema, KasutajaLiides kasutajaLiides) {
        if (lisaInfo.containsKey(teema)) {
            kasutajaLiides.näitaTeade("\nLISAINFO - " + teema.toUpperCase() + ":\n" + lisaInfo.get(teema));
        } else {
            kasutajaLiides.näitaViga("Teemat '" + teema + "' ei leitud. Saadaval teemad: " +
                    String.join(", ", lisaInfo.keySet()));
        }
    }
    
    /**
     * Tagastab küsimuse teksti.
     *
     * @return küsimuse tekst
     */
    public String getKüsimusTekst() {
        return küsimusTekst;
    }
    
    /**
     * Tagastab küsimuse vastusevariandid.
     *
     * @return vastusevariandid
     */
    public List<Valik> getValikud() {
        return valikud;
    }
}
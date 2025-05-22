package model;
import view.KasutajaLiides;

/**
 * Valik klass esindab ühte vastusevarianti küsimusele koos tagajärje
 * ja selgitusega. Klass haldab valiku tulemuse näitamist.
 *
 * @author Kevin Laig, Kaili Must
 */
public class Valik {
    /** Valiku tekst, mida kasutaja näeb */
    private String tekst;

    /** Valiku tagajärg, mis juhtub pärast valimist */
    private String tagajärg;

    /** Hariduslik selgitus, miks see valik oli hea või halb */
    private String selgitus;

    /** Turvalisusskoor, mida kasutaja saab selle valiku eest */
    private int turvalisusSkoor;

    /**
     * Konstruktor, mis loob uue valiku.
     *
     * @param tekst valiku tekst
     * @param tagajärg valiku tagajärg
     * @param selgitus hariduslik selgitus
     * @param turvalisusSkoor saadav skoor
     */
    public Valik(String tekst, String tagajärg, String selgitus, int turvalisusSkoor) {
        this.tekst = tekst;
        this.tagajärg = tagajärg;
        this.selgitus = selgitus;
        this.turvalisusSkoor = turvalisusSkoor;
    }

    /**
     * Näitab valiku tulemust pärast valimist.
     *
     * @param kasutajaLiides kasutajaliides suhtlemiseks
     */
    public void näitaTulemus(KasutajaLiides kasutajaLiides) {
        kasutajaLiides.näitaTeade("\n--- TULEMUS ---");
        kasutajaLiides.näitaTeade(tagajärg);

        String skoorText = (turvalisusSkoor >= 0) ?
                "+" + turvalisusSkoor + " punkti" :
                turvalisusSkoor + " punkti";

        kasutajaLiides.näitaTeade("(" + skoorText + ")");

        kasutajaLiides.näitaTeade("\n--- SELGITUS ---");
        kasutajaLiides.näitaTeade(selgitus);
    }

    /**
     * Tagastab valiku teksti.
     *
     * @return valiku tekst
     */
    public String getTekst() {
        return tekst;
    }

    /**
     * Tagastab valiku turvalisusstkoori.
     *
     * @return valiku skoor
     */
    public int getSkoor() {
        return turvalisusSkoor;
    }
    
    /**
     * Tagastab valiku tagajärje.
     *
     * @return valiku tagajärg
     */
    public String getTagajärg() {
        return tagajärg;
    }
    
    /**
     * Tagastab valiku selgituse.
     *
     * @return valiku selgitus
     */
    public String getSelgitus() {
        return selgitus;
    }
    
    /**
     * Tagastab valiku turvalisusstkoori.
     *
     * @return valiku turvalisusskoor
     */
    public int getTurvalisusSkoor() {
        return turvalisusSkoor;
    }
}
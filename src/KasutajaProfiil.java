import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * KasutajaProfiil klass haldab mängija profiili andmeid.
 * Võimaldab salvestada ja laadida mängija andmeid failist.
 *
 * @author Kevin Laig, Kaili Must
 */
public class KasutajaProfiil {
    /** Mängija nimi */
    private String nimi;
    
    /** Mängija koguskoor */
    private int koguSkoor;
    
    /** Läbitud stsenaariumid ja nende skoorid */
    private Map<String, Integer> läbitudStsenaariumid;
    
    /** Ebaõnnestunud stsenaariumid */
    private List<String> ebaõnnestunudStsenaariumid;
    
    /** Andmete kaust */
    private static final String ANDMETE_KAUST = "data/players/";
    
    /**
     * Konstruktor uue kasutajaprofiili loomiseks.
     *
     * @param nimi mängija nimi
     */
    public KasutajaProfiil(String nimi) {
        this.nimi = nimi;
        this.koguSkoor = 0;
        this.läbitudStsenaariumid = new HashMap<>();
        this.ebaõnnestunudStsenaariumid = new ArrayList<>();
    }
    
    /**
     * Tagastab mängija nime.
     *
     * @return mängija nimi
     */
    public String getNimi() {
        return nimi;
    }
    
    /**
     * Tagastab mängija koguskoori.
     *
     * @return mängija koguskoor
     */
    public int getKoguSkoor() {
        return koguSkoor;
    }
    
    /**
     * Tagastab läbitud stsenaariumite arvu.
     *
     * @return läbitud stsenaariumite arv
     */
    public int getLäbitudStsenaariumiteArv() {
        return läbitudStsenaariumid.size();
    }
    
    /**
     * Tagastab ebaõnnestunud stsenaariumite arvu.
     *
     * @return ebaõnnestunud stsenaariumite arv
     */
    public int getEbaõnnestunudStsenaariumiteArv() {
        return ebaõnnestunudStsenaariumid.size();
    }
    
    /**
     * Lisab läbitud stsenaariumi ja uuendab koguskoori.
     *
     * @param stsenaariumiNimi stsenaariumi nimi
     * @param skoor saadud skoor
     */
    public void lisaLäbitudStsenaarium(String stsenaariumiNimi, int skoor) {
        läbitudStsenaariumid.put(stsenaariumiNimi, skoor);
        koguSkoor += skoor;
        salvestaProfiil();
    }
    
    /**
     * Lisab ebaõnnestunud stsenaariumi.
     *
     * @param stsenaariumiNimi stsenaariumi nimi
     */
    public void lisaEbaõnnestunudStsenaarium(String stsenaariumiNimi) {
        ebaõnnestunudStsenaariumid.add(stsenaariumiNimi);
        salvestaProfiil();
    }
    
    /**
     * Salvestab profiili failisüsteemi.
     *
     * @return true kui salvestamine õnnestus, false vastasel juhul
     */
    public boolean salvestaProfiil() {
        // Loome vajalikud kaustad
        File kasutajaKaust = new File(ANDMETE_KAUST + nimi);
        if (!kasutajaKaust.exists()) {
            kasutajaKaust.mkdirs();
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ANDMETE_KAUST + nimi + "/profile.txt"))) {
            // Kirjutame põhiandmed
            writer.write("NIMI=" + nimi);
            writer.newLine();
            writer.write("SKOOR=" + koguSkoor);
            writer.newLine();
            
            // Kirjutame läbitud stsenaariumid
            writer.write("LÄBITUD_STSENAARIUMID=");
            writer.newLine();
            for (Map.Entry<String, Integer> entry : läbitudStsenaariumid.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
            
            // Kirjutame ebaõnnestunud stsenaariumid
            writer.write("EBAÕNNESTUNUD_STSENAARIUMID=");
            writer.newLine();
            for (String stsenaarium : ebaõnnestunudStsenaariumid) {
                writer.write(stsenaarium);
                writer.newLine();
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Viga profiili salvestamisel: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Laeb profiili failisüsteemist.
     *
     * @param nimi mängija nimi
     * @return laetud profiil või null, kui profiili ei leitud
     */
    public static KasutajaProfiil laeProfiil(String nimi) {
        File profiiliFail = new File(ANDMETE_KAUST + nimi + "/profile.txt");
        
        if (!profiiliFail.exists()) {
            return null;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(profiiliFail))) {
            KasutajaProfiil profiil = new KasutajaProfiil(nimi);
            
            String rida;
            String sektsioon = null;
            
            while ((rida = reader.readLine()) != null) {
                if (rida.startsWith("NIMI=")) {
                    // Ignoreerime, kuna nimi on juba konstruktoris määratud
                } else if (rida.startsWith("SKOOR=")) {
                    profiil.koguSkoor = Integer.parseInt(rida.substring(6));
                } else if (rida.equals("LÄBITUD_STSENAARIUMID=")) {
                    sektsioon = "läbitud";
                } else if (rida.equals("EBAÕNNESTUNUD_STSENAARIUMID=")) {
                    sektsioon = "ebaõnnestunud";
                } else if (sektsioon != null) {
                    if (sektsioon.equals("läbitud") && rida.contains(":")) {
                        String[] osad = rida.split(":");
                        profiil.läbitudStsenaariumid.put(osad[0], Integer.parseInt(osad[1]));
                    } else if (sektsioon.equals("ebaõnnestunud")) {
                        profiil.ebaõnnestunudStsenaariumid.add(rida);
                    }
                }
            }
            
            return profiil;
        } catch (IOException | NumberFormatException e) {
            System.err.println("Viga profiili laadimisel: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Tagastab kõigi olemasolevate mängijate nimed.
     *
     * @return mängijate nimede loend
     */
    public static List<String> saaOlemasolevadMängijad() {
        List<String> mängijad = new ArrayList<>();
        
        File kasutajadKaust = new File(ANDMETE_KAUST);
        if (!kasutajadKaust.exists()) {
            kasutajadKaust.mkdirs();
            return mängijad;
        }
        
        File[] kasutajaKaustad = kasutajadKaust.listFiles(File::isDirectory);
        if (kasutajaKaustad != null) {
            for (File kaust : kasutajaKaustad) {
                if (new File(kaust, "profile.txt").exists()) {
                    mängijad.add(kaust.getName());
                }
            }
        }
        
        return mängijad;
    }
}

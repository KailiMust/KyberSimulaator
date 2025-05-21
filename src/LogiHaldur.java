import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LogiHaldur klass haldab mängija tegevuste logimist.
 * Salvestab mängusessiooni info, kasutaja valikud ja skoorid.
 *
 * @author Kevin Laig, Kaili Must
 */
public class LogiHaldur {
    /** Mängija nimi */
    private final String mängijaNimi;
    
    /** Sessiooni algusaeg */
    private final LocalDateTime sessioonAlgus;
    
    /** Andmete kaust */
    private static final String ANDMETE_KAUST = "data/players/";
    
    /** Kuupäeva formaat */
    private static final DateTimeFormatter KUUPÄEVA_FORMAAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Konstruktor uue logihalduri loomiseks.
     *
     * @param mängijaNimi mängija nimi
     */
    public LogiHaldur(String mängijaNimi) {
        this.mängijaNimi = mängijaNimi;
        this.sessioonAlgus = LocalDateTime.now();
        
        // Loome vajalikud kaustad
        File kasutajaKaust = new File(ANDMETE_KAUST + mängijaNimi);
        if (!kasutajaKaust.exists()) {
            kasutajaKaust.mkdirs();
        }
        
        // Logime sessiooni alguse
        logiSündmus("Sessioon algas");
    }
    
    /**
     * Logib sündmuse koos ajatempliga.
     *
     * @param sündmus logitav sündmus
     */
    public void logiSündmus(String sündmus) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ANDMETE_KAUST + mängijaNimi + "/log.txt", true))) {
            writer.write("[" + LocalDateTime.now().format(KUUPÄEVA_FORMAAT) + "] " + sündmus);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Viga sündmuse logimisel: " + e.getMessage());
        }
    }
    
    /**
     * Logib kasutaja valiku stsenaariumis.
     *
     * @param stsenaariumiNimi stsenaariumi nimi
     * @param küsimus esitatud küsimus
     * @param valik kasutaja tehtud valik
     */
    public void logiValik(String stsenaariumiNimi, String küsimus, String valik) {
        logiSündmus("Stsenaarium: " + stsenaariumiNimi + " | Küsimus: " + küsimus + " | Valik: " + valik);
    }
    
    /**
     * Logib stsenaariumi lõpetamise ja skoori.
     *
     * @param stsenaariumiNimi stsenaariumi nimi
     * @param skoor saadud skoor
     * @param lõpetatud kas stsenaarium lõpetati edukalt
     */
    public void logiStsenaariumiLõpp(String stsenaariumiNimi, int skoor, boolean lõpetatud) {
        if (lõpetatud) {
            logiSündmus("Stsenaarium lõpetatud: " + stsenaariumiNimi + " | Skoor: " + skoor);
        } else {
            logiSündmus("Stsenaarium katkestatud: " + stsenaariumiNimi);
        }
    }
    
    /**
     * Logib sessiooni lõpu ja kokkuvõtte.
     *
     * @param läbitudStsenaariumid läbitud stsenaariumite arv
     * @param koguSkoor kogutud skoor
     */
    public void logiSessioonLõpp(int läbitudStsenaariumid, int koguSkoor) {
        LocalDateTime lõpp = LocalDateTime.now();
        long kestusMinutites = java.time.Duration.between(sessioonAlgus, lõpp).toMinutes();
        
        logiSündmus("Sessioon lõppes | Kestus: " + kestusMinutites + " minutit | " +
                    "Läbitud stsenaariumid: " + läbitudStsenaariumid + " | " +
                    "Koguskoor: " + koguSkoor);
    }
}

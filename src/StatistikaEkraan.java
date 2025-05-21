import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * StatistikaEkraan klass haldab mängija statistika kuvamist JavaFX kasutajaliideses.
 * Näitab mängija koguskoori, läbitud stsenaariumite arvu jms.
 *
 * @author Kevin Laig, Kaili Must
 */
public class StatistikaEkraan {
    /** Peamine lava */
    private final Stage peaLava;
    
    /** Mängija nimi */
    private final String mängijaNimi;
    
    /** Kasutajaprofiil */
    private final KasutajaProfiil profiil;
    
    /** Tagasikutse peamenüüsse naasmiseks */
    private final Runnable tagasiPeamenüüsse;
    
    /**
     * Konstruktor statistika ekraani loomiseks.
     *
     * @param peaLava rakenduse peamine lava
     * @param mängijaNimi mängija nimi
     * @param tagasiPeamenüüsse tagasikutse peamenüüsse naasmiseks
     */
    public StatistikaEkraan(Stage peaLava, String mängijaNimi, Runnable tagasiPeamenüüsse) {
        this.peaLava = peaLava;
        this.mängijaNimi = mängijaNimi;
        this.tagasiPeamenüüsse = tagasiPeamenüüsse;
        
        // Laeme kasutajaprofiili
        KasutajaProfiil laetudProfiil = KasutajaProfiil.laeProfiil(mängijaNimi);
        if (laetudProfiil != null) {
            this.profiil = laetudProfiil;
        } else {
            this.profiil = new KasutajaProfiil(mängijaNimi);
        }
    }
    
    /**
     * Näitab statistika ekraani.
     */
    public void näita() {
        BorderPane juurPaneel = new BorderPane();
        juurPaneel.setPadding(new Insets(20));
        
        // Pealkirja paneel
        Label pealkiri = new Label("Mängija statistika");
        pealkiri.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(pealkiri, Pos.CENTER);
        juurPaneel.setTop(pealkiri);
        
        // Statistika paneel
        VBox statistikaPaneel = new VBox(15);
        statistikaPaneel.setPadding(new Insets(20, 0, 20, 0));
        statistikaPaneel.setAlignment(Pos.CENTER);
        
        Label mängijaNimiLabel = new Label("Mängija: " + mängijaNimi);
        mängijaNimiLabel.setStyle("-fx-font-size: 18px;");
        
        Label koguSkoorLabel = new Label("Koguskoor: " + profiil.getKoguSkoor() + " punkti");
        koguSkoorLabel.setStyle("-fx-font-size: 18px;");
        
        Label läbitudLabel = new Label("Läbitud stsenaariumid: " + profiil.getLäbitudStsenaariumiteArv());
        läbitudLabel.setStyle("-fx-font-size: 16px;");
        
        Label ebaõnnestunudLabel = new Label("Ebaõnnestunud stsenaariumid: " + profiil.getEbaõnnestunudStsenaariumiteArv());
        ebaõnnestunudLabel.setStyle("-fx-font-size: 16px;");
        
        // Lihtne visuaalne esitus
        Label visuaalneLabel = new Label("Edukuse määr: ");
        visuaalneLabel.setStyle("-fx-font-size: 16px;");
        
        int läbitud = profiil.getLäbitudStsenaariumiteArv();
        int ebaõnnestunud = profiil.getEbaõnnestunudStsenaariumiteArv();
        int kokku = läbitud + ebaõnnestunud;
        
        if (kokku > 0) {
            double edukuseMäär = (double) läbitud / kokku;
            String edukuseProtsent = String.format("%.1f", edukuseMäär * 100);
            
            Label edukuseLabel = new Label("Edukuse määr: " + edukuseProtsent + "%");
            edukuseLabel.setStyle("-fx-font-size: 16px;");
            statistikaPaneel.getChildren().add(edukuseLabel);
        }
        
        statistikaPaneel.getChildren().addAll(mängijaNimiLabel, koguSkoorLabel, läbitudLabel, ebaõnnestunudLabel);
        juurPaneel.setCenter(statistikaPaneel);
        
        // Nuppude paneel
        VBox nupudPaneel = new VBox(15);
        nupudPaneel.setAlignment(Pos.CENTER);
        
        Button tagasiBtn = new Button("Tagasi peamenüüsse");
        tagasiBtn.setPrefWidth(200);
        tagasiBtn.setOnAction(_ -> tagasiPeamenüüsse.run());
        
        nupudPaneel.getChildren().add(tagasiBtn);
        juurPaneel.setBottom(nupudPaneel);
        
        // Loome stseeni
        Scene stseen = new Scene(juurPaneel);
        peaLava.setScene(stseen);
    }
}

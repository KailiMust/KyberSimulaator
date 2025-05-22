package view;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.KasutajaProfiil;

/**
 * StatistikaEkraan klass haldab kasutaja statistika kuvamist JavaFX kasutajaliideses.
 * Näitab kasutaja koguskoori, läbitud stsenaariumite arvu jms.
 *
 * @author Kevin Laig, Kaili Must
 */
public class StatistikaEkraan {
    private final Stage peaLava;
    private final String kasutajaNimi;
    private final KasutajaProfiil profiil;
    private final Runnable tagasiPeamenüüsse;
    
    /**
     * Konstruktor statistika ekraani loomiseks.
     *
     * @param peaLava rakenduse peamine lava
     * @param kasutajaNimi kasutaja nimi
     * @param tagasiPeamenüüsse tagasikutse peamenüüsse naasmiseks
     */
    public StatistikaEkraan(Stage peaLava, String kasutajaNimi, Runnable tagasiPeamenüüsse) {
        this.peaLava = peaLava;
        this.kasutajaNimi = kasutajaNimi;
        this.tagasiPeamenüüsse = tagasiPeamenüüsse;
        
        // Laeme kasutajaprofiili
        KasutajaProfiil laetudProfiil = KasutajaProfiil.laeProfiil(kasutajaNimi);
        this.profiil = (laetudProfiil != null) ? laetudProfiil : new KasutajaProfiil(kasutajaNimi);
    }
    
    /**
     * Näitab statistika ekraani.
     */
    public void näita() {
        Scene stseen = looStatistikaEkraan();
        lisaCSS(stseen);
        peaLava.setScene(stseen);
    }
    
    /**
     * Loob statistika ekraani.
     */
    private Scene looStatistikaEkraan() {
        BorderPane juurPaneel = new BorderPane();
        juurPaneel.setPadding(new Insets(20));
        
        // Pealkiri
        Label pealkiri = new Label("Kasutaja statistika");
        pealkiri.getStyleClass().add("title-label");
        BorderPane.setAlignment(pealkiri, Pos.CENTER);
        juurPaneel.setTop(pealkiri);
        
        // Statistika paneel klaasipaneelina
        VBox statistikaPaneel = new VBox(15);
        statistikaPaneel.getStyleClass().add("glass-pane");
        statistikaPaneel.setAlignment(Pos.CENTER);
        
        // Statistika info
        statistikaPaneel.getChildren().addAll(
            looInfoLabel("Kasutaja: " + kasutajaNimi, "18px"),
            looInfoLabel("Koguskoor: " + profiil.getKoguSkoor() + " punkti", "18px"),
            looInfoLabel("Läbitud stsenaariumid: " + profiil.getLäbitudStsenaariumiteArv(), "16px"),
            looInfoLabel("Ebaõnnestunud stsenaariumid: " + profiil.getEbaõnnestunudStsenaariumiteArv(), "16px")
        );
        
        // Lisa edukuse määr, kui on andmeid
        int läbitud = profiil.getLäbitudStsenaariumiteArv();
        int ebaõnnestunud = profiil.getEbaõnnestunudStsenaariumiteArv();
        int kokku = läbitud + ebaõnnestunud;
        
        if (kokku > 0) {
            double edukuseMäär = (double) läbitud / kokku;
            String edukuseProtsent = String.format("%.1f", edukuseMäär * 100);
            statistikaPaneel.getChildren().add(
                looInfoLabel("Edukuse määr: " + edukuseProtsent + "%", "16px")
            );
        }
        
        juurPaneel.setCenter(statistikaPaneel);
        
        // Tagasi nupp
        Button tagasiBtn = new Button("Tagasi peamenüüsse");
        tagasiBtn.setPrefWidth(200);
        tagasiBtn.setOnAction(_ -> tagasiPeamenüüsse.run());
        
        VBox nupudPaneel = new VBox(tagasiBtn);
        nupudPaneel.setAlignment(Pos.CENTER);
        juurPaneel.setBottom(nupudPaneel);
        
        return new Scene(juurPaneel);
    }
    
    /**
     * Loob info labeli määratud stiili ja teksti suurusega.
     */
    private Label looInfoLabel(String tekst, String suurus) {
        Label label = new Label(tekst);
        label.setStyle("-fx-font-size: " + suurus + ";");
        return label;
    }
    
    /**
     * Lisab stseenile CSS stiilid.
     */
    private void lisaCSS(Scene stseen) {
        stseen.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }
}
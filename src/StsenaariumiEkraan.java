import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * StsenaariumiEkraan klass haldab stsenaariumi kuvamist JavaFX kasutajaliideses.
 * Võimaldab kasutajal näha stsenaariumi kirjeldust, vastata küsimustele ja saada tagasisidet.
 *
 * @author Kevin Laig, Kaili Must
 */
public class StsenaariumiEkraan {
    /** Peamine lava */
    private final Stage peaLava;
    
    /** Stsenaarium, mida kuvatakse */
    private final Stsenaarium stsenaarium;
    
    /** Mängija nimi - kasutatakse logi halduri ja kasutajaprofiili loomiseks */
    private final String mängijaNimi;
    
    /** Praegune küsimus */
    private Küsimus praeguneKüsimus;
    
    /** Küsimuste indeks */
    private int küsimuseIndeks = 0;
    
    /** Kogutud skoor */
    private int skoor = 0;
    
    /** Logi haldur */
    private LogiHaldur logiHaldur;
    
    /** Kasutajaprofiil */
    private KasutajaProfiil profiil;
    
    /** Tagasikutse peamenüüsse naasmiseks */
    private Runnable tagasiPeamenüüsse;
    
    /**
     * Konstruktor stsenaariumi ekraani loomiseks.
     *
     * @param peaLava rakenduse peamine lava
     * @param stsenaarium esitatav stsenaarium
     * @param mängijaNimi mängija nimi
     * @param tagasiPeamenüüsse tagasikutse peamenüüsse naasmiseks
     */
    public StsenaariumiEkraan(Stage peaLava, Stsenaarium stsenaarium, String mängijaNimi, Runnable tagasiPeamenüüsse) {
        this.peaLava = peaLava;
        this.stsenaarium = stsenaarium;
        this.mängijaNimi = mängijaNimi;
        this.tagasiPeamenüüsse = tagasiPeamenüüsse;
        
        // Loome logi halduri
        this.logiHaldur = new LogiHaldur(mängijaNimi);
        
        // Laeme või loome kasutajaprofiili
        this.profiil = KasutajaProfiil.laeProfiil(mängijaNimi);
        if (this.profiil == null) {
            this.profiil = new KasutajaProfiil(mängijaNimi);
        }
        
        // Logime stsenaariumi alustamise
        logiHaldur.logiSündmus("Alustas stsenaariumit: " + stsenaarium.getPealkiri());
    }
    
    /**
     * Näitab stsenaariumi ekraani ja alustab stsenaariumi esitamist.
     */
    public void näita() {
        BorderPane juurPaneel = new BorderPane();
        juurPaneel.setPadding(new Insets(20));
        
        // Pealkirja paneel
        Label pealkiri = new Label(stsenaarium.getPealkiri());
        pealkiri.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(pealkiri, Pos.CENTER);
        juurPaneel.setTop(pealkiri);
        
        // Stsenaariumi kirjelduse paneel
        VBox kirjeldusPaneel = new VBox(15);
        kirjeldusPaneel.setPadding(new Insets(20, 0, 20, 0));
        
        TextArea kirjeldus = new TextArea(stsenaarium.getKirjeldus());
        kirjeldus.setWrapText(true);
        kirjeldus.setEditable(false);
        kirjeldus.setPrefHeight(100);
        
        kirjeldusPaneel.getChildren().add(kirjeldus);
        juurPaneel.setCenter(kirjeldusPaneel);
        
        // Nuppude paneel
        HBox nupudPaneel = new HBox(15);
        nupudPaneel.setAlignment(Pos.CENTER);
        
        Button jätkaBtn = new Button("Alusta");
        jätkaBtn.setPrefWidth(150);
        jätkaBtn.setOnAction(_ -> näitaJärgmineKüsimus());
        
        Button väljuBtn = new Button("Välju stsenaariumist");
        väljuBtn.setPrefWidth(150);
        väljuBtn.setOnAction(_ -> katkestaStsenaarium());
        
        nupudPaneel.getChildren().addAll(jätkaBtn, väljuBtn);
        juurPaneel.setBottom(nupudPaneel);
        
        // Loome stseeni
        Scene stseen = new Scene(juurPaneel);
        peaLava.setScene(stseen);
    }
    
    /**
     * Näitab järgmist küsimust stsenaariumis.
     */
    private void näitaJärgmineKüsimus() {
        // Kontrollime, kas stsenaariumis on veel küsimusi
        List<Küsimus> küsimused = stsenaarium.getKüsimused();
        if (küsimuseIndeks >= küsimused.size()) {
            näitaTulemusEkraan();
            return;
        }
        
        praeguneKüsimus = küsimused.get(küsimuseIndeks);
        küsimuseIndeks++;
        
        BorderPane juurPaneel = new BorderPane();
        juurPaneel.setPadding(new Insets(20));
        
        // Pealkirja paneel
        Label pealkiri = new Label(stsenaarium.getPealkiri() + " - Küsimus " + küsimuseIndeks);
        pealkiri.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        BorderPane.setAlignment(pealkiri, Pos.CENTER);
        juurPaneel.setTop(pealkiri);
        
        // Keskmine paneel, mis sisaldab stsenaariumi kirjeldust ja küsimust
        VBox keskmineKontainer = new VBox(15);
        keskmineKontainer.setPadding(new Insets(10, 0, 10, 0));
        
        // Stsenaariumi kirjelduse paneel
        VBox kirjeldusPaneel = new VBox(5);
        kirjeldusPaneel.setPadding(new Insets(0, 0, 10, 0));
        
        Label kirjeldusLabel = new Label("Stsenaariumi kirjeldus:");
        kirjeldusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        TextArea kirjeldus = new TextArea(stsenaarium.getKirjeldus());
        kirjeldus.setWrapText(true);
        kirjeldus.setEditable(false);
        kirjeldus.setPrefHeight(80);
        kirjeldus.setStyle("-fx-control-inner-background: #f8f8f8; -fx-background-color: #f0f0f0;");
        
        kirjeldusPaneel.getChildren().addAll(kirjeldusLabel, kirjeldus);
        keskmineKontainer.getChildren().add(kirjeldusPaneel);
        
        // Küsimuse paneel
        VBox küsimusePaneel = new VBox(10);
        küsimusePaneel.setPadding(new Insets(0, 0, 10, 0));
        
        Label küsimusLabel = new Label("Küsimus:");
        küsimusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        Label küsimusTekst = new Label(praeguneKüsimus.getKüsimusTekst());
        küsimusTekst.setWrapText(true);
        küsimusTekst.setStyle("-fx-font-size: 16px;");
        
        küsimusePaneel.getChildren().addAll(küsimusLabel, küsimusTekst);
        keskmineKontainer.getChildren().add(küsimusePaneel);
        
        // Valikute paneel
        VBox valikutePaneel = new VBox(10);
        Label valikudLabel = new Label("Valikud:");
        valikudLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        valikutePaneel.getChildren().add(valikudLabel);
        
        ToggleGroup valikuGrupp = new ToggleGroup();
        List<RadioButton> valikuNupud = new ArrayList<>();
        
        List<Valik> valikud = praeguneKüsimus.getValikud();
        for (int i = 0; i < valikud.size(); i++) {
            Valik valik = valikud.get(i);
            RadioButton valikuNupp = new RadioButton(valik.getTekst());
            valikuNupp.setToggleGroup(valikuGrupp);
            valikuNupp.setUserData(valik);
            valikuNupp.setWrapText(true);
            valikutePaneel.getChildren().add(valikuNupp);
            valikuNupud.add(valikuNupp);
        }
        
        keskmineKontainer.getChildren().add(valikutePaneel);
        juurPaneel.setCenter(keskmineKontainer);
        
        // Nuppude paneel
        HBox nupudPaneel = new HBox(15);
        nupudPaneel.setAlignment(Pos.CENTER);
        nupudPaneel.setPadding(new Insets(10, 0, 0, 0));
        
        Button infoBtn = new Button("Lisainfo");
        infoBtn.setPrefWidth(100);
        infoBtn.setOnAction(_ -> näitaLisainfo());
        
        Button jätkaBtn = new Button("Jätka");
        jätkaBtn.setPrefWidth(100);
        jätkaBtn.setOnAction(_ -> {
            RadioButton valitudNupp = (RadioButton) valikuGrupp.getSelectedToggle();
            if (valitudNupp != null) {
                Valik valitudValik = (Valik) valitudNupp.getUserData();
                töötleValik(valitudValik);
            }
        });
        
        Button väljuBtn = new Button("Välju");
        väljuBtn.setPrefWidth(100);
        väljuBtn.setOnAction(_ -> katkestaStsenaarium());
        
        nupudPaneel.getChildren().addAll(infoBtn, jätkaBtn, väljuBtn);
        juurPaneel.setBottom(nupudPaneel);
        
        // Loome stseeni
        Scene stseen = new Scene(juurPaneel);
        peaLava.setScene(stseen);
    }
    
    /**
     * Näitab lisainfo dialoogi.
     */
    private void näitaLisainfo() {
        Stage lisainfoLava = new Stage();
        lisainfoLava.initOwner(peaLava);
        lisainfoLava.initModality(javafx.stage.Modality.WINDOW_MODAL);
        lisainfoLava.setTitle("Lisainfo: " + stsenaarium.getPealkiri());
        lisainfoLava.setMinWidth(500);
        lisainfoLava.setMinHeight(300);
        
        BorderPane juurPaneel = new BorderPane();
        juurPaneel.setPadding(new Insets(20));
        
        // Pealkirja paneel
        Label pealkiri = new Label("Lisainfo: " + stsenaarium.getPealkiri());
        pealkiri.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        BorderPane.setAlignment(pealkiri, Pos.CENTER);
        juurPaneel.setTop(pealkiri);
        
        // Lisainfo sisu
        VBox sisuPaneel = new VBox(15);
        sisuPaneel.setPadding(new Insets(20, 0, 20, 0));
        
        StringBuilder lisainfoTekst = new StringBuilder();
        lisainfoTekst.append("Küsimus: ").append(praeguneKüsimus.getKüsimusTekst()).append("\n\n");
        lisainfoTekst.append("Täiendav info küberturvalisuse kohta:\n");
        lisainfoTekst.append("--------------------------------------------------\n");
        lisainfoTekst.append("See küsimus testib teie oskust tuvastada ja reageerida küberohule. ");
        lisainfoTekst.append("Oluline on mõelda kriitiliselt ja hinnata iga valiku võimalikke tagajärgi. ");
        lisainfoTekst.append("Parim valik on tavaliselt see, mis kaitseb teie andmeid ja privaatsust kõige paremini.");
        
        TextArea lisainfoAla = new TextArea(lisainfoTekst.toString());
        lisainfoAla.setWrapText(true);
        lisainfoAla.setEditable(false);
        lisainfoAla.setPrefHeight(200);
        
        sisuPaneel.getChildren().add(lisainfoAla);
        juurPaneel.setCenter(sisuPaneel);
        
        // Sulgemise nupp
        Button sulgeBtn = new Button("Sulge");
        sulgeBtn.setPrefWidth(100);
        sulgeBtn.setOnAction(_ -> lisainfoLava.close());
        
        HBox nupudPaneel = new HBox();
        nupudPaneel.setAlignment(Pos.CENTER);
        nupudPaneel.getChildren().add(sulgeBtn);
        juurPaneel.setBottom(nupudPaneel);
        
        // Loome stseeni
        Scene stseen = new Scene(juurPaneel);
        lisainfoLava.setScene(stseen);
        
        // Näitame dialoogi
        lisainfoLava.showAndWait();
        
        // Logime lisainfo vaatamise
        logiHaldur.logiSündmus("Mängija " + mängijaNimi + " vaatas lisainfot küsimuse kohta");
    }
    
    /**
     * Töötleb kasutaja tehtud valiku.
     *
     * @param valik kasutaja tehtud valik
     */
    private void töötleValik(Valik valik) {
        // Logime valiku
        logiHaldur.logiValik(stsenaarium.getPealkiri(), praeguneKüsimus.getKüsimusTekst(), valik.getTekst());
        
        // Lisame skoori
        skoor += valik.getTurvalisusSkoor();
        
        // Näitame tagajärge ja selgitust
        BorderPane juurPaneel = new BorderPane();
        juurPaneel.setPadding(new Insets(20));
        
        // Pealkirja paneel
        Label pealkiri = new Label("Tagajärg");
        pealkiri.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        BorderPane.setAlignment(pealkiri, Pos.CENTER);
        juurPaneel.setTop(pealkiri);
        
        // Tagajärje paneel
        VBox tagajärjePaneel = new VBox(15);
        tagajärjePaneel.setPadding(new Insets(20, 0, 20, 0));
        
        Label tagajärjeTekst = new Label(valik.getTagajärg());
        tagajärjeTekst.setWrapText(true);
        tagajärjeTekst.setStyle("-fx-font-size: 16px;");
        
        Label selgitusTekst = new Label(valik.getSelgitus());
        selgitusTekst.setWrapText(true);
        selgitusTekst.setStyle("-fx-font-size: 14px;");
        
        Label skoorTekst = new Label("Punktid: " + valik.getTurvalisusSkoor());
        skoorTekst.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        tagajärjePaneel.getChildren().addAll(tagajärjeTekst, selgitusTekst, skoorTekst);
        juurPaneel.setCenter(tagajärjePaneel);
        
        // Nuppude paneel
        HBox nupudPaneel = new HBox(15);
        nupudPaneel.setAlignment(Pos.CENTER);
        
        Button jätkaBtn = new Button("Jätka");
        jätkaBtn.setPrefWidth(150);
        jätkaBtn.setOnAction(_ -> näitaJärgmineKüsimus());
        
        nupudPaneel.getChildren().add(jätkaBtn);
        juurPaneel.setBottom(nupudPaneel);
        
        // Loome stseeni
        Scene stseen = new Scene(juurPaneel);
        peaLava.setScene(stseen);
    }
    
    /**
     * Näitab stsenaariumi tulemuste ekraani.
     */
    private void näitaTulemusEkraan() {
        // Logime stsenaariumi lõpetamise
        logiHaldur.logiStsenaariumiLõpp(stsenaarium.getPealkiri(), skoor, true);
        
        // Uuendame kasutajaprofiili
        profiil.lisaLäbitudStsenaarium(stsenaarium.getPealkiri(), skoor);
        
        BorderPane juurPaneel = new BorderPane();
        juurPaneel.setPadding(new Insets(20));
        
        // Pealkirja paneel
        Label pealkiri = new Label("Stsenaarium lõpetatud!");
        pealkiri.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(pealkiri, Pos.CENTER);
        juurPaneel.setTop(pealkiri);
        
        // Tulemuste paneel
        VBox tulemusedPaneel = new VBox(15);
        tulemusedPaneel.setPadding(new Insets(20, 0, 20, 0));
        tulemusedPaneel.setAlignment(Pos.CENTER);
        
        Label stsenaariumiNimi = new Label(stsenaarium.getPealkiri());
        stsenaariumiNimi.setStyle("-fx-font-size: 18px;");
        
        Label skoorTekst = new Label("Sinu skoor: " + skoor + " punkti");
        skoorTekst.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        Label kokkuvõte = new Label(stsenaarium.getKokkuvõte());
        kokkuvõte.setWrapText(true);
        kokkuvõte.setStyle("-fx-font-size: 16px;");
        
        tulemusedPaneel.getChildren().addAll(stsenaariumiNimi, skoorTekst, kokkuvõte);
        juurPaneel.setCenter(tulemusedPaneel);
        
        // Nuppude paneel
        VBox nupudPaneel = new VBox(15);
        nupudPaneel.setAlignment(Pos.CENTER);
        
        Button uuestiBtn = new Button("Mängi uuesti");
        uuestiBtn.setPrefWidth(200);
        uuestiBtn.setOnAction(_ -> {
            küsimuseIndeks = 0;
            skoor = 0;
            näita();
        });
        
        Button uusStsBtn = new Button("Vali uus stsenaarium");
        uusStsBtn.setPrefWidth(200);
        uusStsBtn.setOnAction(_ -> tagasiPeamenüüsse.run());
        
        Button peamenüüBtn = new Button("Tagasi peamenüüsse");
        peamenüüBtn.setPrefWidth(200);
        peamenüüBtn.setOnAction(_ -> tagasiPeamenüüsse.run());
        
        nupudPaneel.getChildren().addAll(uuestiBtn, uusStsBtn, peamenüüBtn);
        juurPaneel.setBottom(nupudPaneel);
        
        // Loome stseeni
        Scene stseen = new Scene(juurPaneel);
        peaLava.setScene(stseen);
    }
    
    /**
     * Katkestab stsenaariumi ja naaseb peamenüüsse.
     */
    private void katkestaStsenaarium() {
        // Logime stsenaariumi katkestamise
        logiHaldur.logiStsenaariumiLõpp(stsenaarium.getPealkiri(), skoor, false);
        
        // Uuendame kasutajaprofiili
        profiil.lisaEbaõnnestunudStsenaarium(stsenaarium.getPealkiri());
        
        // Naaseme peamenüüsse
        tagasiPeamenüüsse.run();
    }
}

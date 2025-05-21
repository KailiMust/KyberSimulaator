import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

/**
 * KüberSimulaatorFX on programmi JavaFX peaklass, mis käivitab küberturvalisuse
 * stsenaariumi simulaatori graafilise kasutajaliidese.
 * See klass haldab stseenide vahetamist ja programmi elutsüklit.
 *
 * @author Kevin Laig, Kaili Must
 */
public class KüberSimulaatorFX extends Application {
    
    // Rakenduse peamine aken
    private Stage peaLava;
    
    // Stsenaariumite haldur
    private StsenaariumiHaldur haldur;
    
    // Praegune mängija
    private String praeguneKasutaja = "Külaline";
    
    // Logi haldur
    private LogiHaldur logiHaldur;
    
    // Kasutajaprofiil
    private KasutajaProfiil profiil;
    
    /**
     * Programmi käivituspunkt JavaFX rakenduse jaoks.
     *
     * @param args käsurea argumendid
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * JavaFX rakenduse käivitamise meetod.
     * Seadistab peamise lava ja näitab mängija valiku ekraani.
     *
     * @param primaryStage rakenduse peamine lava
     */
    @Override
    public void start(Stage primaryStage) {
        this.peaLava = primaryStage;
        this.haldur = new StsenaariumiHaldur();
        
        // Loome andmete kausta, kui seda pole
        looAndmeteKaust();
        
        // Seadistame lava
        peaLava.setTitle("Küberturvalisuse Simulaator");
        peaLava.setMinWidth(800);
        peaLava.setMinHeight(600);
        peaLava.setWidth(800);
        peaLava.setHeight(600);
        peaLava.setResizable(false);
        
        // Näitame mängija valiku ekraani
        näitaMängijaValikEkraan();
        
        peaLava.show();
    }
    
    /**
     * Loob vajalikud kaustad andmete salvestamiseks.
     */
    private void looAndmeteKaust() {
        File andmeteKaust = new File("data/players");
        if (!andmeteKaust.exists()) {
            boolean loodud = andmeteKaust.mkdirs();
            if (!loodud) {
                System.err.println("Hoiatus: Andmete kausta loomine ebaõnnestus!");
            }
        }
    }
    
    /**
     * Näitab mängija valiku ekraani.
     * Kasutaja saab valida olemasoleva mängija või luua uue.
     */
    private void näitaMängijaValikEkraan() {
        BorderPane juurPaneel = new BorderPane();
        juurPaneel.setPadding(new Insets(20));
        
        // Pealkirja paneel
        Label pealkiri = new Label("Vali mängija");
        pealkiri.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(pealkiri, Pos.CENTER);
        juurPaneel.setTop(pealkiri);
        
        // Keskmine paneel mängijate loendiga
        VBox keskmineOsa = new VBox(15);
        keskmineOsa.setAlignment(Pos.CENTER);
        
        // Olemasolevate mängijate loend
        List<String> olemasolevadMängijad = KasutajaProfiil.saaOlemasolevadMängijad();
        
        if (!olemasolevadMängijad.isEmpty()) {
            Label olemasolevadLabel = new Label("Olemasolevad mängijad:");
            olemasolevadLabel.setStyle("-fx-font-size: 16px;");
            
            ListView<String> mängijateLoend = new ListView<>();
            mängijateLoend.getItems().addAll(olemasolevadMängijad);
            mängijateLoend.setPrefHeight(150);
            
            Button valiMängijaBtn = new Button("Vali mängija");
            valiMängijaBtn.setPrefWidth(200);
            valiMängijaBtn.setOnAction(_ -> {
                String valitudMängija = mängijateLoend.getSelectionModel().getSelectedItem();
                if (valitudMängija != null) {
                    valiMängija(valitudMängija);
                }
            });
            
            keskmineOsa.getChildren().addAll(olemasolevadLabel, mängijateLoend, valiMängijaBtn);
        } else {
            Label eiOleMängijaidLabel = new Label("Hetkel pole ühtegi mängijat. Loo uus mängija, et alustada.");
            eiOleMängijaidLabel.setStyle("-fx-font-size: 16px;");
            keskmineOsa.getChildren().add(eiOleMängijaidLabel);
        }
        
        // Uue mängija loomise nupp
        Button uusMängijaBtn = new Button("Loo uus mängija");
        uusMängijaBtn.setPrefWidth(200);
        uusMängijaBtn.setOnAction(_ -> looUusMängija());
        
        keskmineOsa.getChildren().add(uusMängijaBtn);
        juurPaneel.setCenter(keskmineOsa);
        
        // Loome stseeni
        Scene stseen = new Scene(juurPaneel);
        peaLava.setScene(stseen);
    }
    
    /**
     * Valib mängija ja laeb tema profiili.
     * 
     * @param mängijaNimi valitud mängija nimi
     */
    private void valiMängija(String mängijaNimi) {
        this.praeguneKasutaja = mängijaNimi;
        
        // Kui tegu pole külalisega, laeme profiili
        if (!mängijaNimi.equals("Külaline")) {
            this.profiil = KasutajaProfiil.laeProfiil(mängijaNimi);
            if (this.profiil == null) {
                this.profiil = new KasutajaProfiil(mängijaNimi);
                this.profiil.salvestaProfiil();
            }
            
            // Loome logi halduri
            this.logiHaldur = new LogiHaldur(mängijaNimi);
            logiHaldur.logiSündmus("Sisenes mängu");
        }
        
        näitaPeamenüü(mängijaNimi);
    }
    
    /**
     * Avab dialoogi uue mängija loomiseks.
     */
    private void looUusMängija() {
        // Kasutame mängija loomise dialoogi
        MängijaLoomisDialoog dialoog = new MängijaLoomisDialoog();
        String uusMängijaNimi = dialoog.näitaDialoog(peaLava);
        
        if (uusMängijaNimi != null && !uusMängijaNimi.isEmpty()) {
            // Loome uue profiili
            this.profiil = new KasutajaProfiil(uusMängijaNimi);
            this.profiil.salvestaProfiil();
            
            // Loome logi halduri
            this.logiHaldur = new LogiHaldur(uusMängijaNimi);
            logiHaldur.logiSündmus("Loodud uus mängija");
            
            // Määrame praeguse kasutaja
            this.praeguneKasutaja = uusMängijaNimi;
            
            // Näitame peamenüüd
            näitaPeamenüü(uusMängijaNimi);
        }
    }
    
    /**
     * Näitab peamenüü ekraani.
     * 
     * @param mängijaNimi valitud mängija nimi
     */
    private void näitaPeamenüü(String mängijaNimi) {
        BorderPane juurPaneel = new BorderPane();
        juurPaneel.setPadding(new Insets(20));
        
        // Pealkirja paneel
        VBox päisPaneel = new VBox(10);
        päisPaneel.setAlignment(Pos.CENTER);
        
        Label pealkiri = new Label("Küberturvalisuse Simulaator");
        pealkiri.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        Label mängijaInfo = new Label("Mängija: " + mängijaNimi);
        mängijaInfo.setStyle("-fx-font-size: 16px;");
        
        päisPaneel.getChildren().addAll(pealkiri, mängijaInfo);
        juurPaneel.setTop(päisPaneel);
        
        // Nuppude paneel
        VBox nupudPaneel = new VBox(15);
        nupudPaneel.setAlignment(Pos.CENTER);
        
        Button juhuslikBtn = new Button("Mängi juhuslikku stsenaariumit");
        juhuslikBtn.setPrefWidth(250);
        juhuslikBtn.setOnAction(_ -> käivitaJuhuslikStsenaarium());
        
        Button kategooriaBtn = new Button("Vali kategooria");
        kategooriaBtn.setPrefWidth(250);
        kategooriaBtn.setOnAction(_ -> näitaKategooriadEkraan());
        
        Button statistikaBtn = new Button("Vaata statistikat");
        statistikaBtn.setPrefWidth(250);
        statistikaBtn.setOnAction(_ -> {
            StatistikaEkraan statistikaEkraan = new StatistikaEkraan(
                peaLava, 
                praeguneKasutaja, 
                () -> näitaPeamenüü(praeguneKasutaja)
            );
            statistikaEkraan.näita();
        });
        
        Button juhisedBtn = new Button("Juhised");
        juhisedBtn.setPrefWidth(250);
        juhisedBtn.setOnAction(_ -> näitaJuhisedEkraan());
        
        Button tagasiBtn = new Button("Tagasi mängija valikusse");
        tagasiBtn.setPrefWidth(250);
        tagasiBtn.setOnAction(_ -> {
            // Kui oleme sisse loginud, logime sessiooni lõpu
            if (logiHaldur != null && !praeguneKasutaja.equals("Külaline")) {
                logiHaldur.logiSündmus("Väljus peamenüüsse");
            }
            näitaMängijaValikEkraan();
        });
        
        Button väljuBtn = new Button("Välju");
        väljuBtn.setPrefWidth(250);
        väljuBtn.setOnAction(_ -> {
            // Kui oleme sisse loginud, logime sessiooni lõpu
            if (logiHaldur != null && !praeguneKasutaja.equals("Külaline")) {
                logiHaldur.logiSessioonLõpp(
                    profiil.getLäbitudStsenaariumiteArv(), 
                    profiil.getKoguSkoor()
                );
            }
            peaLava.close();
        });
        
        nupudPaneel.getChildren().addAll(
            juhuslikBtn, kategooriaBtn, statistikaBtn, 
            juhisedBtn, tagasiBtn, väljuBtn
        );
        juurPaneel.setCenter(nupudPaneel);
        
        // Loome stseeni
        Scene stseen = new Scene(juurPaneel);
        peaLava.setScene(stseen);
    }
    
    /**
     * Käivitab juhusliku stsenaariumi.
     */
    private void käivitaJuhuslikStsenaarium() {
        Stsenaarium stsenaarium = haldur.juhuslikStsenaarium();
        if (stsenaarium != null) {
            näitaStsenaariumiEkraan(stsenaarium);
        } else {
            // Näitame viga, kui stsenaariumit ei leitud
            näitaViga("Stsenaariumite laadimine ebaõnnestus!");
        }
    }
    
    /**
     * Näitab kategooriate valiku ekraani.
     */
    private void näitaKategooriadEkraan() {
        BorderPane juurPaneel = new BorderPane();
        juurPaneel.setPadding(new Insets(20));
        
        // Pealkirja paneel
        Label pealkiri = new Label("Vali kategooria");
        pealkiri.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(pealkiri, Pos.CENTER);
        juurPaneel.setTop(pealkiri);
        
        // Kategooriate paneel
        VBox kategooriadPaneel = new VBox(15);
        kategooriadPaneel.setAlignment(Pos.CENTER);
        
        String[] kategooriad = haldur.saaKategooriad();
        
        for (String kategooria : kategooriad) {
            Button kategooriaBtn = new Button(kategooria);
            kategooriaBtn.setPrefWidth(200);
            final String valitudKategooria = kategooria;
            kategooriaBtn.setOnAction(_ -> käivitaKategooriaStsenaarium(valitudKategooria));
            kategooriadPaneel.getChildren().add(kategooriaBtn);
        }
        
        Button tagasiBtn = new Button("Tagasi");
        tagasiBtn.setPrefWidth(200);
        tagasiBtn.setOnAction(_ -> näitaPeamenüü(praeguneKasutaja));
        kategooriadPaneel.getChildren().add(tagasiBtn);
        
        juurPaneel.setCenter(kategooriadPaneel);
        
        // Loome stseeni
        Scene stseen = new Scene(juurPaneel);
        peaLava.setScene(stseen);
    }
    
    /**
     * Käivitab valitud kategooria stsenaariumi.
     * 
     * @param kategooria valitud kategooria
     */
    private void käivitaKategooriaStsenaarium(String kategooria) {
        Stsenaarium stsenaarium = haldur.saaKategooriast(kategooria);
        if (stsenaarium != null) {
            näitaStsenaariumiEkraan(stsenaarium);
        } else {
            // Näitame viga, kui stsenaariumit ei leitud
            näitaViga("Valitud kategoorias pole stsenaariumeid!");
        }
    }
    
    /**
     * Näitab stsenaariumi ekraani.
     * 
     * @param stsenaarium esitatav stsenaarium
     */
    private void näitaStsenaariumiEkraan(Stsenaarium stsenaarium) {
        // Kasutame StsenaariumiEkraan klassi stsenaariumi kuvamiseks
        StsenaariumiEkraan stsenaariumiEkraan = new StsenaariumiEkraan(
            peaLava, 
            stsenaarium, 
            praeguneKasutaja, 
            () -> näitaPeamenüü(praeguneKasutaja)
        );
        stsenaariumiEkraan.näita();
    }
    
    /**
     * Näitab veateadet kasutajale.
     * 
     * @param veateade näidatav veateade
     */
    private void näitaViga(String veateade) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.ERROR
        );
        alert.setTitle("Viga");
        alert.setHeaderText(null);
        alert.setContentText(veateade);
        alert.showAndWait();
    }
    
    /**
     * Näitab juhiste ekraani.
     */
    private void näitaJuhisedEkraan() {
        BorderPane juurPaneel = new BorderPane();
        juurPaneel.setPadding(new Insets(20));
        
        // Pealkirja paneel
        Label pealkiri = new Label("Juhised");
        pealkiri.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setAlignment(pealkiri, Pos.CENTER);
        juurPaneel.setTop(pealkiri);
        
        // Juhiste paneel
        VBox juhisedPaneel = new VBox(15);
        juhisedPaneel.setAlignment(Pos.CENTER);
        juhisedPaneel.setPadding(new Insets(20));
        
        Label juhised = new Label(
            "Küberturvalisuse simulaator võimaldab sul harjutada reageerimist\n" +
            "erinevatele küberturvalisuse olukordadele turvalisel viisil.\n\n" +
            "STSENAARIUMITES NAVIGEERIMINE:\n" +
            "- Loe hoolikalt stsenaariumi kirjeldust\n" +
            "- Vasta küsimustele, valides sobiva vastusevariandi\n" +
            "- Kasuta lisainfo nuppu täiendava teabe saamiseks\n\n" +
            "MÄNGIJA PROFIIL:\n" +
            "- Loo endale mängija profiil, et jälgida oma edusamme\n" +
            "- Sinu tulemused salvestatakse automaatselt\n" +
            "- Statistika ekraanil näed oma koguskoori ja läbitud stsenaariumeid\n\n" +
            "IGA STSENAARIUM ÕPETAB SULLE MIDAGI KÜBERTURVALISUSE KOHTA!"
        );
        juhised.setWrapText(true);
        
        Button tagasiBtn = new Button("Tagasi");
        tagasiBtn.setPrefWidth(200);
        tagasiBtn.setOnAction(_ -> näitaPeamenüü(praeguneKasutaja));
        
        juhisedPaneel.getChildren().addAll(juhised, tagasiBtn);
        juurPaneel.setCenter(juhisedPaneel);
        
        // Loome stseeni
        Scene stseen = new Scene(juurPaneel);
        peaLava.setScene(stseen);
        
        // Kui oleme sisse loginud, logime sündmuse
        if (logiHaldur != null && !praeguneKasutaja.equals("Külaline")) {
            logiHaldur.logiSündmus("Vaatas juhiseid");
        }
    }
}

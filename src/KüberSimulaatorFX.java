import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import model.KasutajaProfiil;
import model.Stsenaarium;
import view.MängijaLoomisDialoog;
import view.StatistikaEkraan;
import view.StsenaariumiEkraan;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.LogiHaldur;
import controller.StsenaariumiHaldur;
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
        peaLava.setResizable(true);
        
         // Määrame rakenduse ikooni
        try {
            Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
            peaLava.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Hoiatus: Rakenduse ikooni laadimine ebaõnnestus: " + e.getMessage());
        }
        
        // Näitame mängija valiku ekraani
        Scene alguseStseen = looMängijaValikEkraan();
        peaLava.setScene(lisaCSS(alguseStseen));
        
        // Lisame akna suuruse muutumise käsitleja
        peaLava.widthProperty().addListener((obs, vanaLaius, uusLaius) -> {
            uuendaKasutajaliideseLaiuse(uusLaius.doubleValue());
        });
        
        peaLava.heightProperty().addListener((obs, vanaKõrgus, uusKõrgus) -> {
            uuendaKasutajaliidesKõrgus(uusKõrgus.doubleValue());
        });
        
        peaLava.show();
    }
    
    private void uuendaKasutajaliideseLaiuse(double laius) {
        // Kohandame UI elemente vastavalt laiusele
        // Vajadusel muudame nuppude laiused, tekstide suurused jne
        System.out.println("Akna uus laius: " + laius);
    }
    
    private void uuendaKasutajaliidesKõrgus(double kõrgus) {
        // Kohandame UI elemente vastavalt kõrgusele
        System.out.println("Akna uus kõrgus: " + kõrgus);
    }
    
    /**
     * Lisab stseenile CSS stiilid.
     *
     * @param stseen stseen, millele CSS lisatakse
     * @return sama stseen koos CSS-iga
     */
    private Scene lisaCSS(Scene stseen) {
        String cssFile = getClass().getResource("/styles.css").toExternalForm();
        stseen.getStylesheets().add(cssFile);
        return stseen;
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
     * Loob mängija valiku ekraani.
     *
     * @return loodud stseen
     */
    private Scene looMängijaValikEkraan() {
        BorderPane juurPaneel = new BorderPane();
        juurPaneel.setPadding(new Insets(20));
        
        // Pealkirja paneel
        Label pealkiri = new Label("Vali mängija");
        pealkiri.getStyleClass().add("title-label");
        BorderPane.setAlignment(pealkiri, Pos.CENTER);
        juurPaneel.setTop(pealkiri);
        
        // Keskmine paneel mängijate loendiga
        VBox keskmineOsa = new VBox(15);
        keskmineOsa.setAlignment(Pos.CENTER);
        
        // Olemasolevate mängijate loend
        List<String> olemasolevadMängijad = KasutajaProfiil.saaOlemasolevadMängijad();
        
        if (!olemasolevadMängijad.isEmpty()) {
            Label olemasolevadLabel = new Label("Olemasolevad mängijad:");
            olemasolevadLabel.getStyleClass().add("subtitle-label");
            
            ListView<String> mängijateLoend = new ListView<>();
            mängijateLoend.getItems().addAll(olemasolevadMängijad);
            mängijateLoend.setPrefHeight(200);
            
            // Kasutame bindings, et kohandada suurust
            mängijateLoend.prefWidthProperty().bind(
                Bindings.max(300, keskmineOsa.widthProperty().multiply(0.8))
            );
            
            Button valiMängijaBtn = new Button("Vali mängija");
            valiMängijaBtn.prefWidthProperty().bind(
                Bindings.max(200, keskmineOsa.widthProperty().multiply(0.5))
            );
            
            valiMängijaBtn.setOnAction(_ -> {
                String valitudMängija = mängijateLoend.getSelectionModel().getSelectedItem();
                if (valitudMängija != null) {
                    valiMängija(valitudMängija);
                } else {
                    näitaViga("Palun vali mängija loendist");
                }
            });
            
            keskmineOsa.getChildren().addAll(olemasolevadLabel, mängijateLoend, valiMängijaBtn);
        } else {
            Label eiOleMängijaidLabel = new Label("Hetkel pole ühtegi mängijat. Loo uus mängija, et alustada.");
            eiOleMängijaidLabel.setWrapText(true);
            eiOleMängijaidLabel.getStyleClass().add("subtitle-label");
            keskmineOsa.getChildren().add(eiOleMängijaidLabel);
        }
        
        // Uue mängija loomise nupp
        Button uusMängijaBtn = new Button("Loo uus mängija");
        uusMängijaBtn.prefWidthProperty().bind(
            Bindings.max(200, keskmineOsa.widthProperty().multiply(0.5))
        );
        uusMängijaBtn.setOnAction(_ -> looUusMängija());
        
        keskmineOsa.getChildren().add(uusMängijaBtn);
        
        // Kasutame ScrollPane, et tagada nähtavus ka väiksemate akende puhul
        ScrollPane kerimisAla = new ScrollPane(keskmineOsa);
        kerimisAla.setFitToWidth(true);
        kerimisAla.setFitToHeight(true);
        juurPaneel.setCenter(kerimisAla);
        
        // Klaviatuuri sündmused
        juurPaneel.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                Platform.exit();
            }
        });
        
        // Loome stseeni
        return new Scene(juurPaneel);
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
     * Loob peamenüü ekraani.
     * 
     * @param mängijaNimi mängija nimi
     * @return loodud stseen
     */
    private Scene looPeamenüüEkraan(String mängijaNimi) {
        BorderPane juurPaneel = new BorderPane();
        juurPaneel.setPadding(new Insets(20));
        
        // Pealkirja paneel
        VBox päisPaneel = new VBox(10);
        päisPaneel.setAlignment(Pos.CENTER);
        päisPaneel.setPadding(new Insets(0, 0, 20, 0));
        
        Label pealkiri = new Label("Küberturvalisuse Simulaator");
        pealkiri.getStyleClass().add("title-label");
        
        Label mängijaInfo = new Label("Mängija: " + mängijaNimi);
        mängijaInfo.getStyleClass().add("subtitle-label");
        
        päisPaneel.getChildren().addAll(pealkiri, mängijaInfo);
        juurPaneel.setTop(päisPaneel);
        
        // Nuppude paneel klaasipaneelina
        VBox nupudPaneel = new VBox(15);
        nupudPaneel.getStyleClass().add("glass-pane");
        nupudPaneel.setAlignment(Pos.CENTER);
        nupudPaneel.setMaxWidth(400);
        
        // Simulaatori kiirkirjeldus
        Label kirjeldusLabel = new Label(
            "Tere tulemast Küberturvalisuse Simulaatorisse! Siin saad harjutada küberturvalisuse stsenaariumite läbimist, " +
            "teha otsuseid ja õppida, kuidas kaitsta ennast ja ettevõtet erinevate küberohtude eest."
        );
        kirjeldusLabel.setWrapText(true);
        kirjeldusLabel.setPadding(new Insets(0, 0, 10, 0));
        
        // Loome nupud
        Button juhuslikBtn = new Button("Mängi juhuslikku stsenaariumit");
        seadistaMenuüNupp(juhuslikBtn, nupudPaneel);
        juhuslikBtn.setOnAction(_ -> käivitaJuhuslikStsenaarium());
        
        Button kategooriaBtn = new Button("Vali kategooria");
        seadistaMenuüNupp(kategooriaBtn, nupudPaneel);
        kategooriaBtn.setOnAction(_ -> {
            Scene kategooriadStseen = looKategooriadEkraan();
            String cssFile = getClass().getResource("/styles.css").toExternalForm();
            kategooriadStseen.getStylesheets().add(cssFile);
            peaLava.setScene(kategooriadStseen);
        });
        
        Button statistikaBtn = new Button("Vaata statistikat");
        seadistaMenuüNupp(statistikaBtn, nupudPaneel);
        statistikaBtn.setOnAction(_ -> {
            StatistikaEkraan statistikaEkraan = new StatistikaEkraan(
                peaLava, 
                praeguneKasutaja, 
                () -> {
                    Scene peamenüüStseen = looPeamenüüEkraan(praeguneKasutaja);
                    String cssFile = getClass().getResource("/styles.css").toExternalForm();
                    peamenüüStseen.getStylesheets().add(cssFile);
                    peaLava.setScene(peamenüüStseen);
                }
            );
            statistikaEkraan.näita();
        });
        
        Button juhisedBtn = new Button("Juhised");
        seadistaMenuüNupp(juhisedBtn, nupudPaneel);
        juhisedBtn.setOnAction(_ -> {
            Scene juhisedStseen = looJuhisedEkraan();
            String cssFile = getClass().getResource("/styles.css").toExternalForm();
            juhisedStseen.getStylesheets().add(cssFile);
            peaLava.setScene(juhisedStseen);
        });
        
        Button tagasiBtn = new Button("Tagasi mängija valikusse");
        seadistaMenuüNupp(tagasiBtn, nupudPaneel);
        tagasiBtn.setOnAction(_ -> {
            // Kui oleme sisse loginud, logime sessiooni lõpu
            if (logiHaldur != null && !praeguneKasutaja.equals("Külaline")) {
                logiHaldur.logiSündmus("Väljus peamenüüsse");
            }
            Scene valijaEkraan = looMängijaValikEkraan();
            String cssFile = getClass().getResource("/styles.css").toExternalForm();
            valijaEkraan.getStylesheets().add(cssFile);
            peaLava.setScene(valijaEkraan);
        });
        
        Button väljuBtn = new Button("Välju");
        seadistaMenuüNupp(väljuBtn, nupudPaneel);
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
            kirjeldusLabel,
            juhuslikBtn, kategooriaBtn, statistikaBtn, 
            juhisedBtn, tagasiBtn, väljuBtn
        );
        
        // Lisame StackPane, et joondada nupud keskele ja säilitada paindlikkus
        StackPane keskmineKontainer = new StackPane();
        keskmineKontainer.getChildren().add(nupudPaneel);
        juurPaneel.setCenter(keskmineKontainer);
        
        // Loome stseeni
        Scene stseen = new Scene(juurPaneel);
        
        // Lisa klaviatuuri tugi
        stseen.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                // Kui oleme sisse loginud, logime sessiooni lõpu
                if (logiHaldur != null && !praeguneKasutaja.equals("Külaline")) {
                    logiHaldur.logiSündmus("Väljus peamenüüsse");
                }
                Scene valijaEkraan = looMängijaValikEkraan();
                String cssFile = getClass().getResource("/styles.css").toExternalForm();
                valijaEkraan.getStylesheets().add(cssFile);
                peaLava.setScene(valijaEkraan);
            }
        });
        
        return stseen;
    }
    
    /**
     * Loob kategooriate valiku ekraani.
     *
     * @return loodud stseen
     */
    private Scene looKategooriadEkraan() {
        BorderPane juurPaneel = new BorderPane();
        juurPaneel.setPadding(new Insets(20));
        
        // Pealkirja paneel
        Label pealkiri = new Label("Vali kategooria");
        pealkiri.getStyleClass().add("title-label");
        BorderPane.setAlignment(pealkiri, Pos.CENTER);
        juurPaneel.setTop(pealkiri);
        
        // Kategooriate paneel klaasipaneelina
        VBox kategooriadPaneel = new VBox(15);
        kategooriadPaneel.getStyleClass().add("glass-pane");
        kategooriadPaneel.setPadding(new Insets(20));
        kategooriadPaneel.setAlignment(Pos.CENTER);
        kategooriadPaneel.setMaxWidth(500);
        
        String[] kategooriad = haldur.saaKategooriad();
        
        // Lisa kategooriate kirjeldused ja nupud
        Map<String, String> kategooriaKirjeldused = new HashMap<>();
        kategooriaKirjeldused.put("Phishing", "Õpi tuvastama ja reageerima phishing-rünnakutele. Need stsenaariumid õpetavad kuidas kaitsta end ja oma ettevõtet petuskeemide eest.");
        kategooriaKirjeldused.put("Paroolid", "Harjuta turvalisuse paroolide valimist ja kaitsmist. Need stsenaariumid õpetavad parooli turvalisuse põhimõtteid.");
        // Lisa siia vajadusel teisi kategooriaid
        
        for (String kategooria : kategooriad) {
            // Loo paneel kategooriale
            VBox kategooriaPaneel = new VBox(5);
            kategooriaPaneel.setPadding(new Insets(10));
            kategooriaPaneel.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 5px;");
            
            // Leia kirjeldus
            String kirjeldus = kategooriaKirjeldused.getOrDefault(kategooria, 
                "Selle kategooria stsenaariumid aitavad sul õppida küberturvalisuse olulisi aspekte.");
            
            Label kategoriaLabel = new Label(kategooria);
            kategoriaLabel.getStyleClass().add("subtitle-label");
            
            Label kirjeldusLabel = new Label(kirjeldus);
            kirjeldusLabel.setWrapText(true);
            
            Button valiBtn = new Button("Vali see kategooria");
            valiBtn.setPrefWidth(180);
            
            final String valitudKategooria = kategooria;
            valiBtn.setOnAction(_ -> käivitaKategooriaStsenaarium(valitudKategooria));
            
            kategooriaPaneel.getChildren().addAll(kategoriaLabel, kirjeldusLabel, valiBtn);
            kategooriadPaneel.getChildren().add(kategooriaPaneel);
        }
        
        Button tagasiBtn = new Button("Tagasi");
        tagasiBtn.setPrefWidth(200);
        tagasiBtn.setOnAction(_ -> {
            Scene peamenüüStseen = looPeamenüüEkraan(praeguneKasutaja);
            String cssFile = getClass().getResource("/styles.css").toExternalForm();
            peamenüüStseen.getStylesheets().add(cssFile);
            peaLava.setScene(peamenüüStseen);
        });
        
        kategooriadPaneel.getChildren().add(tagasiBtn);
        
        // Lisame StackPane, et joondada paneel keskele
        StackPane keskmineKontainer = new StackPane();
        keskmineKontainer.getChildren().add(kategooriadPaneel);
        
        // Lisame ScrollPane, et tagada nähtavus
        ScrollPane kerimisAla = new ScrollPane(keskmineKontainer);
        kerimisAla.setFitToWidth(true);
        juurPaneel.setCenter(kerimisAla);
        
        // Loome stseeni
        Scene stseen = new Scene(juurPaneel);
        
        // Lisa klaviatuuri tugi
        stseen.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                Scene peamenüüStseen = looPeamenüüEkraan(praeguneKasutaja);
                String cssFile = getClass().getResource("/styles.css").toExternalForm();
                peamenüüStseen.getStylesheets().add(cssFile);
                peaLava.setScene(peamenüüStseen);
            }
        });
        
        return stseen;
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
        Scene kategooriadStseen = looKategooriadEkraan();
        peaLava.setScene(lisaCSS(kategooriadStseen));
    }
    
    /**
     * Näitab peamenüü ekraani.
     * 
     * @param mängijaNimi mängija nimi, kelle peamenüü näidatakse
     */
    private void näitaPeamenüü(String mängijaNimi) {
        Scene peamenüüStseen = looPeamenüüEkraan(mängijaNimi);
        peaLava.setScene(lisaCSS(peamenüüStseen));
    }

    /**
     * Seadistab menüünupu omadused.
     *
     * @param nupp seadistatav nupp
     * @param konteiner nupu konteiner
     */
    private void seadistaMenuüNupp(Button nupp, VBox konteiner) {
        nupp.setMaxWidth(Double.MAX_VALUE);
        nupp.setPrefWidth(300);
        
        // Lisa bindings, et nupp kohanduks konteineriga
        nupp.prefWidthProperty().bind(
            Bindings.max(300, konteiner.widthProperty().multiply(0.8))
        );
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
            () -> {
                Scene peamenüüStseen = looPeamenüüEkraan(praeguneKasutaja);
                String cssFile = getClass().getResource("/styles.css").toExternalForm();
                peamenüüStseen.getStylesheets().add(cssFile);
                peaLava.setScene(peamenüüStseen);
            }
        );
        stsenaariumiEkraan.näita();
    }
    
    /**
     * Näitab veateadet kasutajale.
     * 
     * @param veateade näidatav veateade
     */
    private void näitaViga(String veateade) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Viga");
        alert.setContentText(veateade);
        alert.showAndWait();
    }
    
    /**
     * Näitab juhiste ekraani.
     */
    private void näitaJuhisedEkraan() {
        Scene juhisedStseen = looJuhisedEkraan();
        peaLava.setScene(lisaCSS(juhisedStseen));
        
        // Kui oleme sisse loginud, logime sündmuse
        if (logiHaldur != null && !praeguneKasutaja.equals("Külaline")) {
            logiHaldur.logiSündmus("Vaatas juhiseid");
        }
    }
    
    /**
     * Loob juhiste ekraani.
     *
     * @return loodud stseen
     */
    private Scene looJuhisedEkraan() {
        BorderPane juurPaneel = new BorderPane();
        juurPaneel.setPadding(new Insets(20));
        
        // Pealkirja paneel
        Label pealkiri = new Label("Juhised");
        pealkiri.getStyleClass().add("title-label");
        BorderPane.setAlignment(pealkiri, Pos.CENTER);
        juurPaneel.setTop(pealkiri);
    
    // Juhiste paneel klaasiga
    VBox juhisedPaneel = new VBox(15);
    juhisedPaneel.getStyleClass().add("glass-pane");
    juhisedPaneel.setPadding(new Insets(20));
    
    ScrollPane kerimisAla = new ScrollPane(juhisedPaneel);
    kerimisAla.setFitToWidth(true);
    
    // Stsenaariumite sektsiooni pealkiri
    Label stsenaariumiPealkiri = new Label("STSENAARIUMITES NAVIGEERIMINE");
    stsenaariumiPealkiri.getStyleClass().add("subtitle-label");
    
    // Stsenaariumite navigeerimise juhised
    TextFlow stsenaariumidJuhised = new TextFlow();
    stsenaariumidJuhised.setLineSpacing(5);
    
    Text stsJuhis1 = new Text("• Loe hoolikalt stsenaariumi kirjeldust\n");
    Text stsJuhis2 = new Text("• Vasta küsimustele, valides sobiva vastusevariandi\n");
    Text stsJuhis3 = new Text("• Kasuta lisainfo nuppu täiendava teabe saamiseks\n");
    Text stsJuhis4 = new Text("• Iga stsenaariumi küsimus sisaldab mitmeid vastusevariante, millel on erinev mõju sinu skoorile\n");
    Text stsJuhis5 = new Text("• Pärast vastuse valimist näed tagajärgi ja õpetlikku selgitust\n");
    Text stsJuhis6 = new Text("• Soovi korral saad igal hetkel stsenaariumist väljuda, kuid sellisel juhul loetakse see ebaõnnestunud läbimiseks\n\n");
    
    stsenaariumidJuhised.getChildren().addAll(stsJuhis1, stsJuhis2, stsJuhis3, stsJuhis4, stsJuhis5, stsJuhis6);
    
    // Mängija profiili sektsiooni pealkiri
    Label profiiliPealkiri = new Label("MÄNGIJA PROFIIL");
    profiiliPealkiri.getStyleClass().add("subtitle-label");
    
    // Mängija profiili juhised
    TextFlow profiiliJuhised = new TextFlow();
    profiiliJuhised.setLineSpacing(5);
    
    Text profJuhis1 = new Text("• Loo endale mängija profiil, et jälgida oma edusamme\n");
    Text profJuhis2 = new Text("• Sinu tulemused salvestatakse automaatselt\n");
    Text profJuhis3 = new Text("• Statistika ekraanil näed oma koguskoori ja läbitud stsenaariumeid\n");
    Text profJuhis4 = new Text("• Kui soovid alustada uuesti, võid luua uue profiili\n\n");
    
    profiiliJuhised.getChildren().addAll(profJuhis1, profJuhis2, profJuhis3, profJuhis4);
    
    // Küberturvalisuse õppimise sektsiooni pealkiri
    Label õppimisePealkiri = new Label("KÜBERTURVALISUSE ÕPPIMINE");
    õppimisePealkiri.getStyleClass().add("subtitle-label");
    
    // Küberturvalisuse õppimise juhised
    TextFlow õppimiseJuhised = new TextFlow();
    õppimiseJuhised.setLineSpacing(5);
    
    Text õppJuhis1 = new Text("• Iga stsenaarium õpetab sulle olulisi küberturvalisuse põhimõtteid\n");
    Text õppJuhis2 = new Text("• Pärast stsenaariumi läbimist kuvatakse kokkuvõte ja õpetlik info\n");
    Text õppJuhis3 = new Text("• Mida rohkem stsenaariumeid läbid, seda paremini õpid tuvastama ja reageerima küberturvalisuse ohtudele\n");
    Text õppJuhis4 = new Text("• Püüa iga kategooria stsenaariumid läbida, et õppida erinevate ohtude kohta\n");
    
    õppimiseJuhised.getChildren().addAll(õppJuhis1, õppJuhis2, õppJuhis3, õppJuhis4);
    
    // Klaviatuur ja hiirekasutuse sektsiooni pealkiri
    Label klaviatuuriPealkiri = new Label("KLAVIATUURI JA HIIRE KASUTAMINE");
    klaviatuuriPealkiri.getStyleClass().add("subtitle-label");
    
    // Klaviatuuri ja hiire kasutamise juhised
    TextFlow klaviatuuriJuhised = new TextFlow();
    klaviatuuriJuhised.setLineSpacing(5);
    
    Text klavJuhis1 = new Text("• Hiire klõpsuga saad valida vastusevariante ja vajutada nuppe\n");
    Text klavJuhis2 = new Text("• Enter klahv: kinnita valik või jätka\n");
    Text klavJuhis3 = new Text("• Escape klahv: tagasi või tühista\n");
    Text klavJuhis4 = new Text("• Tab klahv: liigu erinevate elementide vahel\n");
    
    klaviatuuriJuhised.getChildren().addAll(klavJuhis1, klavJuhis2, klavJuhis3, klavJuhis4);
    
    // Lisame kõik elemendid juhiste paneelile
    juhisedPaneel.getChildren().addAll(
        stsenaariumiPealkiri, stsenaariumidJuhised,
        profiiliPealkiri, profiiliJuhised,
        õppimisePealkiri, õppimiseJuhised,
        klaviatuuriPealkiri, klaviatuuriJuhised
    );
    
    juurPaneel.setCenter(kerimisAla);
    
    // Nuppude paneel
    HBox nupudPaneel = new HBox(15);
    nupudPaneel.setAlignment(Pos.CENTER);
    nupudPaneel.setPadding(new Insets(15, 0, 0, 0));
    
    Button tagasiBtn = new Button("Tagasi");
    tagasiBtn.setPrefWidth(200);
    tagasiBtn.setOnAction(_ -> {
        Scene peamenüüStseen = looPeamenüüEkraan(praeguneKasutaja);
        String cssFile = getClass().getResource("/styles.css").toExternalForm();
        peamenüüStseen.getStylesheets().add(cssFile);
        peaLava.setScene(peamenüüStseen);
    });
    
    nupudPaneel.getChildren().add(tagasiBtn);
    juurPaneel.setBottom(nupudPaneel);
    
    // Loome stseeni
    Scene stseen = new Scene(juurPaneel);
    
    // Lisa klaviatuuri tugi
    stseen.setOnKeyPressed(event -> {
        if (event.getCode() == KeyCode.ESCAPE) {
            Scene peamenüüStseen = looPeamenüüEkraan(praeguneKasutaja);
            String cssFile = getClass().getResource("/styles.css").toExternalForm();
            peamenüüStseen.getStylesheets().add(cssFile);
            peaLava.setScene(peamenüüStseen);
        }
    });
    
    return stseen;
}
}

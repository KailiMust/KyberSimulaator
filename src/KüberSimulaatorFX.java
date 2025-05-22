import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import model.KasutajaProfiil;
import model.Stsenaarium;
import view.KasutajaKustutamiseDialoog;
import view.KasutajaLoomisDialoog;
import view.StatistikaEkraan;
import view.StsenaariumiEkraan;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import controller.LogiHaldur;
import controller.StsenaariumiHaldur;

/**
 * KüberSimulaatorFX - optimeeritud JavaFX peaklass küberturvalisuse simulaatori jaoks.
 * Vähendatud koodiridu ja parandatud struktuuri kaudu.
 *
 * @author Kevin Laig, Kaili Must
 */
public class KüberSimulaatorFX extends Application {
    
    private Stage peaLava;
    private StsenaariumiHaldur haldur;
    private String praeguneKasutaja = "Külaline";
    private LogiHaldur logiHaldur;
    private KasutajaProfiil profiil;
    
    // Taaskasutatavad konstanted
    private static final String CSS_PATH = "/styles.css";
    private static final int MIN_WINDOW_SIZE = 600;
    private static final int DEFAULT_WINDOW_SIZE = 800;
    private static final String ANDMETE_KAUST = "data/players";
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.peaLava = primaryStage;
        this.haldur = new StsenaariumiHaldur();
        
        seadistaPeamineLava();
        looAndmeteKaust();
        näitaKasutajaValikEkraan();
    }
    
    /**
     * Seadistab peamise lava põhiomadused.
     */
    private void seadistaPeamineLava() {
        peaLava.setTitle("Küberturvalisuse Simulaator");
        peaLava.setMinWidth(MIN_WINDOW_SIZE);
        peaLava.setMinHeight(MIN_WINDOW_SIZE);
        peaLava.setWidth(DEFAULT_WINDOW_SIZE);
        peaLava.setHeight(DEFAULT_WINDOW_SIZE);
        
        // Määrame ikooni, kui saadaval
        laadiRakendusIkoon();
        peaLava.show();
    }
    
    /**
     * Laadib rakenduse ikooni.
     */
    private void laadiRakendusIkoon() {
        try {
            Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
            peaLava.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Hoiatus: Rakenduse ikooni laadimine ebaõnnestus: " + e.getMessage());
        }
    }
    
    /**
     * Loob vajalikud kaustad andmete salvestamiseks.
     */
    private void looAndmeteKaust() {
        File andmeteKaust = new File(ANDMETE_KAUST);
        if (!andmeteKaust.exists() && !andmeteKaust.mkdirs()) {
            System.err.println("Hoiatus: Andmete kausta loomine ebaõnnestus!");
        }
    }
    
    /**
     * Näitab kasutaja valiku ekraani.
     */
    private void näitaKasutajaValikEkraan() {
        Scene stseen = looKasutajaValikEkraan();
        lisaCSS(stseen);
        peaLava.setScene(stseen);
    }
    
    /**
     * Loob kasutaja valiku ekraani.
     */
    private Scene looKasutajaValikEkraan() {
        BorderPane juurPaneel = new BorderPane();
        juurPaneel.setPadding(new Insets(20));
        
        // Pealkiri
        Label pealkiri = new Label("Vali kasutaja");
        pealkiri.getStyleClass().add("title-label");
        juurPaneel.setTop(looKeskendatudPaneel(pealkiri));
        
        // Sisu
        VBox sisu = looKasutajaValikSisu();
        ScrollPane kerimisAla = new ScrollPane(sisu);
        kerimisAla.setFitToWidth(true);
        juurPaneel.setCenter(kerimisAla);
        
        // Klaviatuuri tugi
        Scene stseen = new Scene(juurPaneel);
        stseen.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) Platform.exit();
        });
        
        // Määrame fookuse ListView-le pärast stseeni laadimist
        Platform.runLater(() -> {
            // Otsime ListView komponenti ja määrame sellele fookuse
            stseen.getRoot().lookupAll(".list-view").stream()
                  .findFirst()
                  .ifPresent(node -> node.requestFocus());
        });
        
        return stseen;
    }
    
    /**
     * Loob kasutaja valiku sisu.
     */
    private VBox looKasutajaValikSisu() {
        VBox sisu = new VBox(15);
        sisu.setAlignment(Pos.CENTER);
        
        List<String> olemasolevadKasutajad = KasutajaProfiil.saaOlemasolevadKasutajadSorteeritult();
        
        if (!olemasolevadKasutajad.isEmpty()) {
            sisu.getChildren().addAll(
                looSubtiitel("Olemasolevad kasutajad:"),
                looKasutajateLoend(olemasolevadKasutajad, sisu)
            );

            // Kasutaja kustutamise nupp
            Button kustutaBtn = looKohandatudNupp("Kustuta kasutaja", 200, sisu, _ -> kustutaKasutaja());
            sisu.getChildren().add(kustutaBtn);
        } else {
            Label teade = new Label("Hetkel pole ühtegi kasutajat. Loo uus kasutaja, et alustada.");
            teade.setWrapText(true);
            teade.getStyleClass().add("subtitle-label");
            sisu.getChildren().add(teade);
        }
        
        // Uue kasutaja loomise nupp
        Button uusKasutajaBtn = looKohandatudNupp("Loo uus kasutaja", 200, sisu, _ -> looUusKasutaja());
        sisu.getChildren().add(uusKasutajaBtn);
        
        return sisu;
    }
    
    /**
     * Loob kasutajate loendi koos valimise nupuga.
     */
    private VBox looKasutajateLoend(List<String> kasutajad, VBox konteiner) {
        VBox loendPaneel = new VBox(10);
        
        ListView<String> loend = new ListView<>();
        loend.getItems().addAll(kasutajad);
        loend.setPrefHeight(200);
        loend.prefWidthProperty().bind(Bindings.max(300, konteiner.widthProperty().multiply(0.8)));
        
        // Valime automaatselt esimese kasutaja
        if (!kasutajad.isEmpty()) {
            loend.getSelectionModel().selectFirst();
        }
        
        Button valiBtn = looKohandatudNupp("Vali kasutaja", 200, konteiner, _ -> {
            String valitud = loend.getSelectionModel().getSelectedItem();
            if (valitud != null) {
                valiKasutaja(valitud);
            } else {
                näitaViga("Palun vali kasutaja loendist");
            }
        });
        
        // Enter-klahvi tugi
        loend.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String valitud = loend.getSelectionModel().getSelectedItem();
                if (valitud != null) {
                    valiKasutaja(valitud);
                }
            }
        });
        
        // Topelt-klõpsu tugi
        loend.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String valitud = loend.getSelectionModel().getSelectedItem();
                if (valitud != null) {
                    valiKasutaja(valitud);
                }
            }
        });
        
        loendPaneel.getChildren().addAll(loend, valiBtn);
        return loendPaneel;
    }
    
    /**
     * Valib kasutaja ja laeb tema profiili.
     */
    private void valiKasutaja(String kasutajaNimi) {
        this.praeguneKasutaja = kasutajaNimi;
        
        if (!kasutajaNimi.equals("Külaline")) {
            this.profiil = KasutajaProfiil.laeProfiil(kasutajaNimi);
            if (this.profiil == null) {
                this.profiil = new KasutajaProfiil(kasutajaNimi);
                this.profiil.salvestaProfiil();
            }
            this.logiHaldur = new LogiHaldur(kasutajaNimi);
            logiHaldur.logiSündmus("Sisenes mängu");
        }
        
        näitaPeamenüü();
    }
    
    /**
     * Avab dialoogi uue kasutaja loomiseks.
     */
    private void looUusKasutaja() {
        KasutajaLoomisDialoog dialoog = new KasutajaLoomisDialoog();
        String uusKasutajaNimi = dialoog.näitaDialoog(peaLava);
        
        if (uusKasutajaNimi != null && !uusKasutajaNimi.isEmpty()) {
            this.profiil = new KasutajaProfiil(uusKasutajaNimi);
            this.profiil.salvestaProfiil();
            this.logiHaldur = new LogiHaldur(uusKasutajaNimi);
            logiHaldur.logiSündmus("Loodud uus kasutaja");
            this.praeguneKasutaja = uusKasutajaNimi;
            näitaPeamenüü();
        }
    }
    
    /**
     * Avab kasutaja kustutamise dialoogi.
     */
    private void kustutaKasutaja() {
        KasutajaKustutamiseDialoog dialoog = new KasutajaKustutamiseDialoog();
        boolean kasutajaKustutati = dialoog.näitaDialoog(peaLava);
        
        if (kasutajaKustutati) {
            näitaKasutajaValikEkraan();
        }
    }

    /**
     * Näitab peamenüü ekraani.
     */
    private void näitaPeamenüü() {
        Scene stseen = looPeamenüüEkraan();
        lisaCSS(stseen);
        peaLava.setScene(stseen);
    }
    
    /**
     * Loob peamenüü ekraani.
     */
    private Scene looPeamenüüEkraan() {
        BorderPane juurPaneel = new BorderPane();
        juurPaneel.setPadding(new Insets(20));
        
        // Päis
        VBox päis = looTervitusePäis();
        juurPaneel.setTop(päis);
        
        // Menüü nupud klaasipaneelina
        VBox menüüPaneel = looMenüüPaneel();
        StackPane keskmineKontainer = new StackPane(menüüPaneel);
        juurPaneel.setCenter(keskmineKontainer);
        
        // Klaviatuuri tugi
        Scene stseen = new Scene(juurPaneel);
        stseen.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                logi("Väljus peamenüüsse");
                näitaKasutajaValikEkraan();
            }
        });
        
        return stseen;
    }
    
    /**
     * Loob tervituse päise.
     */
    private VBox looTervitusePäis() {
        VBox päis = new VBox(10);
        päis.setAlignment(Pos.CENTER);
        päis.setPadding(new Insets(0, 0, 20, 0));
        
        Label pealkiri = new Label("Küberturvalisuse Simulaator");
        pealkiri.getStyleClass().add("title-label");
        
        Label kasutajaInfo = new Label("Kasutaja: " + praeguneKasutaja);
        kasutajaInfo.getStyleClass().add("subtitle-label");
        
        päis.getChildren().addAll(pealkiri, kasutajaInfo);
        return päis;
    }
    
    /**
     * Loob peamenüü nuppude paneeli.
     */
    private VBox looMenüüPaneel() {
        VBox menüü = new VBox(15);
        menüü.getStyleClass().add("glass-pane");
        menüü.setAlignment(Pos.CENTER);
        menüü.setMaxWidth(400);
        
        // Kirjeldus
        Label kirjeldus = new Label(
            "Tere tulemast Küberturvalisuse Simulaatorisse! Siin saad harjutada küberturvalisuse stsenaariumite läbimist, " +
            "teha otsuseid ja õppida, kuidas kaitsta ennast ja ettevõtet erinevate küberohtude eest."
        );
        kirjeldus.setWrapText(true);
        kirjeldus.setPadding(new Insets(0, 0, 10, 0));
        
        // Nupud
        Button[] nupud = {
            looMenüüNupp("Mängi juhuslikku stsenaariumit", menüü, _ -> käivitaJuhuslikStsenaarium()),
            looMenüüNupp("Vali kategooria", menüü, _ -> näitaKategooriadEkraan()),
            looMenüüNupp("Vaata statistikat", menüü, _ -> näitaStatistika()),
            looMenüüNupp("Juhised", menüü, _ -> näitaJuhisedEkraan()),
            looMenüüNupp("Tagasi kasutaja valikusse", menüü, _ -> {
                logi("Väljus peamenüüsse");
                näitaKasutajaValikEkraan();
            }),
            looMenüüNupp("Välju", menüü, _ -> {
                logiSessioonLõpp();
                peaLava.close();
            })
        };
        
        menüü.getChildren().add(kirjeldus);
        menüü.getChildren().addAll(nupud);
        return menüü;
    }
    
    /**
     * Näitab kategooriate valiku ekraani.
     */
    private void näitaKategooriadEkraan() {
        Scene stseen = looKategooriadEkraan();
        lisaCSS(stseen);
        peaLava.setScene(stseen);
    }
    
    /**
     * Loob kategooriate valiku ekraani.
     */
    private Scene looKategooriadEkraan() {
        BorderPane juurPaneel = new BorderPane();
        juurPaneel.setPadding(new Insets(20));
        
        // Pealkiri
        Label pealkiri = new Label("Vali kategooria");
        pealkiri.getStyleClass().add("title-label");
        juurPaneel.setTop(looKeskendatudPaneel(pealkiri));
        
        // Kategooriate paneel
        VBox kategooriadPaneel = looKategooriadPaneel();
        ScrollPane kerimisAla = new ScrollPane(new StackPane(kategooriadPaneel));
        kerimisAla.setFitToWidth(true);
        juurPaneel.setCenter(kerimisAla);
        
        return looStseenKlaviatuuriTuega(juurPaneel, this::näitaPeamenüü);
    }
    
    /**
     * Loob kategooriate paneeli.
     */
    private VBox looKategooriadPaneel() {
        VBox paneel = new VBox(15);
        paneel.getStyleClass().add("glass-pane");
        paneel.setPadding(new Insets(20));
        paneel.setAlignment(Pos.CENTER);
        paneel.setMaxWidth(500);
        
        String[] kategooriad = haldur.saaKategooriad();
        Map<String, String> kirjeldused = getKategoriaKirjeldused();
        
        for (String kategooria : kategooriad) {
            paneel.getChildren().add(looKategooriaKaart(kategooria, kirjeldused.get(kategooria)));
        }
        
        Button tagasiBtn = looStandardNupp("Tagasi", 200, _ -> näitaPeamenüü());
        paneel.getChildren().add(tagasiBtn);
        
        return paneel;
    }
    
    /**
     * Loob kategooria kaardi.
     */
    private VBox looKategooriaKaart(String kategooria, String kirjeldus) {
        VBox kaart = new VBox(5);
        kaart.setPadding(new Insets(10));
        kaart.setStyle("-fx-border-color: #e0e0e0; -fx-border-radius: 5px;");
        
        Label nimi = looSubtiitel(kategooria);
        Label info = new Label(kirjeldus != null ? kirjeldus : "Selle kategooria stsenaariumid aitavad sul õppida küberturvalisuse olulisi aspekte.");
        info.setWrapText(true);
        
        Button valiBtn = looStandardNupp("Vali see kategooria", 180, _ -> käivitaKategooriaStsenaarium(kategooria));
        
        kaart.getChildren().addAll(nimi, info, valiBtn);
        return kaart;
    }
    
    /**
     * Tagastab kategooriate kirjeldused.
     */
    private Map<String, String> getKategoriaKirjeldused() {
        Map<String, String> kirjeldused = new HashMap<>();
        kirjeldused.put("Phishing", "Õpi tuvastama ja reageerima phishing-rünnakutele. Need stsenaariumid õpetavad kuidas kaitsta end ja oma ettevõtet petuskeemide eest.");
        kirjeldused.put("Paroolid", "Harjuta turvalisuse paroolide valimist ja kaitsmist. Need stsenaariumid õpetavad parooli turvalisuse põhimõtteid.");
        return kirjeldused;
    }
    
    /**
     * Näitab statistika ekraani.
     */
    private void näitaStatistika() {
        StatistikaEkraan statistikaEkraan = new StatistikaEkraan(peaLava, praeguneKasutaja, this::näitaPeamenüü);
        statistikaEkraan.näita();
    }
    
    /**
     * Näitab juhiste ekraani.
     */
    private void näitaJuhisedEkraan() {
        logi("Vaatas juhiseid");
        Scene stseen = looJuhisedEkraan();
        lisaCSS(stseen);
        peaLava.setScene(stseen);
    }
    
    /**
     * Loob juhiste ekraani.
     */
    private Scene looJuhisedEkraan() {
        BorderPane juurPaneel = new BorderPane();
        juurPaneel.setPadding(new Insets(20));
        
        // Pealkiri
        Label pealkiri = new Label("Juhised");
        pealkiri.getStyleClass().add("title-label");
        juurPaneel.setTop(looKeskendatudPaneel(pealkiri));
        
        // Juhiste sisu
        VBox juhisedPaneel = looJuhisedSisu();
        ScrollPane kerimisAla = new ScrollPane(juhisedPaneel);
        kerimisAla.setFitToWidth(true);
        juurPaneel.setCenter(kerimisAla);
        
        // Tagasi nupp
        Button tagasiBtn = looStandardNupp("Tagasi", 200, _ -> näitaPeamenüü());
        juurPaneel.setBottom(looKeskendatudPaneel(tagasiBtn));
        
        return looStseenKlaviatuuriTuega(juurPaneel, this::näitaPeamenüü);
    }
    
    /**
     * Loob juhiste sisu.
     */
    private VBox looJuhisedSisu() {
        VBox sisu = new VBox(15);
        sisu.getStyleClass().add("glass-pane");
        sisu.setPadding(new Insets(20));
        
        // Juhiste sektsioonid
        sisu.getChildren().addAll(
            looJuhisedSektsioon("STSENAARIUMITES NAVIGEERIMINE", new String[]{
                "• Loe hoolikalt stsenaariumi kirjeldust",
                "• Vasta küsimustele, valides sobiva vastusevariandi",
                "• Kasuta lisainfo nuppu täiendava teabe saamiseks",
                "• Iga stsenaariumi küsimus sisaldab mitmeid vastusevariante, millel on erinev mõju sinu skoorile",
                "• Pärast vastuse valimist näed tagajärgi ja õpetlikku selgitust",
                "• Soovi korral saad igal hetkel stsenaariumist väljuda, kuid sellisel juhul loetakse see ebaõnnestunud läbimiseks"
            }),
            looJuhisedSektsioon("MÄNGIJA PROFIIL", new String[]{
                "• Loo endale kasutaja profiil, et jälgida oma edusamme",
                "• Sinu tulemused salvestatakse automaatselt",
                "• Statistika ekraanil näed oma koguskoori ja läbitud stsenaariumeid",
                "• Kui soovid alustada uuesti, võid luua uue profiili"
            }),
            looJuhisedSektsioon("KÜBERTURVALISUSE ÕPPIMINE", new String[]{
                "• Iga stsenaarium õpetab sulle olulisi küberturvalisuse põhimõtteid",
                "• Pärast stsenaariumi läbimist kuvatakse kokkuvõte ja õpetlik info",
                "• Mida rohkem stsenaariumeid läbid, seda paremini õpid tuvastama ja reageerima küberturvalisuse ohtudele",
                "• Püüa iga kategooria stsenaariumid läbida, et õppida erinevate ohtude kohta"
            }),
            looJuhisedSektsioon("KLAVIATUURI JA HIIRE KASUTAMINE", new String[]{
                "• Hiire klõpsuga saad valida vastusevariante ja vajutada nuppe",
                "• Enter klahv: kinnita valik või jätka",
                "• Escape klahv: tagasi või tühista",
                "• Tab klahv: liigu erinevate elementide vahel"
            })
        );
        
        return sisu;
    }
    
    /**
     * Loob juhiste sektsiooni.
     */
    private VBox looJuhisedSektsioon(String pealkiri, String[] punktid) {
        VBox sektsioon = new VBox(5);
        
        Label tiitel = looSubtiitel(pealkiri);
        TextFlow tekst = new TextFlow();
        tekst.setLineSpacing(5);
        
        for (String punkt : punktid) {
            tekst.getChildren().add(new Text(punkt + "\n"));
        }
        
        sektsioon.getChildren().addAll(tiitel, tekst);
        return sektsioon;
    }
    
    /**
     * Käivitab juhusliku stsenaariumi.
     */
    private void käivitaJuhuslikStsenaarium() {
        Stsenaarium stsenaarium = haldur.juhuslikStsenaarium();
        if (stsenaarium != null) {
            näitaStsenaariumiEkraan(stsenaarium);
        } else {
            näitaViga("Stsenaariumite laadimine ebaõnnestus!");
        }
    }
    
    /**
     * Käivitab valitud kategooria stsenaariumi.
     */
    private void käivitaKategooriaStsenaarium(String kategooria) {
        Stsenaarium stsenaarium = haldur.saaKategooriast(kategooria);
        if (stsenaarium != null) {
            näitaStsenaariumiEkraan(stsenaarium);
        } else {
            näitaViga("Valitud kategoorias pole stsenaariumeid!");
        }
    }
    
    /**
     * Näitab stsenaariumi ekraani.
     */
    private void näitaStsenaariumiEkraan(Stsenaarium stsenaarium) {
        StsenaariumiEkraan stsenaariumiEkraan = new StsenaariumiEkraan(
            peaLava, stsenaarium, praeguneKasutaja, this::näitaPeamenüü
        );
        stsenaariumiEkraan.näita();
    }
    
    // ============== UTILIITIMEETODID ==============
    
    /**
     * Loob menüü nupu kohandatud suuruse ja funktsionaalsusega.
     */
    private Button looMenüüNupp(String tekst, VBox konteiner, Consumer<Void> tegevus) {
        Button nupp = looKohandatudNupp(tekst, 300, konteiner, tegevus);
        return nupp;
    }
    
    /**
     * Loob kohandatud nupu.
     */
    private Button looKohandatudNupp(String tekst, int minimaalseLaius, VBox konteiner, Consumer<Void> tegevus) {
        Button nupp = new Button(tekst);
        nupp.setMaxWidth(Double.MAX_VALUE);
        nupp.setPrefWidth(minimaalseLaius);
        nupp.prefWidthProperty().bind(
            Bindings.max(minimaalseLaius, konteiner.widthProperty().multiply(0.8))
        );
        nupp.setOnAction(_ -> tegevus.accept(null));
        return nupp;
    }
    
    /**
     * Loob standardse nupu.
     */
    private Button looStandardNupp(String tekst, int laius, Consumer<Void> tegevus) {
        Button nupp = new Button(tekst);
        nupp.setPrefWidth(laius);
        nupp.setOnAction(_ -> tegevus.accept(null));
        return nupp;
    }
    
    /**
     * Loob subtiiitli labeli.
     */
    private Label looSubtiitel(String tekst) {
        Label label = new Label(tekst);
        label.getStyleClass().add("subtitle-label");
        return label;
    }
    
    /**
     * Loob keskendatud paneeli ühele komponendile.
     */
    private VBox looKeskendatudPaneel(javafx.scene.Node komponent) {
        VBox paneel = new VBox(komponent);
        paneel.setAlignment(Pos.CENTER);
        return paneel;
    }
    
    /**
     * Loob stseeni koos klaviatuuri toega.
     */
    private Scene looStseenKlaviatuuriTuega(javafx.scene.Parent juur, Runnable escapeTegevus) {
        Scene stseen = new Scene(juur);
        stseen.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE && escapeTegevus != null) {
                escapeTegevus.run();
            }
        });
        return stseen;
    }
    
    /**
     * Lisab stseenile CSS stiilid.
     */
    private void lisaCSS(Scene stseen) {
        stseen.getStylesheets().add(getClass().getResource(CSS_PATH).toExternalForm());
    }
    
    /**
     * Näitab veateadet kasutajale.
     */
    private void näitaViga(String veateade) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Viga");
        alert.setContentText(veateade);
        alert.showAndWait();
    }
    
    /**
     * Logib sündmuse, kui logi haldur on saadaval.
     */
    private void logi(String sündmus) {
        if (logiHaldur != null && !praeguneKasutaja.equals("Külaline")) {
            logiHaldur.logiSündmus(sündmus);
        }
    }
    
    /**
     * Logib sessiooni lõpu.
     */
    private void logiSessioonLõpp() {
        if (logiHaldur != null && !praeguneKasutaja.equals("Külaline")) {
            logiHaldur.logiSessioonLõpp(
                profiil.getLäbitudStsenaariumiteArv(),
                profiil.getKoguSkoor()
            );
        }
    }
}
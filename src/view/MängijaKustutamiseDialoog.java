package view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.KasutajaProfiil;

import java.util.List;
import java.util.Optional;

/**
 * KasutajaKustutamiseDialoog klass kuvab dialoogi kasutaja kustutamiseks.
 * Nagu digitaalse konto kustutamise protsess - nõuab kinnitust ja hoiatab tagajärgede eest.
 * 
 * @author Kevin Laig, Kaili Must
 */
public class MängijaKustutamiseDialoog {
    
    private boolean kasutajaKustutati = false;
    
    /**
     * Näitab kasutaja kustutamise dialoogi.
     * 
     * @param omanikLava omaniku lava
     * @return true, kui kasutaja kustutati, false vastasel juhul
     */
    public boolean näitaDialoog(Stage omanikLava) {
        // Laeme olemasolevad kasutajad
        List<String> kasutajad = KasutajaProfiil.saaOlemasolevadMängijadSorteeritult();
        
        if (kasutajad.isEmpty()) {
            näitaTeade("Ühtegi kasutajat pole kustutamiseks saadaval.", Alert.AlertType.INFORMATION);
            return false;
        }
        
        // Loome dialoogi lava
        Stage dialoogLava = new Stage();
        dialoogLava.initModality(Modality.WINDOW_MODAL);
        dialoogLava.initOwner(omanikLava);
        dialoogLava.setTitle("Kustuta kasutaja");
        dialoogLava.setMinWidth(400);
        dialoogLava.setMinHeight(350);
        
        // Loome komponendid
        VBox juurPaneel = new VBox(15);
        juurPaneel.setPadding(new Insets(20));
        juurPaneel.setAlignment(Pos.TOP_CENTER);
        
        // Hoiatus
        Label hoiatusLabel = new Label("HOIATUS");
        hoiatusLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        hoiatusLabel.setTextFill(Color.RED);
        
        Label selgitusLabel = new Label("Mängija kustutamine eemaldab KÕIK mängijaga seotud andmed:\n" +
                "• Kõik läbitud stsenaariumid ja skoorid\n" +
                "• Kogu statistika ja ajalugu\n" +
                "• Kõik logifailid\n\n" +
                "See tegevust ei saa tagasi võtta!");
        selgitusLabel.setWrapText(true);
        selgitusLabel.setStyle("-fx-font-size: 14px;");
        
        Label juhisLabel = new Label("Vali kustutatav mängija:");
        juhisLabel.getStyleClass().add("subtitle-label");
        
        // Kasutajate loend
        ListView<String> kasutajateLoend = new ListView<>();
        kasutajateLoend.getItems().addAll(kasutajad);
        kasutajateLoend.setPrefHeight(150);
        kasutajateLoend.getSelectionModel().selectFirst();
        
        // Nuppude rida
        HBox nupudRida = new HBox(15);
        nupudRida.setAlignment(Pos.CENTER);
        
        Button kustutaBtn = new Button("Kustuta mängija");
        kustutaBtn.setPrefWidth(140);
        kustutaBtn.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white;");
        
        Button tühistaBtn = new Button("Tühista");
        tühistaBtn.setPrefWidth(120);
        
        nupudRida.getChildren().addAll(kustutaBtn, tühistaBtn);
        
        // Sündmuste käsitlejad
        kustutaBtn.setOnAction(_ -> {
            String valitudKasutaja = kasutajateLoend.getSelectionModel().getSelectedItem();
            if (valitudKasutaja != null) {
                if (kinnitaKustutamine(valitudKasutaja)) {
                    if (KasutajaProfiil.kustutaProfiil(valitudKasutaja)) {
                        kasutajaKustutati = true;
                        näitaTeade("Mängija '" + valitudKasutaja + "' kustutati edukalt!", Alert.AlertType.INFORMATION);
                        dialoogLava.close();
                    } else {
                        näitaTeade("Viga mängija kustutamisel. Palun proovi uuesti.", Alert.AlertType.ERROR);
                    }
                }
            } else {
                näitaTeade("Palun vali kustutamiseks mängija!", Alert.AlertType.WARNING);
            }
        });
        
        tühistaBtn.setOnAction(_ -> {
            kasutajaKustutati = false;
            dialoogLava.close();
        });
        
        // Klaviatuuri tugi
        juurPaneel.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                kustutaBtn.fire();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                tühistaBtn.fire();
            }
        });
        
        // Määrame fookuse loendile
        Platform.runLater(() -> kasutajateLoend.requestFocus());
        
        // Loome paigutuse
        juurPaneel.getChildren().addAll(
            hoiatusLabel, selgitusLabel, juhisLabel, kasutajateLoend, nupudRida
        );
        
        // Seadistame stseeni ja lisame CSS
        Scene stseen = new Scene(juurPaneel);
        stseen.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        dialoogLava.setScene(stseen);
        
        // Näitame dialoogi ja ootame selle sulgemist
        dialoogLava.showAndWait();
        
        return kasutajaKustutati;
    }
    
    /**
    * Küsib kasutajalt kinnitust kustutamiseks.
    */
    private boolean kinnitaKustutamine(String mängijaNimi) {
        Alert kinnitusDialoog = new Alert(Alert.AlertType.CONFIRMATION);
        kinnitusDialoog.setTitle("Kinnita kustutamine");
        kinnitusDialoog.setHeaderText("Kas oled kindel?");
        kinnitusDialoog.setContentText("Kas soovid tõesti kustutada mängija '" + mängijaNimi + "'?\n\n" +
                "Kõik tema andmed kustutatakse jäädavalt ja seda ei saa tagasi võtta!");
        
        ButtonType jahNupp = new ButtonType("Jah, kustuta");
        ButtonType eiNupp = new ButtonType("Ei, tühista");
        kinnitusDialoog.getButtonTypes().setAll(jahNupp, eiNupp);
        
        kinnitusDialoog.getDialogPane().getStylesheets().add(
            getClass().getResource("/styles.css").toExternalForm()
        );
        
        Optional<ButtonType> tulemus = kinnitusDialoog.showAndWait();
        return tulemus.isPresent() && tulemus.get() == jahNupp;
    }
    
    /**
     * Näitab teadet kasutajale.
     */
    private void näitaTeade(String teade, Alert.AlertType tüüp) {
        Alert alert = new Alert(tüüp);
        alert.setTitle(tüüp == Alert.AlertType.ERROR ? "Viga" : 
                      tüüp == Alert.AlertType.WARNING ? "Hoiatus" : "Teade");
        alert.setHeaderText(null);
        alert.setContentText(teade);
        alert.showAndWait();
    }
}
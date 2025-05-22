package view;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * KasutajaLoomisDialoog klass kuvab dialoogi uue kasutaja loomiseks.
 * 
 * @author Kevin Laig, Kaili Must
 */
public class KasutajaLoomisDialoog {
    
    private String kasutajaNimi = null;
    
    /**
     * Näitab kasutaja loomise dialoogi.
     * 
     * @param omanikLava omaniku lava
     * @return loodud kasutaja nimi või null, kui loomine tühistati
     */
    public String näitaDialoog(Stage omanikLava) {
        // Loome uue lava
        Stage dialoogLava = new Stage();
        dialoogLava.initModality(Modality.WINDOW_MODAL);
        dialoogLava.initOwner(omanikLava);
        dialoogLava.setTitle("Loo uus kasutaja");
        dialoogLava.setMinWidth(350);
        dialoogLava.setMinHeight(280);
        
        try {
            Image icon = new Image(getClass().getResourceAsStream("/icon.png"));
            dialoogLava.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("Hoiatus: Ikooni laadimine ebaõnnestus dialoogile: " + e.getMessage());
        }

        // Loome komponendid
        VBox juurPaneel = new VBox(15);
        juurPaneel.setPadding(new Insets(20));
        juurPaneel.setAlignment(Pos.CENTER);
        
        Label juhisLabel = new Label("Sisesta kasutaja nimi:");
        juhisLabel.getStyleClass().add("subtitle-label");
        
        // Lisame täpsema juhise
        Label validatsiooniJuhis = new Label("(Ainult tähed ja numbrid, vähemalt 3 märki)");
        validatsiooniJuhis.setStyle("-fx-font-size: 12px; -fx-text-fill: #666666;");
        
        TextField nimiVäli = new TextField();
        nimiVäli.setPromptText("Kasutaja nimi");
        nimiVäli.setPrefWidth(250);
        
        HBox nupudRida = new HBox(15);
        nupudRida.setAlignment(Pos.CENTER);
        
        Button looBtn = new Button("Loo kasutaja");
        looBtn.setPrefWidth(120);
        
        Button tühistaBtn = new Button("Tühista");
        tühistaBtn.setPrefWidth(120);
        
        nupudRida.getChildren().addAll(looBtn, tühistaBtn);
        
        // Lisa validatsioon
        Label veaLabel = new Label("");
        veaLabel.setTextFill(Color.RED);
        veaLabel.setVisible(false);
        
        // Seadistame sündmuste käsitlejad
        looBtn.setOnAction(_ -> {
            String nimi = nimiVäli.getText().trim();
            String viga = valideerNimi(nimi);
            
            if (viga != null) {
                veaLabel.setText(viga);
                veaLabel.setVisible(true);
            } else {
                this.kasutajaNimi = nimi;
                dialoogLava.close();
            }
        });
        
        tühistaBtn.setOnAction(_ -> {
            this.kasutajaNimi = null;
            dialoogLava.close();
        });
        
        // Lisa klaviatuuri sündmuste käsitleja
        juurPaneel.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                looBtn.fire();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                tühistaBtn.fire();
            }
        });
        
        // Määrame fookuse tekstiväljale
        Platform.runLater(() -> nimiVäli.requestFocus());
        
        // Loome paigutuse
        juurPaneel.getChildren().addAll(juhisLabel, validatsiooniJuhis, nimiVäli, veaLabel, nupudRida);
        
        // Seadistame stseeni ja lisame CSS
        Scene stseen = new Scene(juurPaneel);
        stseen.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        dialoogLava.setScene(stseen);
        
        // Näitame dialoogi ja ootame selle sulgemist
        dialoogLava.showAndWait();
        
        return kasutajaNimi;
    }
    
    /**
     * Valideerib kasutaja nime rangelt.
     * Lubatud on ainult tähed (a-z, A-Z, ä, ö, ü, õ) ja numbrid (0-9).
     * 
     * @param nimi valideeritav nimi
     * @return veateade või null, kui nimi on kehtiv
     */
    private String valideerNimi(String nimi) {
        // Kontrollime, kas nimi on tühi
        if (nimi == null || nimi.isEmpty()) {
            return "Nimi ei saa olla tühi!";
        }
        
        // Kontrollime minimaalset pikkust
        if (nimi.length() < 3) {
            return "Nimi peab olema vähemalt 3 tähemärki pikk!";
        }
        
        // Kontrollime maksimaalset pikkust
        if (nimi.length() > 25) {
            return "Nimi ei saa olla pikem kui 25 tähemärki!";
        }
        
        // Kontrollime, et nimi sisaldaks ainult lubatud märke
        if (!nimi.matches("[a-zA-ZäöüõÄÖÜÕ0-9]+")) {
            return "Nimi võib sisaldada ainult tähti ja numbreid!\nTühikud, kirjavahemärgid ja erimärgid pole lubatud.";
        }
        
        // Kontrollime, et ei koosneks ainult numbritest
        if (nimi.matches("^[0-9]+$")) {
            return "Nimi ei saa koosneda ainult numbritest!";
        }
        
        // Kui jõudsime siia, on nimi kehtiv
        return null;
    }
}
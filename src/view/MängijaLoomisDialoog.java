package view;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * MängijaLoomisDialoog klass kuvab dialoogi uue mängija loomiseks.
 * 
 * @author Kevin Laig, Kaili Must
 */
public class MängijaLoomisDialoog {
    
    private String mängijaNimi = null;
    
    /**
     * Näitab mängija loomise dialoogi.
     * 
     * @param omanikLava omaniku lava
     * @return loodud mängija nimi või null, kui loomine tühistati
     */
    public String näitaDialoog(Stage omanikLava) {
        // Loome uue lava
        Stage dialoogLava = new Stage();
        dialoogLava.initModality(Modality.WINDOW_MODAL);
        dialoogLava.initOwner(omanikLava);
        dialoogLava.setTitle("Loo uus mängija");
        dialoogLava.setMinWidth(350);
        dialoogLava.setMinHeight(220);
        
        // Loome komponendid
        VBox juurPaneel = new VBox(15);
        juurPaneel.setPadding(new Insets(20));
        juurPaneel.setAlignment(Pos.CENTER);
        
        Label juhisLabel = new Label("Sisesta mängija nimi:");
        juhisLabel.getStyleClass().add("subtitle-label");
        
        TextField nimiVäli = new TextField();
        nimiVäli.setPromptText("Mängija nimi");
        nimiVäli.setPrefWidth(250);
        
        HBox nupudRida = new HBox(15);
        nupudRida.setAlignment(Pos.CENTER);
        
        Button looBtn = new Button("Loo mängija");
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
            if (nimi.isEmpty()) {
                veaLabel.setText("Nimi ei saa olla tühi!");
                veaLabel.setVisible(true);
            } else if (nimi.length() < 3) {
                veaLabel.setText("Nimi peab olema vähemalt 3 tähemärki pikk!");
                veaLabel.setVisible(true);
            } else {
                this.mängijaNimi = nimi;
                dialoogLava.close();
            }
        });
        
        tühistaBtn.setOnAction(_ -> {
            this.mängijaNimi = null;
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
        juurPaneel.getChildren().addAll(juhisLabel, nimiVäli, veaLabel, nupudRida);
        
        // Seadistame stseeni ja lisame CSS
        Scene stseen = new Scene(juurPaneel);
        // Kuna see on eraldi klass, peame lisama CSS-i otse
        stseen.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        dialoogLava.setScene(stseen);
        
        // Näitame dialoogi ja ootame selle sulgemist
        dialoogLava.showAndWait();
        
        return mängijaNimi;
    }
}

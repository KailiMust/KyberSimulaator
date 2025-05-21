import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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
        dialoogLava.setMinWidth(300);
        dialoogLava.setMinHeight(200);
        
        // Loome komponendid
        Label juhisLabel = new Label("Sisesta mängija nimi:");
        TextField nimiVäli = new TextField();
        nimiVäli.setPromptText("Mängija nimi");
        
        Button looBtn = new Button("Loo mängija");
        Button tühistaBtn = new Button("Tühista");
        
        // Seadistame sündmuste käsitlejad
        looBtn.setOnAction(_ -> {
            String nimi = nimiVäli.getText().trim();
            if (!nimi.isEmpty()) {
                mängijaNimi = nimi;
                dialoogLava.close();
            }
        });
        
        tühistaBtn.setOnAction(_ -> {
            mängijaNimi = null;
            dialoogLava.close();
        });
        
        // Loome paigutuse
        VBox juurPaneel = new VBox(15);
        juurPaneel.setPadding(new Insets(20));
        juurPaneel.setAlignment(Pos.CENTER);
        juurPaneel.getChildren().addAll(juhisLabel, nimiVäli, looBtn, tühistaBtn);
        
        // Seadistame stseeni
        Scene stseen = new Scene(juurPaneel);
        dialoogLava.setScene(stseen);
        
        // Näitame dialoogi ja ootame selle sulgemist
        dialoogLava.showAndWait();
        
        return mängijaNimi;
    }
}

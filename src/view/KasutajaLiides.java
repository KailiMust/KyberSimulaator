package view;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * KasutajaLiides klass haldab kasutajaga suhtlemist.
 * Võimaldab kasutada kas konsooli (Scanner) või hüpikaknaid (JOptionPane) kasutajaga suhtlemiseks.
 *
 * @author Kevin Laig, Kaili Must
 */
public class KasutajaLiides {
    /** Flag, mis näitab, kas kasutada JOptionPane-i (true) või Scanner-it (false) */
    private final boolean kasutaJOptionPane;

    /** Scanner objekt konsooli kaudu sisendi lugemiseks */
    private Scanner scanner;

    /** StringBuffer objekt JOptionPane sõnumite kogumiseks */
    private StringBuffer messageBuffer;

    /** Viimase kasutajadialoogi tekst, mida näidata koos sisendiga */
    private String currentDialogText;

    /**
     * Konstruktor, mis määrab, millist kasutajaliidese tüüpi kasutatakse.
     *
     * @param kasutaJOptionPane true kui kasutatakse JOptionPane, false kui Scanner
     */
    public KasutajaLiides(boolean kasutaJOptionPane) {
        this.kasutaJOptionPane = kasutaJOptionPane;

        if (!kasutaJOptionPane) {
            scanner = new Scanner(System.in);
        } else {
            messageBuffer = new StringBuffer();
            currentDialogText = "";
        }
    }

    /**
     * Küsib kasutajalt sisendit ja kuvab küsimuse.
     *
     * @param küsimus kasutajale näidatav küsimus
     * @return kasutaja sisestatud tekst
     */
    public String küsiSisend(String küsimus) {
        if (kasutaJOptionPane) {
            // Lisame teated ja küsimuse ühte dialoogiaknasse
            String displayText = currentDialogText;
            if (!displayText.isEmpty()) {
                displayText += "\n\n";
            }
            displayText += küsimus;

            // Küsime kasutajalt sisendit ja tühjendame puhvri
            String sisend = JOptionPane.showInputDialog(null, displayText,
                    "Küberturvalisuse Simulaator", JOptionPane.QUESTION_MESSAGE);

            messageBuffer = new StringBuffer();
            currentDialogText = "";

            return sisend;
        } else {
            System.out.print(küsimus + " ");
            return scanner.nextLine();
        }
    }

    /**
     * Näitab kasutajale teadet. Kui kasutatakse JOptionPane,
     * kogub teated puhvrisse ja näitab need koos järgmise sisendküsimusega.
     *
     * @param teade näidatav teade
     */
    public void näitaTeade(String teade) {
        if (kasutaJOptionPane) {
            // Kogume teated puhvrisse
            if (!messageBuffer.isEmpty()) {
                messageBuffer.append("\n");
            }
            messageBuffer.append(teade);

            // Uuendame dialoogi teksti
            currentDialogText = messageBuffer.toString();

            // Konsoolis näitame samuti teadet
            System.out.println(teade);
        } else {
            System.out.println(teade);
        }
    }

    /**
     * Näitab kasutajale veateadet.
     *
     * @param veateade näidatav veateade
     */
    public void näitaViga(String veateade) {
        if (kasutaJOptionPane) {
            // Vea puhul näitame kohe hoiatusdialoogi
            JOptionPane.showMessageDialog(null, veateade,
                    "Viga", JOptionPane.ERROR_MESSAGE);

            // Kirjutame vea ka puhvrisse, et see säiliks järgmises dialoogis
            if (!messageBuffer.isEmpty()) {
                messageBuffer.append("\n");
            }
            messageBuffer.append("VIGA: " + veateade);

            // Uuendame jooksva dialoogi teksti
            currentDialogText = messageBuffer.toString();

            // Konsoolis näitame ka
            System.out.println("VIGA: " + veateade);
        } else {
            System.out.println("VIGA: " + veateade);
        }
    }

    /**
     * Puhastab sõnumipuhvri, et alustada uut dialoogi puhtalt lehelt.
     */
    public void puhastaPuhver() {
        if (kasutaJOptionPane) {
            messageBuffer = new StringBuffer();
            currentDialogText = "";
        }
    }
}
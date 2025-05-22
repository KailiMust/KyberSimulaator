# Küberturvalisuse Simulaator

See projekt on küberturvalisuse simulaator, mis on loodud JavaFX-iga.

## Projekti käivitamine ilma Maveni installimiseta

### Vajalikud eeldused
- Java JDK 24 (või uuem)
- Internetiühendus (esmakordsel käivitamisel)

### Projekti käivitamine Windowsis
1. Projekti juurkaustas on juba olemas Maven Wrapper failid (mvnw.cmd, .mvn kaust), mida pole vaja eraldi alla laadida
2. Ava käsurida (Command Prompt või PowerShell)
3. Navigeeri projekti kausta (asenda "tee\projekti\kausta" tegeliku teega, kus projekt asub):
   ```
   cd C:\Users\kasutajanimi\Documents\GitHub\KyberSimulaator
   ```
4. Käivita projekt Maven Wrapper abil:
   ```
   .\mvnw.cmd clean javafx:run
   ```

## Projekti seadistamine ja käivitamine IDE-s (IntelliJ IDEA / Eclipse / VS Code)

### IntelliJ IDEA
1. Ava IntelliJ IDEA
2. Vali "File" > "Open" ja vali projekti kaust
3. Kui avaneb dialoog "Import Project from Maven", klõpsa "OK"
4. Ava "Run" > "Edit Configurations"
5. Klõpsa "+" nuppu ja vali "Maven"
6. Määra järgmised seaded:
   - Name: KyberSimulaator
   - Command line: `clean javafx:run`
   - Märgi "Resolve Workspace artifacts"
7. Klõpsa "Apply" ja "OK"
8. Käivita projekt valides "Run" > "Run 'KyberSimulaator'"

### VS Code
1. Ava VS Code
2. Ava projekti kaust ("File" > "Open Folder")
3. Installi "Extension Pack for Java" laiendus, kui seda pole veel tehtud
4. Ava käsurida VS Code-is ("Terminal" > "New Terminal")
5. Sisesta käsk:
   ```
   .\mvnw.cmd clean javafx:run
   ```

### Veaparandus: "java.lang.NoClassDefFoundError: Scene"
Kui saad vea "NoClassDefFoundError: Scene", siis JavaFX moodulid pole korrektselt seadistatud. Lahendus:

1. Lisa projekti käivitamisel järgmised VM argumendid:
   ```
   --module-path "C:\path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.web,javafx.swing,javafx.media,javafx.base
   ```
   (Asenda "C:\path\to\javafx-sdk" tegeliku JavaFX SDK asukohaga)

2. Või kasuta Maven Wrapper-it, mis lahendab sõltuvused automaatselt:
   ```
   .\mvnw.cmd clean javafx:run
   ```

## Projekti kompileerimine üksikuks käivitatavaks failiks

### JAR-faili loomine
1. Käivita järgmine käsk projekti kaustas:
   ```
   .\mvnw.cmd clean package
   ```
2. Loodud JAR-fail asub `target` kaustas

## Projekti struktuur
- `src/` - Lähtekoodi failid
- `resources/` - Ressursifailid (CSS, pildid jne)
- `data/` - Andmefailid (kasutajate profiilid jne)
- `target/` - Kompileeritud failid (genereeritakse ehitamise käigus)

## Märkused
- Esmakordsel käivitamisel laeb Maven Wrapper alla kõik vajalikud sõltuvused, mis võib võtta mõne minuti.
- Veenduge, et teil on installitud õige Java versioon (JDK 24 või uuem).

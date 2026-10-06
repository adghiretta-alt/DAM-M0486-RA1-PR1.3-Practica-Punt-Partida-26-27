package com.project.pr13;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.project.pr13.format.PersonaFormatter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PR130MainTest {

    private static final String XML_CONTENT = """
            <?xml version="1.0" encoding="UTF-8"?>
            <persones>
                <persona>
                    <nom>Maria</nom>
                    <cognom>López</cognom>
                    <edat>36</edat>
                    <ciutat>Barcelona</ciutat>
                </persona>
                <persona>
                    <nom>Gustavo</nom>
                    <cognom>Catadasús</cognom>
                    <edat>15</edat>
                    <ciutat>London</ciutat>
                </persona>
            </persones>
            """;

    private static final String CAPÇALERES = """
            Nom      Cognom         Edat  Ciutat
            -------- -------------- ----- ---------""";

    private static final String DADES_PERSONES = """
            Maria    López          36    Barcelona
            Gustavo  Catadasús      15    London""";

    @TempDir
    File tempDir;  // Directori temporal creat automàticament per JUnit

    private PR130Main app;
    private File tempFile;

    @BeforeEach
    void setup() throws IOException {
        // Crea un fitxer temporal "persones.xml" dins del directori temporal creat per JUnit
        tempFile = new File(tempDir, "persones.xml");
        try (FileWriter writer = new FileWriter(tempFile, StandardCharsets.UTF_8)) {
            writer.write(XML_CONTENT);
        }

        // Inicialitza l'objecte PR130Main amb el directori temporal
        app = new PR130Main(tempDir);
    }

    /**
     * Unifica els salts de línia (Windows i Linux) i elimina els espais del final de cada línia,
     * que no es veuen per pantalla.
     */
    private static String normalitzar(String text) {
        return text.replace("\r\n", "\n").lines()
                .map(String::stripTrailing)
                .collect(Collectors.joining("\n"))
                .strip();
    }

    /**
     * Executa l'acció i retorna el text que ha escrit per consola.
     */
    private static String capturarSortida(Runnable accio) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            accio.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    void testParseXML() {
        // Verifica que el document es parsegi correctament des del fitxer temporal
        Document doc = PR130Main.parseXML(tempFile);
        assertNotNull(doc, "El document no hauria de ser nul després de parsejar.");
    }

    @Test
    void testLlegeixPersones() {
        // Comprova que el document conté dues persones
        Document doc = PR130Main.parseXML(tempFile);
        assertNotNull(doc, "El document no hauria de ser nul després de parsejar.");
        NodeList persones = doc.getElementsByTagName("persona");
        assertEquals(2, persones.getLength(), "Hauria d'haver-hi dues persones al document XML.");
    }

    @Test
    void testImprimirCapçaleres() {
        assertEquals(CAPÇALERES, normalitzar(PersonaFormatter.getCapçaleres()), "Les capçaleres no són correctes.");
    }

    @Test
    void testFormatarPersona() {
        assertEquals("Maria    López          36    Barcelona",
                normalitzar(PersonaFormatter.formatarPersona("Maria", "López", "36", "Barcelona")),
                "El format de la persona no és correcte.");
        assertEquals("Armengol Pastor         72    Abidjan",
                normalitzar(PersonaFormatter.formatarPersona("Armengol", "Pastor", "72", "Abidjan")),
                "Les columnes han de tenir una amplada fixa, encara que el text sigui més curt.");
    }

    @Test
    void testImprimirCapçaleresPerConsola() {
        String sortida = capturarSortida(PR130Main::imprimirCapçaleres);
        assertEquals(CAPÇALERES, normalitzar(sortida), "imprimirCapçaleres ha d'escriure les capçaleres per consola.");
    }

    @Test
    void testImprimirDadesPersones() {
        Document doc = PR130Main.parseXML(tempFile);
        assertNotNull(doc, "El document no hauria de ser nul després de parsejar.");
        NodeList persones = doc.getElementsByTagName("persona");

        String sortida = capturarSortida(() -> PR130Main.imprimirDadesPersones(persones));
        assertEquals(DADES_PERSONES, normalitzar(sortida),
                "imprimirDadesPersones ha d'escriure una línia formatada per a cada persona.");
    }

    @Test
    void testProcessFile() {
        // Comprova la sortida completa del programa: capçaleres i dades alineades
        String sortida = capturarSortida(() -> app.processarFitxerXML("persones.xml"));
        assertEquals(CAPÇALERES + "\n" + DADES_PERSONES, normalitzar(sortida),
                "La sortida per pantalla no coincideix amb la sortida esperada de l'enunciat.");
    }
}

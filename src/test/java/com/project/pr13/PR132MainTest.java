package com.project.pr13;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PR132MainTest {

    private static final String XML_CONTENT = """
            <?xml version="1.0" encoding="UTF-8"?>
            <cursos>
                <curs id="AMS2">
                    <tutor>LARA, Francesc</tutor>
                    <alumnes>
                        <alumne>ALVAREZ, Tomas</alumne>
                        <alumne>CAMACHO, David</alumne>
                    </alumnes>
                    <moduls>
                        <modul id="M06">
                            <titol>Accés a dades</titol>
                        </modul>
                    </moduls>
                </curs>
                <curs id="AWS1">
                    <tutor>Julian Fuentes</tutor>
                    <alumnes>
                        <alumne>FERNANDEZ, Ruben</alumne>
                        <alumne>JANSSEN, Gerard</alumne>
                    </alumnes>
                </curs>
            </cursos>
            """;

    @TempDir
    File tempDir;  // Directori temporal proporcionat per JUnit

    private Path tempFilePath;
    private PR132Main app;

    @BeforeEach
    void setup() throws IOException {
        // Crear el fitxer temporal "cursos.xml" dins del directori temporal
        File tempFile = new File(tempDir, "cursos.xml");
        tempFilePath = tempFile.toPath();

        // Escriure el contingut de base al fitxer "cursos.xml"
        try (FileWriter writer = new FileWriter(tempFile, StandardCharsets.UTF_8)) {
            writer.write(XML_CONTENT);
        }

        // Inicialitzar l'objecte PR132Main amb el fitxer temporal "cursos.xml"
        app = new PR132Main(tempFilePath);
    }

    @Test
    void testLlistarCursos() {
        // Cada fila ha de tenir l'ID del curs, el tutor i el total d'alumnes
        List<List<String>> cursos = app.llistarCursos();
        assertEquals(List.of(
                        List.of("AMS2", "LARA, Francesc", "2"),
                        List.of("AWS1", "Julian Fuentes", "2")),
                cursos, "La llista de cursos (ID, tutor, total d'alumnes) no és correcta.");
    }

    @Test
    void testMostrarModuls() {
        // Cada fila ha de tenir l'ID i el títol del mòdul
        assertEquals(List.of(List.of("M06", "Accés a dades")), app.mostrarModuls("AMS2"),
                "El curs AMS2 hauria de tenir només el mòdul M06, 'Accés a dades'.");
        assertEquals(List.of(), app.mostrarModuls("AWS1"),
                "El curs AWS1 no té mòduls: la llista hauria de ser buida.");
    }

    @Test
    void testLlistarAlumnes() {
        // Només s'han de retornar els alumnes del curs indicat
        assertEquals(List.of("ALVAREZ, Tomas", "CAMACHO, David"), app.llistarAlumnes("AMS2"),
                "La llista d'alumnes del curs AMS2 no és correcta.");
        assertEquals(List.of("FERNANDEZ, Ruben", "JANSSEN, Gerard"), app.llistarAlumnes("AWS1"),
                "La llista d'alumnes del curs AWS1 no és correcta.");
    }

    @Test
    void testAfegirAlumne() {
        // Afegir un nou alumne al curs AWS1
        app.afegirAlumne("AWS1", "NOU, Alumne");

        List<String> alumnesAWS1 = app.llistarAlumnes("AWS1");
        assertEquals(3, alumnesAWS1.size(), "El curs AWS1 hauria de tenir 3 alumnes.");
        assertTrue(alumnesAWS1.containsAll(List.of("FERNANDEZ, Ruben", "JANSSEN, Gerard", "NOU, Alumne")),
                "L'alumne s'hauria d'haver afegit al curs AWS1 sense perdre els que ja hi eren.");
        assertEquals(List.of("ALVAREZ, Tomas", "CAMACHO, David"), app.llistarAlumnes("AMS2"),
                "Els altres cursos no s'han de modificar.");

        // El canvi s'ha de desar al fitxer: una instància nova l'ha de veure
        PR132Main appNova = new PR132Main(tempFilePath);
        assertTrue(appNova.llistarAlumnes("AWS1").contains("NOU, Alumne"),
                "El canvi s'hauria d'haver desat al fitxer XML.");
    }

    @Test
    void testEliminarAlumne() {
        // Eliminar l'alumne CAMACHO, David del curs AMS2
        app.eliminarAlumne("AMS2", "CAMACHO, David");

        assertEquals(List.of("ALVAREZ, Tomas"), app.llistarAlumnes("AMS2"),
                "Al curs AMS2 només hi hauria de quedar ALVAREZ, Tomas.");
        assertEquals(List.of("FERNANDEZ, Ruben", "JANSSEN, Gerard"), app.llistarAlumnes("AWS1"),
                "Els altres cursos no s'han de modificar.");

        // El canvi s'ha de desar al fitxer: una instància nova l'ha de veure
        PR132Main appNova = new PR132Main(tempFilePath);
        assertFalse(appNova.llistarAlumnes("AMS2").contains("CAMACHO, David"),
                "El canvi s'hauria d'haver desat al fitxer XML.");
    }

    @Test
    void testUtilitzaXPath() throws IOException {
        // L'enunciat demana fer servir XPath per navegar per l'arbre XML.
        // Es revisa el codi font de PR132Main, sense tenir en compte els comentaris.
        String codi = Files.readString(Path.of("src", "main", "java", "com", "project", "pr13", "PR132Main.java"));
        String codiSenseComentaris = codi
                .replaceAll("(?s)/\\*.*?\\*/", "")
                .replaceAll("//[^\\n]*", "");

        assertTrue(codiSenseComentaris.contains(".evaluate(") || codiSenseComentaris.contains(".compile("),
                "Cal fer servir XPath (evaluate) per navegar per l'arbre XML.");
        assertFalse(codiSenseComentaris.contains("getElementsByTagName"),
                "Cal navegar per l'arbre XML amb XPath, no amb getElementsByTagName.");
    }
}

package com.project.pr13;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PR131MainTest {

    @TempDir
    File tempDir;  // Directori temporal proporcionat per JUnit

    private PR131Main app;

    @BeforeEach
    void setup() {
        // Inicialitza l'objecte PR131Main amb el directori temporal creat per JUnit
        app = new PR131Main(tempDir);
    }

    /**
     * Comprova que el document té exactament el contingut de l'annex "biblioteca.xml".
     */
    private static void comprovarBiblioteca(Document doc) {
        Element biblioteca = doc.getDocumentElement();
        assertEquals("biblioteca", biblioteca.getTagName(), "L'element arrel hauria de ser 'biblioteca'.");

        NodeList llibres = biblioteca.getElementsByTagName("llibre");
        assertEquals(1, llibres.getLength(), "Hi hauria d'haver un sol llibre.");
        Element llibre = (Element) llibres.item(0);
        assertEquals("001", llibre.getAttribute("id"), "L'ID del llibre hauria de ser '001'.");

        String[][] camps = {
                {"titol", "El viatge dels venturons"},
                {"autor", "Joan Pla"},
                {"anyPublicacio", "1998"},
                {"editorial", "Edicions Mar"},
                {"genere", "Aventura"},
                {"pagines", "320"},
                {"disponible", "true"},
        };
        for (String[] camp : camps) {
            NodeList nodes = llibre.getElementsByTagName(camp[0]);
            assertEquals(1, nodes.getLength(), "El llibre hauria de tenir un element '" + camp[0] + "'.");
            assertEquals(camp[1], nodes.item(0).getTextContent(),
                    "El valor de '" + camp[0] + "' hauria de ser '" + camp[1] + "'.");
        }

        int fills = 0;
        for (Node node = llibre.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                fills++;
            }
        }
        assertEquals(camps.length, fills, "El llibre no hauria de tenir més elements que els de l'annex.");
    }

    @Test
    void testConstruirDocument() {
        // L'enunciat demana construir el document amb DocumentBuilder
        Document doc = PR131Main.construirDocument();
        assertNotNull(doc, "construirDocument ha de crear el document amb DocumentBuilder i retornar-lo.");
        comprovarBiblioteca(doc);
    }

    @Test
    void testProcessDocumentCreatesFile() {
        // Comprova que el fitxer biblioteca.xml es crea correctament
        app.processarFitxerXML("biblioteca.xml");

        // Comprova que el fitxer s'ha creat dins del directori temporal
        File outputFile = new File(tempDir, "biblioteca.xml");
        assertTrue(outputFile.exists(), "El fitxer biblioteca.xml hauria d'existir.");
    }

    @Test
    void testDocumentContent() throws Exception {
        // Executa la generació del fitxer XML
        app.processarFitxerXML("biblioteca.xml");

        // Llegeix el fitxer generat. Si no existeix o no és XML vàlid, el test falla amb l'excepció.
        File outputFile = new File(tempDir, "biblioteca.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(outputFile);
        doc.getDocumentElement().normalize();

        comprovarBiblioteca(doc);
    }

    @Test
    void testSetAndGetDataDir() {
        // Comprova el getter i el setter de dataDir
        File newDataDir = new File(tempDir, "newDir");
        app.setDataDir(newDataDir);
        assertEquals(newDataDir, app.getDataDir(), "El getter hauria de retornar el nou directori assignat.");
    }
}

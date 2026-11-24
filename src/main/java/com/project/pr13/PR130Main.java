package com.project.pr13;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.project.pr13.format.PersonaFormatter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Programa que lee un fichero XML con información
 * de personas y la muestra por pantalla.
 */
public class PR130Main {

    // Directorio donde se encuentran los XML
    private final File dataDir;

    /**
     * Constructor.
     */
    public PR130Main(File dataDir) {
        this.dataDir = dataDir;
    }

    /**
     * Punto de entrada del programa.
     */
    public static void main(String[] args) {

        // Obtener la carpeta data/pr13
        String userDir = System.getProperty("user.dir");

        File dataDir = new File(
                userDir,
                "data" + File.separator + "pr13"
        );

        // Crear la aplicación
        PR130Main app = new PR130Main(dataDir);

        // Procesar el XML
        app.processarFitxerXML("persones.xml");
    }

    /**
     * Lee el XML y muestra las personas.
     */
    public void processarFitxerXML(String filename) {

        // Crear referencia al fichero XML
        File inputFile = new File(dataDir, filename);

        // Convertir XML a Document
        Document doc = parseXML(inputFile);

        if (doc != null) {

            // Obtener todos los nodos <persona>
            NodeList persones =
                    doc.getElementsByTagName("persona");

            // Mostrar encabezados
            imprimirCapçaleres();

            // Mostrar datos
            imprimirDadesPersones(persones);
        }
    }

    /**
     * Lee un XML y devuelve un Document.
     */
    public static Document parseXML(File inputFile) {

        try {

            // Crear fábrica de documentos XML
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            // Crear lector XML
            DocumentBuilder builder =
                    factory.newDocumentBuilder();

            // Leer y devolver el XML
            return builder.parse(inputFile);

        } catch (Exception e) {

            System.err.println(
                    "Error al llegir el fitxer XML: "
                    + e.getMessage()
            );

            return null;
        }
    }

    /**
     * Imprime las cabeceras de la tabla.
     */
    public static void imprimirCapçaleres() {

        System.out.println(
                PersonaFormatter.getCapçaleres()
        );
    }

    /**
     * Recorre todos los elementos persona
     * y muestra sus datos formateados.
     */
    public static void imprimirDadesPersones(
            NodeList persones) {

        // Recorrer todas las personas del XML
        for (int i = 0;
             i < persones.getLength();
             i++) {

            Element personaElement =
                    (Element) persones.item(i);

            // Obtener nombre
            String nom =
                    personaElement
                        .getElementsByTagName("nom")
                        .item(0)
                        .getTextContent();

            // Obtener apellido
            String cognom =
                    personaElement
                        .getElementsByTagName("cognom")
                        .item(0)
                        .getTextContent();

            // Obtener edad
            String edat =
                    personaElement
                        .getElementsByTagName("edat")
                        .item(0)
                        .getTextContent();

            // Obtener ciudad
            String ciutat =
                    personaElement
                        .getElementsByTagName("ciutat")
                        .item(0)
                        .getTextContent();

            // Mostrar persona formateada
            System.out.println(
                    PersonaFormatter.formatarPersona(
                            nom,
                            cognom,
                            edat,
                            ciutat
                    )
            );
        }
    }
}

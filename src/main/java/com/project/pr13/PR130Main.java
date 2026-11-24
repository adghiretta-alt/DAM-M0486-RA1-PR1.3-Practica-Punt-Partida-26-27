package com.project.pr13;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.project.pr13.format.PersonaFormatter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Programa que lee un fichero XML con datos de personas
 * y los muestra por pantalla.
 */
public class PR130Main {

    // Carpeta donde se encuentra el XML
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

        // Obtener el directorio actual del proyecto
        String userDir = System.getProperty("user.dir");

        // Construir la ruta data/pr13
        File dataDir = new File(
                userDir,
                "data" + File.separator + "pr13"
        );

        // Crear la aplicación
        PR130Main app = new PR130Main(dataDir);

        // Procesar el fichero XML
        app.processarFitxerXML("persones.xml");
    }

    /**
     * Lee el XML y muestra la información.
     */
    public void processarFitxerXML(String filename) {

        // Referencia al fichero XML
        File inputFile = new File(dataDir, filename);

        // Convertir XML a Document
        Document doc = parseXML(inputFile);

        if (doc != null) {

            // Obtener todos los nodos <persona>
            NodeList persones =
                    doc.getElementsByTagName("persona");

            // Mostrar cabeceras
            imprimirCapçaleres();

            // Mostrar datos de cada persona
            imprimirDadesPersones(persones);
        }
    }

    /**
     * Lee el fichero XML y devuelve un objeto Document.
     */
    public static Document parseXML(File inputFile) {

        try {

            // Crear fábrica XML
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            // Crear parser XML
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
     * Muestra las cabeceras.
     */
    public static void imprimirCapçaleres() {

        System.out.println(
                PersonaFormatter.getCapçaleres()
        );
    }

    /**
     * Recorre las personas encontradas en el XML
     * y muestra sus datos.
     */
    public static void imprimirDadesPersones(
            NodeList persones) {

        // Recorrer todos los nodos <persona>
        for (int i = 0; i < persones.getLength(); i++) {

            // Obtener una persona concreta de la lista
            Element personaElement =
                    (Element) persones.item(i);

            /*
             * Busca todas las etiquetas <nom>
             * dentro de la persona actual.
             *
             * getElementsByTagName("nom")
             * devuelve un NodeList.
             *
             * Ejemplo:
             *
             * <persona>
             *     <nom>Maria</nom>
             * </persona>
             *
             * Resultado:
             *
             * Posición 0 -> <nom>Maria</nom>
             *
             * item(0)
             * obtiene el primer elemento encontrado.
             *
             * Se usa 0 porque normalmente cada persona
             * tiene un único nombre.
             *
             * Si hubiera varios:
             *
             * <nom>Maria</nom>
             * <nom>Marieta</nom>
             *
             * item(0) -> Maria
             * item(1) -> Marieta
             *
             * y necesitaríamos un bucle para leerlos todos.
             *
             * getTextContent()
             * obtiene únicamente el texto.
             *
             * Resultado final:
             * "Maria"
             */
            String nom =
                    personaElement
                            .getElementsByTagName("nom")
                            .item(0)
                            .getTextContent();

            /*
             * Obtener el texto de:
             *
             * <cognom>Lopez</cognom>
             *
             * Resultado:
             * "Lopez"
             */
            String cognom =
                    personaElement
                            .getElementsByTagName("cognom")
                            .item(0)
                            .getTextContent();

            /*
             * Obtener el texto de:
             *
             * <edat>36</edat>
             *
             * Resultado:
             * "36"
             */
            String edat =
                    personaElement
                            .getElementsByTagName("edat")
                            .item(0)
                            .getTextContent();

            /*
             * Busca todas las etiquetas <ciutat>.
             *
             * Ejemplo:
             *
             * <ciutat>Barcelona</ciutat>
             *
             * getElementsByTagName("ciutat")
             * devuelve un NodeList.
             *
             * item(0)
             * obtiene el primer elemento de la lista.
             *
             * getTextContent()
             * devuelve el texto:
             *
             * "Barcelona"
             *
             * Si hubiera:
             *
             * <ciutat>Barcelona</ciutat>
             * <ciutat>Girona</ciutat>
             *
             * item(0) -> Barcelona
             * item(1) -> Girona
             */
            String ciutat =
                    personaElement
                            .getElementsByTagName("ciutat")
                            .item(0)
                            .getTextContent();

            // Mostrar la persona formateada
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

package com.project.pr13;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Programa que crea un XML de una biblioteca
 * y lo guarda en un fichero.
 */
public class PR131Main {

    // Directorio donde se guardará el XML
    private File dataDir;

    /**
     * Constructor.
     */
    public PR131Main(File dataDir) {
        this.dataDir = dataDir;
    }

    /**
     * Devuelve el directorio de trabajo.
     */
    public File getDataDir() {
        return dataDir;
    }

    /**
     * Cambia el directorio de trabajo.
     */
    public void setDataDir(File dataDir) {
        this.dataDir = dataDir;
    }

    /**
     * Punto de entrada del programa.
     */
    public static void main(String[] args) {

        // Obtener la ruta del proyecto
        String userDir = System.getProperty("user.dir");

        // Construir la ruta data/pr13
        File dataDir = new File(
                userDir,
                "data" + File.separator + "pr13"
        );

        // Crear la aplicación
        PR131Main app = new PR131Main(dataDir);

        // Generar el XML
        app.processarFitxerXML("biblioteca.xml");
    }

    /**
     * Construye y guarda el XML.
     */
    public void processarFitxerXML(String filename) {

        // Comprobar que exista el directorio
        if (comprovarIDirCrearDirectori(dataDir)) {

            // Crear el documento XML
            Document doc = construirDocument();

            // Crear fichero destino
            File fitxerSortida =
                    new File(dataDir, filename);

            // Guardar XML en disco
            guardarDocument(doc, fitxerSortida);
        }
    }

    /**
     * Comprueba si existe el directorio.
     * Si no existe lo crea.
     */
    private boolean comprovarIDirCrearDirectori(
            File directori) {

        if (!directori.exists()) {

            // Crear carpetas necesarias
            return directori.mkdirs();
        }

        return true;
    }

    /**
     * Construye el documento XML.
     */
    static Document construirDocument() {

        try {

            // Crear la factoría XML
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            // Crear el constructor XML
            DocumentBuilder builder =
                    factory.newDocumentBuilder();

            // Crear documento vacío
            Document doc =
                    builder.newDocument();

            /*
             * Crear elemento raíz.
             *
             * <biblioteca>
             * </biblioteca>
             */
            Element biblioteca =
                    doc.createElement("biblioteca");

            /*
             * appendChild()
             * añade el elemento al documento.
             *
             * Si no hacemos esto, el elemento existe
             * en memoria pero no forma parte del XML.
             */
            doc.appendChild(biblioteca);

            /*
             * Crear:
             *
             * <llibre>
             * </llibre>
             */
            Element llibre =
                    doc.createElement("llibre");

            /*
             * Añadir atributo:
             *
             * <llibre id="001">
             */
            llibre.setAttribute("id", "001");

            /*
             * Añadir llibre dentro de biblioteca.
             *
             * Resultado:
             *
             * <biblioteca>
             *     <llibre id="001">
             *     </llibre>
             * </biblioteca>
             */
            biblioteca.appendChild(llibre);

            /*
             * Crear:
             *
             * <titol>
             * </titol>
             */
            Element titol =
                    doc.createElement("titol");

            /*
             * setTextContent()
             * coloca texto entre la apertura
             * y cierre de la etiqueta.
             *
             * Resultado:
             *
             * <titol>
             *     El viatge dels venturons
             * </titol>
             */
            titol.setTextContent(
                    "El viatge dels venturons"
            );

            // Añadir el título al libro
            llibre.appendChild(titol);

            /*
             * Crear autor
             */
            Element autor =
                    doc.createElement("autor");

            autor.setTextContent("Joan Pla");

            llibre.appendChild(autor);

            /*
             * Crear año de publicación
             */
            Element anyPublicacio =
                    doc.createElement(
                            "anyPublicacio"
                    );

            anyPublicacio.setTextContent("1998");

            llibre.appendChild(anyPublicacio);

            /*
             * Crear editorial
             */
            Element editorial =
                    doc.createElement(
                            "editorial"
                    );

            editorial.setTextContent(
                    "Edicions Mar"
            );

            llibre.appendChild(editorial);

            /*
             * Crear género
             */
            Element genere =
                    doc.createElement("genere");

            genere.setTextContent("Aventura");

            llibre.appendChild(genere);

            /*
             * Crear número de páginas
             */
            Element pagines =
                    doc.createElement("pagines");

            pagines.setTextContent("320");

            llibre.appendChild(pagines);

            /*
             * Crear disponibilidad
             */
            Element disponible =
                    doc.createElement(
                            "disponible"
                    );

            disponible.setTextContent("true");

            llibre.appendChild(disponible);

            return doc;

        } catch (Exception e) {

            System.err.println(
                    "Error en crear el document XML: "
                    + e.getMessage()
            );

            return null;
        }
    }

    /**
     * Guarda el XML en un fichero.
     */
    private static void guardarDocument(
            Document doc,
            File fitxerSortida
    ) {

        if (doc == null) {

            System.err.println(
                    "No s'ha pogut crear el document XML."
            );

            return;
        }

        try {

            // Crear transformador XML
            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();

            Transformer transformer =
                    transformerFactory.newTransformer();

            // Formatear salida
            transformer.setOutputProperty(
                    OutputKeys.INDENT,
                    "yes"
            );

            transformer.setOutputProperty(
                    OutputKeys.ENCODING,
                    "UTF-8"
            );

            // Documento origen
            DOMSource source =
                    new DOMSource(doc);

            // Fichero destino
            StreamResult result =
                    new StreamResult(
                            fitxerSortida
                    );

            // Guardar XML
            transformer.transform(
                    source,
                    result
            );

            System.out.println(
                    "Fitxer XML creat correctament: "
                            + fitxerSortida.getAbsolutePath()
            );

        } catch (Exception e) {

            System.err.println(
                    "Error en guardar el document XML: "
                            + e.getMessage()
            );
        }
    }
}

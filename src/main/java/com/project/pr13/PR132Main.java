package com.project.pr13;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.project.pr13.format.AsciiTablePrinter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Aplicación para gestionar cursos almacenados
 * en un fichero XML.
 */
public class PR132Main {

    // Ruta del archivo XML
    private final Path xmlFilePath;

    // Scanner para leer datos por consola
    private static final Scanner scanner =
            new Scanner(System.in);

    /**
     * Constructor.
     */
    public PR132Main(Path xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }

    /**
     * Punto de entrada del programa.
     */
    public static void main(String[] args) {

        // Construir la ruta del XML
        String userDir =
                System.getProperty("user.dir");

        Path xmlFilePath =
                Paths.get(
                        userDir,
                        "data",
                        "pr13",
                        "cursos.xml"
                );

        // Crear aplicación
        PR132Main app =
                new PR132Main(xmlFilePath);

        // Iniciar menú
        app.executar();
    }

    /**
     * Bucle principal del menú.
     */
    public void executar() {

        boolean exit = false;

        while (!exit) {

            mostrarMenu();

            System.out.print(
                    "Escull una opció: "
            );

            try {

                int opcio =
                        Integer.parseInt(
                                scanner.nextLine()
                        );

                exit = processarOpcio(opcio);

            } catch (NumberFormatException e) {

                System.out.println(
                        "Introdueix un número vàlid."
                );
            }
        }
    }

    /**
     * Procesa la opción elegida.
     */
    public boolean processarOpcio(int opcio) {

        String cursId;
        String nomAlumne;

        switch (opcio) {

            case 1:

                // Obtener lista de cursos
                List<List<String>> cursos =
                        llistarCursos();

                // Mostrar tabla
                imprimirTaulaCursos(cursos);

                return false;

            case 2:

                // Pedir ID del curso
                System.out.print(
                        "Introdueix l'ID del curs per veure els seus mòduls: "
                );

                cursId = scanner.nextLine();

                // Obtener módulos
                List<List<String>> moduls =
                        mostrarModuls(cursId);

                imprimirTaulaModuls(moduls);

                return false;

            case 3:

                // Pedir ID del curso
                System.out.print(
                        "Introdueix l'ID del curs per veure la llista d'alumnes: "
                );

                cursId = scanner.nextLine();

                // Obtener alumnos
                List<String> alumnes =
                        llistarAlumnes(cursId);

                imprimirLlistaAlumnes(alumnes);

                return false;

            case 4:

                // Pedir curso
                System.out.print(
                        "Introdueix l'ID del curs on vols afegir l'alumne: "
                );

                cursId = scanner.nextLine();

                // Pedir nombre
                System.out.print(
                        "Introdueix el nom complet de l'alumne a afegir: "
                );

                nomAlumne =
                        scanner.nextLine();

                // Añadir alumno
                afegirAlumne(
                        cursId,
                        nomAlumne
                );

                return false;

            case 5:

                // Pedir curso
                System.out.print(
                        "Introdueix l'ID del curs on vols eliminar l'alumne: "
                );

                cursId = scanner.nextLine();

                // Pedir alumno
                System.out.print(
                        "Introdueix el nom complet de l'alumne a eliminar: "
                );

                nomAlumne =
                        scanner.nextLine();

                // Eliminar alumno
                eliminarAlumne(
                        cursId,
                        nomAlumne
                );

                return false;

            case 6:

                System.out.println(
                        "Sortint del programa..."
                );

                return true;

            default:

                System.out.println(
                        "Opció no reconeguda. Si us plau, prova de nou."
                );

                return false;
        }
    }

    /**
     * Muestra el menú.
     */
    private void mostrarMenu() {

        System.out.println("\nMENÚ PRINCIPAL");
        System.out.println("1. Llistar IDs de cursos i tutors");
        System.out.println("2. Mostrar IDs i títols dels mòduls d'un curs");
        System.out.println("3. Llistar alumnes d’un curs");
        System.out.println("4. Afegir un alumne a un curs");
        System.out.println("5. Eliminar un alumne d'un curs");
        System.out.println("6. Sortir");
    }

    /**
     * Obtiene todos los cursos.
     */
    public List<List<String>> llistarCursos() {

        List<List<String>> cursos =
                new ArrayList<>();

        try {

            // Cargar XML
            Document doc =
                    carregarDocumentXML(xmlFilePath);

            // Crear XPath
            XPath xpath =
                    XPathFactory.newInstance()
                            .newXPath();

            /*
             * XPath:
             *
             * /cursos/curs
             *
             * Busca todas las etiquetas:
             *
             * <curs>
             */
            NodeList nodesCursos =
                    (NodeList) xpath.evaluate(
                            "/cursos/curs",
                            doc,
                            XPathConstants.NODESET
                    );

            // Recorrer cursos encontrados
            for (int i = 0;
                 i < nodesCursos.getLength();
                 i++) {

                Element curs =
                        (Element) nodesCursos.item(i);

                /*
                 * Lee el atributo id
                 *
                 * <curs id="DAM">
                 *
                 * Resultado:
                 * "DAM"
                 */
                String id =
                        curs.getAttribute("id");

                /*
                 * Obtiene:
                 *
                 * <tutor>Joan</tutor>
                 *
                 * Resultado:
                 * "Joan"
                 */
                String tutor =
                        xpath.evaluate(
                                "tutor",
                                curs
                        );

                /*
                 * Cuenta cuántos alumnos existen.
                 */
                String totalAlumnes =
                        xpath.evaluate(
                                "count(alumnes/alumne)",
                                curs
                        );

                List<String> fila =
                        new ArrayList<>();

                fila.add(id);
                fila.add(tutor);
                fila.add(totalAlumnes);

                cursos.add(fila);
            }

        } catch (Exception e) {

            System.err.println(
                    "Error en llistar els cursos: "
                    + e.getMessage()
            );
        }

        return cursos;
    }

    /**
     * Imprime la tabla de cursos.
     */
    public void imprimirTaulaCursos(
            List<List<String>> cursos) {

        List<String> capçaleres =
                List.of(
                        "ID",
                        "Tutor",
                        "Total Alumnes"
                );

        AsciiTablePrinter.imprimirTaula(
                capçaleres,
                cursos
        );
    }

    /**
     * Obtiene los módulos de un curso.
     */
    public List<List<String>> mostrarModuls(
            String idCurs) {

        List<List<String>> moduls =
                new ArrayList<>();

        try {

            Document doc =
                    carregarDocumentXML(xmlFilePath);

            XPath xpath =
                    XPathFactory.newInstance()
                            .newXPath();

            /*
             * Ejemplo:
             *
             * /cursos/curs[@id='DAM']/moduls/modul
             *
             * Busca los módulos del curso DAM.
             */
            String expressio =
                    "/cursos/curs[@id="
                            + xpathLiteral(idCurs)
                            + "]/moduls/modul";

            NodeList nodesModuls =
                    (NodeList) xpath.evaluate(
                            expressio,
                            doc,
                            XPathConstants.NODESET
                    );

            for (int i = 0;
                 i < nodesModuls.getLength();
                 i++) {

                Element modul =
                        (Element) nodesModuls.item(i);

                String id =
                        modul.getAttribute("id");

                String titol =
                        xpath.evaluate(
                                "titol",
                                modul
                        );

                List<String> fila =
                        new ArrayList<>();

                fila.add(id);
                fila.add(titol);

                moduls.add(fila);
            }

        } catch (Exception e) {

            System.err.println(
                    "Error en mostrar els mòduls: "
                    + e.getMessage()
            );
        }

        return moduls;
    }

    /**
     * Imprime los módulos.
     */
    public void imprimirTaulaModuls(
            List<List<String>> moduls) {

        List<String> capçaleres =
                List.of(
                        "ID Mòdul",
                        "Títol"
                );

        AsciiTablePrinter.imprimirTaula(
                capçaleres,
                moduls
        );
    }

    /**
     * Obtiene los alumnos de un curso.
     */
    public List<String> llistarAlumnes(
            String idCurs) {

        List<String> alumnes =
                new ArrayList<>();

        try {

            Document doc =
                    carregarDocumentXML(xmlFilePath);

            XPath xpath =
                    XPathFactory.newInstance()
                            .newXPath();

            String expressio =
                    "/cursos/curs[@id="
                            + xpathLiteral(idCurs)
                            + "]/alumnes/alumne";

            NodeList nodesAlumnes =
                    (NodeList) xpath.evaluate(
                            expressio,
                            doc,
                            XPathConstants.NODESET
                    );

            for (int i = 0;
                 i < nodesAlumnes.getLength();
                 i++) {

                /*
                 * getTextContent()
                 * obtiene el texto del nodo.
                 *
                 * <alumne>Anna Puig</alumne>
                 *
                 * Resultado:
                 * "Anna Puig"
                 */
                alumnes.add(
                        nodesAlumnes
                                .item(i)
                                .getTextContent()
                );
            }

        } catch (Exception e) {

            System.err.println(
                    "Error en llistar els alumnes: "
                    + e.getMessage()
            );
        }

        return alumnes;
    }

    /**
     * Muestra los alumnos.
     */
    public void imprimirLlistaAlumnes(
            List<String> alumnes) {

        System.out.println("Alumnes:");

        alumnes.forEach(
                alumne ->
                        System.out.println(
                                "- " + alumne
                        )
        );
    }

    /**
     * Añade un alumno al XML.
     */
    public void afegirAlumne(
            String idCurs,
            String nomAlumne) {

        try {

            Document doc =
                    carregarDocumentXML(xmlFilePath);

            XPath xpath =
                    XPathFactory.newInstance()
                            .newXPath();

            String expressio =
                    "/cursos/curs[@id="
                            + xpathLiteral(idCurs)
                            + "]/alumnes";

            NodeList resultats =
                    (NodeList) xpath.evaluate(
                            expressio,
                            doc,
                            XPathConstants.NODESET
                    );

            if (resultats.getLength() == 0) {

                System.out.println(
                        "No existeix el curs "
                                + idCurs
                );

                return;
            }

            Element alumnes =
                    (Element) resultats.item(0);

            NodeList existents =
                    alumnes.getElementsByTagName(
                            "alumne"
                    );

            // Comprobar duplicados
            for (int i = 0;
                 i < existents.getLength();
                 i++) {

                if (existents.item(i)
                        .getTextContent()
                        .trim()
                        .equalsIgnoreCase(
                                nomAlumne.trim())) {

                    System.out.println(
                            "Aquest alumne ja està inscrit al curs."
                    );

                    return;
                }
            }

            /*
             * Crea:
             *
             * <alumne></alumne>
             */
            Element alumne =
                    doc.createElement("alumne");

            /*
             * Añade texto:
             *
             * <alumne>Pere Garcia</alumne>
             */
            alumne.setTextContent(
                    nomAlumne.trim()
            );

            /*
             * Añade la etiqueta dentro de:
             *
             * <alumnes>
             */
            alumnes.appendChild(alumne);

            // Guardar cambios
            guardarDocumentXML(doc);

            System.out.println(
                    "Alumne afegit correctament al curs "
                            + idCurs
            );

        } catch (Exception e) {

            System.err.println(
                    "Error en afegir l'alumne: "
                    + e.getMessage()
            );
        }
    }

    /**
     * Elimina un alumno.
     */
    public void eliminarAlumne(
            String idCurs,
            String nomAlumne) {

        try {

            Document doc =
                    carregarDocumentXML(xmlFilePath);

            XPath xpath =
                    XPathFactory.newInstance()
                            .newXPath();

            String expressio =
                    "/cursos/curs[@id="
                            + xpathLiteral(idCurs)
                            + "]/alumnes/alumne";

            NodeList alumnes =
                    (NodeList) xpath.evaluate(
                            expressio,
                            doc,
                            XPathConstants.NODESET
                    );

            Node alumneEliminar = null;

            for (int i = 0;
                 i < alumnes.getLength();
                 i++) {

                Node alumne =
                        alumnes.item(i);

                if (alumne.getTextContent()
                        .trim()
                        .equalsIgnoreCase(
                                nomAlumne.trim())) {

                    alumneEliminar = alumne;
                    break;
                }
            }

            if (alumneEliminar == null) {

                System.out.println(
                        "No s'ha trobat l'alumne al curs indicat."
                );

                return;
            }

            /*
             * Elimina el nodo encontrado.
             */
            alumneEliminar
                    .getParentNode()
                    .removeChild(
                            alumneEliminar
                    );

            guardarDocumentXML(doc);

            System.out.println(
                    "Alumne eliminat correctament del curs "
                            + idCurs
            );

        } catch (Exception e) {

            System.err.println(
                    "Error en eliminar l'alumne: "
                    + e.getMessage()
            );
        }
    }

    /**
     * Carga el XML.
     */
    private Document carregarDocumentXML(
            Path pathToXml) {

        try {

            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();

            DocumentBuilder builder =
                    factory.newDocumentBuilder();

            return builder.parse(
                    pathToXml.toFile()
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error en carregar el document XML.",
                    e
            );
        }
    }

    /**
     * Guarda el XML modificado.
     */
    private void guardarDocumentXML(
            Document document) {

        try {

            // Eliminar espacios vacíos
            eliminarEspaisEnBlanc(document);

            TransformerFactory transformerFactory =
                    TransformerFactory.newInstance();

            Transformer transformer =
                    transformerFactory.newTransformer();

            transformer.setOutputProperty(
                    OutputKeys.INDENT,
                    "yes"
            );

            transformer.setOutputProperty(
                    OutputKeys.ENCODING,
                    "UTF-8"
            );

            /*
             * Convertir Document en archivo XML.
             */
            DOMSource source =
                    new DOMSource(document);

            StreamResult result =
                    new StreamResult(
                            xmlFilePath.toFile()
                    );

            transformer.transform(
                    source,
                    result
            );

            System.out.println(
                    "El fitxer XML ha estat guardat amb èxit."
            );

        } catch (Exception e) {

            System.err.println(
                    "Error en guardar el fitxer XML: "
                    + e.getMessage()
            );
        }
    }

    /**
     * Elimina nodos de texto vacíos.
     */
    private static void eliminarEspaisEnBlanc(
            Node node) {

        NodeList fills =
                node.getChildNodes();

        for (int i = fills.getLength() - 1;
             i >= 0;
             i--) {

            Node fill =
                    fills.item(i);

            if (fill.getNodeType()
                    == Node.TEXT_NODE
                    && fill.getTextContent()
                    .isBlank()) {

                node.removeChild(fill);

            } else {

                eliminarEspaisEnBlanc(fill);
            }
        }
    }

    /**
     * Escapa comillas para XPath.
     */
    private static String xpathLiteral(
            String valor) {

        if (!valor.contains("'")) {
            return "'" + valor + "'";
        }

        if (!valor.contains("\"")) {
            return "\"" + valor + "\"";
        }

        String[] parts =
                valor.split("'", -1);

        StringBuilder resultat =
                new StringBuilder("concat(");

        for (int i = 0;
             i < parts.length;
             i++) {

            if (i > 0) {
                resultat.append(", \"'\", ");
            }

            resultat.append("'")
                    .append(parts[i])
                    .append("'");
        }

        resultat.append(")");

        return resultat.toString();
    }
}

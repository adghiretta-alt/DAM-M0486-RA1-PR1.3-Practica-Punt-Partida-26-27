# Pràctica PR1.3 - Punt de partida #

Aquest projecte correspon a la pràctica PR1.3 relacionada amb treball amb documents XML

### Instruccions ###

Cal completar el codi marcat amb `// *************** CODI PRÀCTICA **********************/` a les classes `PR130Main`, `format/PersonaFormatter`, `PR131Main` i `PR132Main`, seguint l'enunciat de la pràctica.

La pràctica està acabada quan el codi passa tots els tests (`mvn test`).

### Compilació i funcionament ###

Cal el 'Maven' per compilar el projecte
```bash
mvn clean
mvn compile
mvn clean compile test package
```

Per executar el projecte a Windows cal
```bash
.\run.ps1 com.project.pr13.PR13Main
```

Per executar el projecte a Linux/macOS cal
```bash
./run.sh com.project.pr13.PR13Main
```

Per fer anar classes específiques amb main:
```bash
.\run.ps1 com.project.pr13.PR130Main
./run.sh com.project.pr13.PR130Main
```

Per executar sense usar script propi, directament amb maven:
```bash
mvn exec:java "-Dexec.mainClass=com.project.pr13.PR13Main"
```

Per executar, un cop generat l'artefacte .jar (`mvn package`, o bé `mvn package -DskipTests` si encara no passen tots els tests)
```bash
java -cp ./target/project-name-1.0.0.jar com.project.pr13.PR13Main
```

### Execució de tests ###
```bash
# Executar TOTS els tests
mvn test
# Executar un test individual especificant package o només nom del test
mvn test "-Dtest=com.project.pr13.PR130MainTest"
mvn test -Dtest=PR130MainTest
# Executar múltiples tests específics (separats per comes)
mvn test -Dtest="PR130MainTest,PR131MainTest,PR132MainTest"
# Executar un sol mètode d'un test
mvn test -Dtest="PR132MainTest#testAfegirAlumne"
# Tots els tests que comencin amb "PR13"
mvn test -Dtest="PR13*"
```

### Visual Studio Code: resseteig de l'entorn de programació Java ###

Si Visual Studio code no es comporta com esperem i hem provat a solucionar-ho sense èxit podem provar aquestes dues solucions:

* Recarregar la Finestra: Obre la Paleta de Comandes (**Ctrl+Maj+P**), escriu Developer: "**Reload Window**" i prem Enter.

* Netejar l'Espai de Treball: Si recarregar no funciona, obre de nou la Paleta de Comandes (**Ctrl+Maj+P**), escriu "**Java: Clean Java Language Server Workspace**" i prem Enter. Aquesta és una solució molt eficaç per a molts problemes relacionats amb Java a VS Code. Se't demanarà que recarreguis i tornis a escanejar el projecte.

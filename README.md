# Projet de Recherche et Indexation avec Apache Lucene

Ce projet contient quatre programmes Java utilisant **Apache Lucene** :
- **Ajout.java** : Crée un index Lucene avec des documents prédéfinis.
- **Recherche.java** : Permet de rechercher des documents dans l'index à partir d'une entrée clavier.
- **AjoutLangue.java** et **RechercheLangue.java** : Variantes qui permettent de choisir l'analyseur selon la langue (option `--lang=fr`, `--lang=en` ou `--lang=default`).

Ce guide explique comment configurer l'environnement, compiler le projet avec **Maven**, et exécuter les programmes, que ce soit en ligne de commande ou avec un IDE. Il est destiné aux utilisateurs débutants, y compris ceux qui n'ont jamais utilisé Maven ou qui ne sont pas familiers avec les terminaux comme l'invite de commandes (Windows) ou PowerShell. Des instructions spécifiques pour macOS avec **Homebrew** sont incluses. Nous recommandons  d'utiliser **Java 21** pour bénéficier d'une version LTS (Long-Term Support) stable et performante.

## Prérequis

Avant de commencer, assurez-vous d'avoir les outils suivants installés sur votre ordinateur (Windows, Linux ou macOS) :

1. **Java Development Kit (JDK) 21** :
   - Lucene nécessite au moins Java 11, mais nous recommandons **Java 21**, car c'est une version supportée avec des améliorations récentes.
   - **Pour Windows** :
     - Téléchargez le JDK 21 depuis [Adoptium](https://adoptium.net/) ou [Oracle](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html).
     - **Installation** :
       1. Exécutez l'installateur téléchargé (par exemple, `jdk-21_windows-x64_bin.exe`).
       2. Suivez les instructions à l'écran. Notez le dossier d'installation (par exemple, `C:\Program Files\Eclipse Adoptium\jdk-21.0.4+7`).
     - **Configurer `JAVA_HOME`** :
       1. Appuyez sur `Windows + R`, tapez `sysdm.cpl`, et appuyez sur Entrée pour ouvrir "Propriétés système".
       2. Allez dans l'onglet **Avancé** et cliquez sur **Variables d'environnement**.
       3. Sous "Variables système", cliquez sur **Nouveau** et ajoutez :
          - Nom : `JAVA_HOME`
          - Valeur : `C:\Program Files\Eclipse Adoptium\jdk-21.0.4+7` (ajustez selon votre chemin).
       4. Trouvez la variable `Path` dans "Variables système", cliquez sur **Modifier**, et ajoutez `%JAVA_HOME%\bin`.
     - **Vérification** :
       - Ouvrez l'invite de commandes ou PowerShell :
         1. Appuyez sur `Windows + S`, tapez `cmd` (pour l'invite de commandes) ou `powershell`, et appuyez sur Entrée.
         2. Dans la fenêtre qui s'ouvre, tapez :
            ```cmd
            java -version
            ```
         3. Vous devriez voir une sortie comme `openjdk 21` (par exemple, `openjdk 21.0.4 2024-07-16`).
       - Si vous obtenez une erreur comme "java n'est pas reconnu", vérifiez que `JAVA_HOME` et `Path` sont correctement configurés, puis redémarrez l'invite de commandes.
   - **Pour macOS avec Homebrew** :
     - Installez Homebrew si ce n'est pas déjà fait :
       ```bash
       /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
       ```
     - Installez OpenJDK 21 :
       ```bash
       brew install openjdk@21
       ```
     - Configurez `JAVA_HOME` :
       - Homebrew installe généralement le JDK dans `/opt/homebrew/Cellar/openjdk@21/<version>` (macOS ARM) ou `/usr/local/Cellar/openjdk@21/<version>` (macOS Intel).
       - Tapez `brew info openjdk`. Vous devriez recevoir des instructions que vous pouvez suivre:
         ```bash
          sudo ln -sfn /opt/homebrew/opt/openjdk/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk.jdk
          ```
       - Ajoutez ceci à votre `~/.zshrc` ou `~/.bashrc` (par défault: .zshrc):
         ```bash
         export JAVA_HOME=$(/usr/libexec/java_home -v 21)
         export PATH=$JAVA_HOME/bin:$PATH
         ```
       - Appliquez les modifications :
         ```bash
         source ~/.zshrc
         ```
     - **Vérification** :
       ```bash
       java -version
       ```
       Vous devriez voir une sortie comme `openjdk 21` (par exemple, `openjdk 21.0.4 2024-07-16`).
   - **Pour Linux ou macOS sans Homebrew** :
     - Téléchargez le JDK 21 depuis [Adoptium](https://adoptium.net/) ou [Oracle](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html).
     - **Installation** :
       - Décompressez l'archive dans un dossier (par exemple, `/opt/jdk-21`).
     - **Configurer `JAVA_HOME`** :
       ```bash
       export JAVA_HOME=/opt/jdk-21
       export PATH=$JAVA_HOME/bin:$PATH
       ```
       Ajoutez ces lignes à `~/.bashrc` ou `~/.zshrc`.
     - **Vérification** :
       ```bash
       java -version
       ```
       Vous devriez voir une sortie indiquant Java 21 (par exemple, `openjdk 21.0.4 2024-07-16`).

2. **Apache Maven** :
   - Maven est un outil de gestion de projet qui télécharge les dépendances (comme Lucene) et compile le code.
   - **Pour Windows** :
     - Téléchargez Maven depuis [le site officiel](https://maven.apache.org/download.cgi) (par exemple, `apache-maven-3.9.9-bin.zip`).
     - **Installation** :
       1. Décompressez l'archive dans un dossier (par exemple, `C:\maven`).
       2. Configurez `M2_HOME` :
          - Ouvrez "Propriétés système" (`Windows + R`, tapez `sysdm.cpl`).
          - Dans "Variables d'environnement", sous "Variables système", ajoutez :
            - Nom : `M2_HOME`
            - Valeur : `C:\maven\apache-maven-3.9.9`
          - Modifiez `Path` et ajoutez `%M2_HOME%\bin`.
     - **Vérification** :
       - Ouvrez l'invite de commandes ou PowerShell :
         1. Appuyez sur `Windows + S`, tapez `cmd` ou `powershell`, et appuyez sur Entrée.
         2. Tapez :
            ```cmd
            mvn -v
            ```
         3. Vous devriez voir la version de Maven (par exemple, `Apache Maven 3.9.9`) et Java 21.
       - Si vous obtenez "mvn n'est pas reconnu", vérifiez `M2_HOME` et `Path`, puis redémarrez l'invite de commandes.
   - **Pour macOS avec Homebrew** :
     - Installez Maven :
       ```bash
       brew install maven
       ```
     - Homebrew configure automatiquement le `PATH`, mais vérifiez :
       ```bash
       mvn -v
       ```
       Vous devriez voir la version de Maven (par exemple, `Apache Maven 3.9.9`) et Java 21.
     - Si `mvn` n'est pas trouvé, ajoutez :
       ```bash
       export PATH=/opt/homebrew/bin:$PATH
       ```
       Ajoutez cette ligne à `~/.zshrc` et exécutez `source ~/.zshrc`.
   - **Pour Linux ou macOS sans Homebrew** :
     - Téléchargez Maven depuis [le site officiel](https://maven.apache.org/download.cgi).
     - **Installation** :
       - Décompressez l'archive (par exemple, `/opt/maven`).
       - Configurez `M2_HOME` :
         ```bash
         export M2_HOME=/opt/maven
         export PATH=$M2_HOME/bin:$PATH
         ```
         Ajoutez ces lignes à `~/.bashrc` ou `~/.zshrc`.
     - **Vérification** :
       ```bash
       mvn -v
       ```
       Vous devriez voir la version de Maven et Java 21.

3. **Un terminal** :
   - **Pour Windows** :
     - Vous utiliserez l'**invite de commandes** (cmd) ou **PowerShell**, deux outils intégrés à Windows pour exécuter des commandes.
     - **Comment ouvrir** :
       1. Appuyez sur `Windows + S`, tapez `cmd` (pour l'invite de commandes) ou `powershell`, et appuyez sur Entrée.
       2. Une fenêtre noire (cmd) ou bleue (PowerShell) s'ouvrira. C'est ici que vous taperez les commandes.
     - **Commandes de base** :
       - `dir` : Liste les fichiers et dossiers dans le répertoire actuel (comme `ls` sur Linux/macOS).
       - `cd chemin` : Navigue vers un dossier (par exemple, `cd C:\Users\VotreNom\lucene-search`).
       - `cd ..` : Remonte d'un dossier.
       - Exemple : Si votre projet est dans `C:\Projets\lucene-search`, tapez :
         ```cmd
         cd C:\Projets\lucene-search
         dir
         ```
         Vous devriez voir `pom.xml` et le dossier `src`.
   - **Pour Linux/macOS** :
     - Utilisez le terminal par défaut (par exemple, Bash ou Zsh).
     - Commandes similaires : `ls` pour lister, `cd chemin` pour naviguer.

## Structure du projet

Le projet a la structure suivante :

```
lucene-search/
├── pom.xml
├── src/
│   └── main/
│       └── java/
│           ├── Ajout.java
│           ├── Recherche.java
│           ├── AjoutLangue.java
│           └── RechercheLangue.java
...
```

- `pom.xml` : Fichier de configuration Maven qui définit les dépendances (Lucene 10.2.0) et les instructions de compilation.
- `Ajout.java` : Programme pour créer l'index dans `./index/`.
- `Recherche.java` : Programme pour rechercher dans l'index `./index/`.
- `AjoutLangue.java` : Programme pour créer un index avec un analyseur propre à une langue, dans `./index_fr/`, `./index_en/` ou `./index_default/` selon l'option `--lang`.
- `RechercheLangue.java` : Programme pour rechercher dans l'index de la langue choisie avec `--lang` (le même analyseur que celui qui a servi à construire l'index).

Si vous n'avez pas encore ces fichiers, assurez-vous qu'ils sont placés comme indiqué. Les fichiers `Recherche.java` et `Ajout.java` doivent être dans `src/main/java/`.

## Compilation en ligne de commande

Suivez ces étapes pour compiler le projet avec Maven :

1. **Ouvrez un terminal** :
   - **Windows** :
     - Appuyez sur `Windows + S`, tapez `cmd` ou `powershell`, et appuyez sur Entrée.
     - Naviguez vers le dossier du projet :
       1. Trouvez le chemin de votre projet (par exemple, `C:\Projets\lucene-search`).
       2. Tapez :
          ```cmd
          cd C:\Projets\lucene-search
          ```
       3. Vérifiez que vous êtes au bon endroit avec :
          ```cmd
          dir
          ```
          Vous devriez voir `pom.xml` et `src`.
   - **Linux/macOS** :
     - Ouvrez le terminal et naviguez :
       ```bash
       cd chemin/vers/lucene-search
       ```

2. **Compilez le projet** :
   - Exécutez la commande suivante pour nettoyer, télécharger les dépendances, et compiler le code :
     ```bash
     mvn clean package
     ```
   - **Explication** :
     - `clean` : Supprime les fichiers générés précédemment (dossier `target/`).
     - `package` : Compile le code, exécute les tests (s'il y en a), et crée un fichier JAR dans `target/`.
   - **Résultat** :
     - Si la compilation réussit, vous verrez `[INFO] BUILD SUCCESS`.
     - Un dossier `target/` sera créé, contenant :
       - `lucene-search-1.0-SNAPSHOT.jar` : Le fichier exécutable du projet.
       - `libs/` : Les dépendances (fichiers JAR de Lucene).
   - **Pour Windows** :
     - Si vous obtenez "mvn n'est pas reconnu", assurez-vous que Maven est installé et que `%M2_HOME%\bin` est dans `Path`.
     - Si la commande semble bloquer, vérifiez votre connexion Internet (Maven télécharge les dépendances).

3. **Vérifiez les erreurs** :
   - Si vous voyez `[ERROR]`, lisez le message. Les erreurs courantes incluent :
     - `JAVA_HOME` mal configuré : Vérifiez avec `java -version`.
     - Fichiers Java mal placés : Assurez-vous que `Recherche.java` et `Ajout.java` sont dans `src/main/java/`.
     - Problèmes de connexion : Maven a besoin d'Internet pour télécharger les dépendances.

## Exécution des programmes

Après avoir compilé le projet, vous pouvez exécuter `Ajout` pour créer l'index, puis `Recherche` pour effectuer des recherches.

### Étape 1 : Créer l'index avec `Ajout`

1. **Exécutez `Ajout`** :
   - **Windows** :
     - Ouvrez l'invite de commandes ou PowerShell (`Windows + S`, tapez `cmd` ou `powershell`).
     - Naviguez vers le dossier du projet :
       ```cmd
       cd C:\Projets\lucene-search
       ```
     - Exécutez :
       ```cmd
       java --enable-native-access=ALL-UNNAMED -cp "target/lucene-search-1.0-SNAPSHOT.jar;target/libs/*" Ajout
       ```
     - **Note** : Utilisez `;` pour séparer les chemins (spécifique à Windows).
   - **Linux/macOS** :
     - Dans le terminal, exécutez :
       ```bash
       java --enable-native-access=ALL-UNNAMED -cp "target/lucene-search-1.0-SNAPSHOT.jar:target/libs/*" Ajout
       ```
       - Utilisez `:` au lieu de `;`.
   - **Explication** :
     - `-cp` : Spécifie le classpath, incluant le JAR du projet et les dépendances dans `target/libs/`.
     - `Ajout` : La classe principale à exécuter.
   - **Résultat** :
     - Si tout fonctionne, vous verrez : `Index créé avec succès.`
     - Un dossier `./index/` sera créé dans le dossier racine, contenant les fichiers de l'index Lucene.
     - L'index est recréé à chaque exécution (mode `IndexWriterConfig.OpenMode.CREATE`) : relancer `Ajout` remplace l'index existant au lieu d'ajouter les documents une seconde fois.

2. **Vérifiez les erreurs** :
   - Si vous obtenez `Could not find or load main class Ajout`, vérifiez que `target/lucene-search-1.0-SNAPSHOT.jar` existe.
   - Si l'index ne se crée pas, assurez-vous d'avoir les permissions d'écriture dans le dossier du projet.

### Étape 2 : Rechercher avec `Recherche`

1. **Exécutez `Recherche`** :
   - **Windows** :
     - Dans l'invite de commandes ou PowerShell, dans le dossier du projet :
       ```cmd
       java --enable-native-access=ALL-UNNAMED -cp "target/lucene-search-1.0-SNAPSHOT.jar;target/libs/*" Recherche
       ```
   - **Linux/macOS** :
     - Dans le terminal :
       ```bash
       java --enable-native-access=ALL-UNNAMED -cp "target/lucene-search-1.0-SNAPSHOT.jar:target/libs/*" Recherche
       ```
   - **Explication** :
     - Similaire à `Ajout`, mais exécute la classe `Recherche`.
   - **Résultat** :
     - Le programme affiche : `Entrez le texte à chercher (ou Entrée pour quitter) :`
     - Tapez un texte (par exemple, `a b`) et appuyez sur Entrée pour voir les résultats.
     - Exemple de sortie :
       ```
       Résultats : 4 documents trouvés pour "a b"
       ID: Doc1 - Lucene ID: 0 - Titre: titre1 - Auteur: auteur1
       ID: Doc3 - Lucene ID: 2 - Titre: titre3 - Auteur: auteur3
       ID: Doc5 - Lucene ID: 4 - Titre: titre5 - Auteur: auteur5
       ID: Doc2 - Lucene ID: 1 - Titre: titre2 - Auteur: auteur2
       ```
     - Par défaut, les termes de la requête sont reliés par OU : `Doc2`, qui ne contient que `b`, est donc trouvé, mais avec un score plus faible. Pour exiger les deux termes, tapez `+a +b` : vous obtiendrez alors 3 documents (`Doc1`, `Doc3` et `Doc5`).
     - Appuyez sur Entrée sans texte pour quitter.

2. **Vérifiez les erreurs** :
   - Si vous obtenez `Directory does not exist`, assurez-vous d'avoir exécuté `Ajout` d'abord pour créer l'index.
   - Si aucune réponse n'apparaît, vérifiez que l'index dans `./index/` contient des données.

### Variantes selon la langue : `AjoutLangue` et `RechercheLangue`

Ces deux programmes s'exécutent comme `Ajout` et `Recherche`, avec l'option `--lang` (`fr`, `en` ou `default`) ; sans cette option, ils demandent la langue au clavier. Chaque langue a son propre index :

```bash
java --enable-native-access=ALL-UNNAMED -cp "target/lucene-search-1.0-SNAPSHOT.jar:target/libs/*" AjoutLangue --lang=fr       # crée ./index_fr/
java --enable-native-access=ALL-UNNAMED -cp "target/lucene-search-1.0-SNAPSHOT.jar:target/libs/*" RechercheLangue --lang=fr   # interroge ./index_fr/
```

Sous Windows, remplacez `:` par `;` dans le chemin de classes. Comme pour `Ajout`, l'index de la langue choisie est recréé à chaque exécution de `AjoutLangue`.

## Utilisation avec un IDE

Si vous préférez utiliser un environnement graphique, les IDE comme **IntelliJ IDEA** ou **Eclipse** simplifient la gestion du projet Maven. Voici un survol pour chaque :

### IntelliJ IDEA

1. **Installation** :
   - Téléchargez IntelliJ IDEA Community (gratuit) depuis [JetBrains](https://www.jetbrains.com/idea/download/).
   - Installez et lancez l'IDE.

2. **Ouvrir le projet** :
   - Cliquez sur `File > Open` et sélectionnez le dossier `lucene-search` (contenant `pom.xml`).
   - IntelliJ détecte automatiquement le projet Maven et télécharge les dépendances.

3. **Configurer Java 21** :
   - Allez dans `File > Project Structure > Project` et sélectionnez JDK 21 dans la liste des SDK.
   - Si JDK 21 n'apparaît pas, ajoutez-le via `Add SDK > Download JDK` et sélectionnez `Eclipse Temurin 21`.

4. **Compiler** :
   - Cliquez sur l'icône Maven (généralement à droite) et exécutez `clean` puis `package` dans l'onglet `Lifecycle`.
   - Alternativement, utilisez `Build > Build Project`.

5. **Exécuter** :
   - Faites un clic droit sur `Ajout.java` dans `src/main/java/` et sélectionnez `Run 'Ajout.main()'`.
   - Répétez pour `Recherche.java`.
   - Les résultats s'affichent dans la console de l'IDE.

### Eclipse

1. **Installation** :
   - Téléchargez Eclipse IDE for Java Developers depuis [Eclipse](https://www.eclipse.org/downloads/).
   - Installez et lancez l'IDE.

2. **Ouvrir le projet** :
   - Cliquez sur `File > Import > Maven > Existing Maven Projects`.
   - Sélectionnez le dossier `lucene-search` et importez.
   - Eclipse charge les dépendances Maven.

3. **Configurer Java 21** :
   - Allez dans `Preferences > Java > Installed JREs` et ajoutez JDK 21 si nécessaire.
   - Dans `Properties` du projet, sélectionnez Java 21 comme `Java Compiler` et `JRE System Library`.

4. **Compiler** :
   - Cliquez sur `Project > Build All` ou laissez Eclipse compiler automatiquement.

5. **Exécuter** :
   - Faites un clic droit sur `Ajout.java` dans le `Package Explorer`, puis `Run As > Java Application`.
   - Répétez pour `Recherche.java`.
   - Les résultats apparaissent dans la console.

**Avantages des IDE** :
- Gestion automatique des dépendances et du classpath.
- Débogage facile avec points d'arrêt.
- Interface graphique pour exécuter et tester.

**Note** : Assurez-vous que le JDK 21 est configuré dans l'IDE, conformément à la recommandation.

## Résolution des problèmes courants

- **Erreur `mvn not found`** :
  - Vérifiez que Maven est installé et que `M2_HOME` et `PATH` sont configurés.
  - Sur Windows, redémarrez l'invite de commandes après avoir modifié `Path`.
  - Sur macOS avec Homebrew, exécutez `brew install maven` à nouveau.
- **Erreur `java.lang.UnsupportedClassVersionError`** :
  - Vous utilisez une version de Java trop ancienne. Installez Java 21 et mettez à jour `JAVA_HOME`.
- **Erreur `Directory does not exist` dans `Recherche`** :
  - Exécutez `Ajout` d'abord pour créer l'index dans `./index/`.
- **Problèmes de dépendances Maven** :
  - Supprimez le dossier `~/.m2/repository/` et relancez `mvn clean package` pour retélécharger les dépendances.
- **Sortie vide dans `Recherche`** :
  - Vérifiez que l'index contient des données (exécutez `Ajout`).
- **Homebrew sur macOS** :
  - Si `brew` ne fonctionne pas, mettez à jour Homebrew :
    ```bash
    brew update
    ```

## Ressources supplémentaires

- [Documentation officielle de Lucene](https://lucene.apache.org/core/10_2_0/)
- [Guide Maven pour débutants](https://maven.apache.org/guides/getting-started/)
- [Tutoriels IntelliJ IDEA](https://www.jetbrains.com/idea/documentation/)
- [Tutoriels Eclipse](https://www.eclipse.org/documentation/)
- [Homebrew pour macOS](https://brew.sh/)
- [Nouveautés de Java 21](https://openjdk.org/projects/jdk/21/)


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * Classe pour créer un index Lucene avec des documents prédéfinis,
 * en utilisant un analyseur spécifique à la langue pour la troncature.
 * L'utilisateur peut spécifier la langue via un argument ou un menu interactif.
 */
public class AjoutLangue {
    // Constantes pour les noms des champs
    private static final String FIELD_ID = "id";
    private static final String FIELD_TITRE = "titre";
    private static final String FIELD_AUTEUR = "auteur";
    private static final String FIELD_CONTENU = "contenu";
    // Préfixe du dossier d'index : chaque langue a son propre index
    // (./index_fr/, ./index_en/ ou ./index_default/).
    private static final String INDEX_DIR_PREFIX = "./index_";

    // Contenus des documents à indexer
    private static final String[] DOC_CONTENTS = {
            "", // Index 0 inutilisé
            "a b",
            "b",
            "a b c",
            "d",
            "a b c d e"
    };

    public static void main(String[] args) {
        try {
            // Récupération de la langue
            // Le lecteur n'est pas fermé : cela fermerait aussi System.in.
            var reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            var language = getLanguage(args, reader);
            createIndex(language);
            System.out.println("Index créé avec succès dans " + INDEX_DIR_PREFIX + language + "/ (langue : " + language + ").");
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de l'index : " + e.getMessage());
        }
    }

    /**
     * Détermine la langue à utiliser, via les arguments ou un menu interactif.
     *
     * @param args Arguments de la ligne de commande
     * @param reader Lecteur de l'entrée standard. Il ne faut pas le fermer ici :
     *               fermer le lecteur fermerait aussi System.in.
     * @return "fr" pour français, "en" pour anglais, ou "default" pour l'analyseur standard
     * @throws Exception En cas d'erreur de lecture de l'entrée
     */
    private static String getLanguage(String[] args, BufferedReader reader) throws Exception {
        // Vérifie si un argument --lang est fourni
        for (var arg : args) {
            if (arg.startsWith("--lang=")) {
                var lang = arg.substring("--lang=".length()).toLowerCase();
                if (lang.equals("fr") || lang.equals("en") || lang.equals("default")) {
                    return lang;
                }
            }
        }

        // Aucun argument --lang : afficher le menu interactif
        System.out.println("Aucune langue spécifiée. Choisissez une langue pour l'analyseur :");
        System.out.println("  - fr : Français (FrenchAnalyzer)");
        System.out.println("  - en : Anglais (EnglishAnalyzer)");
        System.out.println("  - default : Standard (StandardAnalyzer)");
        System.out.print("Entrez votre choix (fr/en/default) : ");
        var line = reader.readLine();
        var lang = (line == null) ? "" : line.trim().toLowerCase();

        if (lang.equals("fr") || lang.equals("en") || lang.equals("default")) {
            return lang;
        } else {
            throw new IllegalArgumentException("Choix de langue invalide : " + lang);
        }
    }

    /**
     * Crée un analyseur en fonction de la langue spécifiée.
     *
     * @param language La langue ("fr", "en", ou "default")
     * @return Un analyseur adapté (FrenchAnalyzer, EnglishAnalyzer, ou StandardAnalyzer)
     */
    private static Analyzer getAnalyzer(String language) {
        return switch (language) {
            case "fr" -> new FrenchAnalyzer();
            case "en" -> new EnglishAnalyzer();
            default -> new StandardAnalyzer();
        };
    }

    /**
     * Crée un index Lucene en ajoutant les documents définis.
     *
     * @param language La langue pour l'analyseur
     * @throws Exception En cas d'erreur lors de l'indexation
     */
    private static void createIndex(String language) throws Exception {
        var analyzer = getAnalyzer(language);
        var config = new IndexWriterConfig(analyzer);
        // CREATE : l'index est recréé à chaque exécution (sinon, le mode par défaut
        // CREATE_OR_APPEND ajouterait les documents une seconde fois).
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        try (var writer = new IndexWriter(
                FSDirectory.open(Paths.get(INDEX_DIR_PREFIX + language + "/")), config)) {
            for (var i = 1; i < DOC_CONTENTS.length; i++) {
                var doc = new Document();
                doc.add(new StringField(FIELD_ID, "Doc" + i, Field.Store.YES));
                doc.add(new StringField(FIELD_TITRE, "titre" + i, Field.Store.YES));
                doc.add(new StringField(FIELD_AUTEUR, "auteur" + i, Field.Store.YES));
                doc.add(new TextField(FIELD_CONTENU, DOC_CONTENTS[i], Field.Store.NO));
                writer.addDocument(doc);
            }
        }
    }
}
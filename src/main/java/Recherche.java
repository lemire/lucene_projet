import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * Classe permettant de rechercher des documents dans un index Lucene
 * à partir d'une entrée clavier.
 */
public class Recherche {
    // Constantes pour les noms des champs
    private static final String FIELD_ID = "id";
    private static final String FIELD_TITRE = "titre";
    private static final String FIELD_AUTEUR = "auteur";
    private static final String FIELD_CONTENU = "contenu";
    private static final String INDEX_DIR = "./index/";
    private static final String[] SEARCH_FIELDS = {FIELD_TITRE, FIELD_AUTEUR, FIELD_CONTENU};

    public static void main(String[] args) {
        try (var reader = new BufferedReader(
                new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            // Configuration de l'analyseur et du parser
            var analyzer = new StandardAnalyzer();
            var parser = new MultiFieldQueryParser(SEARCH_FIELDS, analyzer);

            // Boucle principale pour les recherches
            while (true) {
                System.out.print("Entrez le texte à chercher (ou Entrée pour quitter) : ");
                var queryText = reader.readLine();

                // Sortie si l'entrée est vide
                if (queryText == null || queryText.trim().isEmpty()) {
                    break;
                }

                // Exécution de la recherche
                searchDocuments(parser, queryText);
            }
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }

    /**
     * Effectue une recherche dans l'index Lucene et affiche les résultats.
     *
     * @param parser    Le parser pour construire la requête
     * @param queryText Le texte de la recherche
     * @throws Exception En cas d'erreur lors de la recherche
     */
    private static void searchDocuments(MultiFieldQueryParser parser, String queryText) throws Exception {
        try (var indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(INDEX_DIR)))) {
            var searcher = new IndexSearcher(indexReader);
            var query = parser.parse(queryText);
            var results = searcher.search(query, 4096);
            var hits = results.scoreDocs;

            // Affichage des résultats
            System.out.println("Résultats : %d documents trouvés pour \"%s\"".formatted(hits.length, queryText));
            java.util.Arrays.stream(hits).forEach(hit -> {
                try {
                    var doc = searcher.storedFields().document(hit.doc);
                    var id = doc.get(FIELD_ID);
                    var titre = doc.get(FIELD_TITRE);
                    var auteur = doc.get(FIELD_AUTEUR);
                    System.out.println("ID: %s - Lucene ID: %d - Titre: %s - Auteur: %s"
                            .formatted(id, hit.doc, titre, auteur));
                } catch (Exception e) {
                    System.err.println("Erreur lors de l'affichage d'un document : " + e.getMessage());
                }
            });
        }
    }
}
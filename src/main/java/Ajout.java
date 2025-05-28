import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

/**
 * Classe pour créer un index Lucene avec des documents prédéfinis.
 */
public class Ajout {
    // Constantes pour les noms des champs
    private static final String FIELD_ID = "id";
    private static final String FIELD_TITRE = "titre";
    private static final String FIELD_AUTEUR = "auteur";
    private static final String FIELD_CONTENU = "contenu";
    private static final String INDEX_DIR = "./index/";

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
            createIndex();
            System.out.println("Index créé avec succès.");
        } catch (Exception e) {
            System.err.println("Erreur lors de la création de l'index : " + e.getMessage());
        }
    }

    /**
     * Crée un index Lucene en ajoutant les documents définis.
     *
     * @throws Exception En cas d'erreur lors de l'indexation
     */
    private static void createIndex() throws Exception {
        var analyzer = new StandardAnalyzer();
        var config = new IndexWriterConfig(analyzer);

        try (var writer = new IndexWriter(
                FSDirectory.open(Paths.get(INDEX_DIR)), config)) {
            // Ajout des documents
            for (int i = 1; i < DOC_CONTENTS.length; i++) {
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
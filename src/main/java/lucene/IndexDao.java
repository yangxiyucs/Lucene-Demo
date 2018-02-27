package lucene;

import models.CollectionDocModel;
import models.RelevanceModel;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class IndexDao {
    public ArrayList<CollectionDocModel> docs; // the collection's
    // documents
    public ArrayList<CollectionDocModel> queries; // the collection's
    // queries
    public ArrayList<RelevanceModel> relevs; // the relevance of the//
    // queries to // the
    // documents
    public ArrayList<RelevanceModel> queryHits; // the hits of the
    int resultLimit = 50; // the number of maximum results to be returned //
    // per query
    int docId; // the id of the document
    Document d;

    // searches
    public void query() {
        try {
            Parser parser = new Parser();

            docs = parser.readDocs();
            queries = parser.readQueries();
            relevs = parser.readRelevs();
            queryHits = new ArrayList<>();

            Analyzer analyzer = new MyAnalyzer();
            /* index */
//Directory index = new RAMDirectory();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            Directory dir = FSDirectory.open(Paths.get("data/index"));
            IndexWriter w;
            w = new IndexWriter(dir, config);
            w.deleteAll();
            for (CollectionDocModel doc : docs) {

                addDoc(w, doc);

            }
            w.close();
            // initialize loop variables
            Query q;
            String queryString;
            MultiFieldQueryParser queryParser;
            IndexReader reader;
            IndexSearcher searcher;
            TopDocs topDocs;
            ScoreDoc[] hits;

            System.out.println("Hits limit: " + resultLimit);
            int i;

            for (i = 0; i < relevs.size(); i++) { // loop

                /* query */

                queryString = queries.get(i).getText();

                // if (option == 1)
                // q = new QueryParser("title", analyzer).parse(queryString);

                queryParser = new MultiFieldQueryParser(new String[]{"title", "author", "source", "text"}, analyzer);

                q = queryParser.parse(queryString);

                /* search */
                reader = DirectoryReader.open(dir);
                searcher = new IndexSearcher(reader);
                searcher.setSimilarity(new BM25Similarity());
                //searcher.setSimilarity(new ClassicSimilarity());
                //return the topDocs
                topDocs = searcher.search(q, resultLimit);
                //the top 50 results
                hits = topDocs.scoreDocs;
                /* save results */
                queryHits.add(new RelevanceModel());
                queryHits.get(queryHits.size() - 1).setQueryID(i + 1);
                for (int j = 0; j < hits.length; j++) { // for// every// hit

                    docId = hits[j].doc; //the ID in index of returned Document
                    d = searcher.doc(docId);// the Document returned based on the id in index
                    queryHits.get(queryHits.size() - 1).getDocsIDs().add(Integer.parseInt(d.get("id"))); // add
                    // the
                    // hit
                    // to
                    // the
                    // current
                    // query
                    // }catch (IOException | ParseException e) {
                    // e.printStackTrace();

                }
                System.out.println(docs.size() + "," + queries.size() + "," + relevs.size() + "," + queryHits.size());
                new calculation(docs, queries, relevs, queryHits, i).calculate();


            }
            new calculation(docs, queries, relevs, queryHits, i).printTotal();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // index all the documents using IndexWriter
    public void addDoc(IndexWriter w, CollectionDocModel collectionDoc) throws IOException {

        Document doc = new Document();
        /* add title field */
        TextField titleField = new TextField("title", collectionDoc.getTitle(), Field.Store.YES);

        /* set title field boosting */

        doc.add(titleField);

        /* add the rest fields */

        TextField authorField = new TextField("author", collectionDoc.getAuthor(), Field.Store.YES);
        TextField sourceField = new TextField("source", collectionDoc.getSource(), Field.Store.YES);
        TextField textField = new TextField("text", collectionDoc.getText(), Field.Store.YES);
        doc.add(authorField);
        doc.add(sourceField);
        doc.add(textField);
        doc.add(new StringField("id", collectionDoc.getId() + "", Field.Store.YES)); // add
        // id
        // field

        w.addDocument(doc);
    }
}


import java.io.IOException;
import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;


import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

public class SearchIndexedDocuments {
	private IndexSearcher searcher = null;
    private IndexReader reader = null;

    /** Creates a new instance of SearchEngine */
    public SearchIndexedDocuments() throws IOException {
    	Path path = Paths.get("index-directory");
        reader = DirectoryReader.open(FSDirectory.open(path));
        searcher = new IndexSearcher(reader);
    }

    public void performSearch(String queryString, String fieldName)
    throws IOException, ParseException {
        
    	Query query;
    	if(fieldName.equals("iri"))
    	{
    		TermQuery tq= new TermQuery(new Term(fieldName, queryString));                
    		query = tq;
    	}
    	else
    	{
    		query = new QueryParser(fieldName, new StandardAnalyzer()).parse(queryString);
    	}
    	//String[] filesToSearch = { "iri", "label"};
    	//MultiFieldQueryParser mqp = new MultiFieldQueryParser(filesToSearch, new StandardAnalyzer());
    	//Query query = mqp.parse(queryString);
    	
    	TopDocs hits = searcher.search(query, reader.maxDoc());
        System.out.println("Results found >> " + hits.totalHits);

        int i = 0;
        FileWriter myWriter = new FileWriter("labels.txt");
        for(ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
        	//System.out.println("==========" + (i + 1) + " : Start Record=========\nIRI :: " + doc.get("iri") + "\nlabel :: " + doc.get("label") + "\nHash :: " + doc.get("npHash") + "\n==========End Record=========\n");
        	//System.out.println(doc.get("label"));
            
            myWriter.write(doc.get("label") + "\n");
            i++;
        }
        myWriter.close();
    }

	public long getTotalRecordsBySearchTerm(String queryString, String fieldName)
    	    throws IOException, ParseException {
    	        
    	Query query;
    	
    	query = new QueryParser(fieldName, new StandardAnalyzer()).parse(queryString);
    	
    	TopDocs hits = searcher.search(query, reader.maxDoc());
        //System.out.println("Results found >> " + hits.totalHits);

    	return hits.totalHits.value;
    }
}

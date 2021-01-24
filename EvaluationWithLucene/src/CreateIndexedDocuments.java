
import java.io.IOException;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.*;


public class CreateIndexedDocuments {

	private IndexWriter indexWriter = null;
	private Connection con;
	
	public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
        	Path path = Paths.get("index-directory");
            Directory indexDir = FSDirectory.open(path);
            IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        }
        return indexWriter;
   }
	
	public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
        	indexWriter.commit();
            indexWriter.close();
        }
   }
	public void openConnection(){
        try{
        	/** JDBC Section */
        	Class.forName("com.mysql.jdbc.Driver").newInstance();
        	if(con == null)
        	{
        		String OnlineServer = "mysql-server-1/ia48";
                String userName = "ia48";
                String pwd = "7INB446Kle";
                con = DriverManager.getConnection("jdbc:mysql://"+OnlineServer,userName,pwd);
        	}
        	else if(con.isClosed())
            {
                String OnlineServer = "mysql-server-1/ia48";
                String userName = "ia48";
                String pwd = "7INB446Kle";
                con = DriverManager.getConnection("jdbc:mysql://"+OnlineServer,userName,pwd);
            }
            else
            {
                String OnlineServer = "mysql-server-1/ia48";
                String userName = "ia48";
                String pwd = "7INB446Kle";
                con = DriverManager.getConnection("jdbc:mysql://"+OnlineServer,userName,pwd);
            }
        }
        catch(Exception ex)
        {
        	System.out.println("Connection Opened: " + ex.getMessage());
        }
    }
    
    public void closeConnection(){
        try{
            con.close();
        }
        catch(Exception ex)
        {
        	System.out.println("Connection Closed: " + ex.getMessage());
        }
    }
	
	public void indexNanopubs() throws IOException {

		openConnection();
		String query = "";
    	query = "SELECT IRI, rdfsLabel, npHash FROM new_topic where rdfsLabel is not null";	
         
         //java.sql.Statement stmt;
         //java.sql.CallableStatement stmt;
         ResultSet rs = null;
	     try {
	    	 System.out.println("Fetching data from mysql.");
	    	 java.sql.Statement stmt = con.createStatement();
	    	 rs = stmt.executeQuery(query);
	    	 rs.last();
	    	 int rows = rs.getRow(); 
	    	 rs.beforeFirst();
	    	 System.out.println(rows + " rows Fetched from mysql.");
	    	 FieldType stringType = new FieldType();
	    	 stringType.setTokenized(false);
	    	 //stringType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
	         if(rows > 0)
	         {
	        	 System.out.println("Indexing Lables, IRIs and nphash.");
	        	 while (rs.next())
	        	 {
	        		 IndexWriter writer = getIndexWriter(false);
	        	     Document doc = new Document();
	        	     doc.add(new StringField("iri", rs.getString("IRI"), Field.Store.YES));
	        	     doc.add(new TextField("label", rs.getString("rdfsLabel"), Field.Store.YES));
	        	     doc.add(new StringField("npHash", rs.getString("npHash"), Field.Store.YES));
	        	     writer.addDocument(doc);
	        	 }
	        	 
	        	 closeIndexWriter();
	        	 System.out.println("Done...");
	         }
	     } catch (SQLException ex) {
	    	 System.out.println("Fetch Data: " + ex.getMessage());
	     }
    }

}

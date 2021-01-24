import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class NanopubRetrievalFramework {
	private Connection con;
	
	public NanopubRetrievalFramework()
	{
		try{
        	/** JDBC Section */
        	Class.forName("com.mysql.jdbc.Driver").newInstance();
        	
            String OnlineServer = "mysql-server-1/ia48";
            String userName = "ia48";
            String pwd = "7INB446Kle";
            con = DriverManager.getConnection("jdbc:mysql://"+OnlineServer,userName,pwd);   
		}
        catch(Exception ex)
        {
        	System.out.println("Connection Opened: " + ex.getMessage());
        }
	}
	
	public void openConnection(){
        try{
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

    
    public ArrayList<String> getAllRdfsLabels() throws IOException{
		openConnection();
		String query = "";
    	query = "SELECT DISTINCT rdfsLabel FROM new_topic_label_cluster WHERE nano_count > 10";	
         ArrayList<String> rdfsLabels = new ArrayList<>();
         ResultSet rs = null;
	     try {
	    	 System.out.println("Fetching rdfslabels from mysql.");
	    	 java.sql.Statement stmt = con.createStatement();
	    	 rs = stmt.executeQuery(query);
	    	 rs.last();
	    	 int rows = rs.getRow(); 
	    	 rs.beforeFirst();
	    	 System.out.println(rows + " rows Fetched from mysql.");
	    	 //FileWriter labelWriter = new FileWriter("rdfsLabel.txt");
	    	 
	    	 if(rows > 0)
	         {
	        	 while (rs.next())
	        	 {
	        		 rdfsLabels.add(rs.getString("rdfsLabel"));
	        		 //labelWriter.write(rs.getString("rdfsLabel") + "\n");
	        	 }
	        	 //labelWriter.flush();
	        	 //labelWriter.close();
	         }
	     } catch (SQLException ex) {
	    	 System.out.println("Fetch Data: " + ex.getMessage());
	     }
	     
	     return rdfsLabels;
    }
    
    public int getTotalRecordsByExactSearchTerm(String term) throws SQLException{
    	//if(con.isClosed())
    	openConnection();
    	
		String query = "";
    	query = "SELECT rdfsLabel FROM new_topic where rdfsLabel = ?";	
         int rows = 0;
         ResultSet rs = null;
	     try {
	    	 PreparedStatement stmt = con.prepareStatement(query);
	    	 stmt.setString(1, term);
	    	 rs = stmt.executeQuery();
	    	 rs.last();
	    	 rows = rs.getRow();
	     } catch (SQLException ex) {
	    	 System.out.println("Label: " + term + " Error:" + ex.getMessage());
	     }
	     
	     return rows;
    }
    
    public int getTotalRecordsBySearchTerm(String term) throws SQLException{
    	//if(con.isClosed())
    	openConnection();
    	
		String query = "";
		if(term.split(" ").length > 1)
		{
			term += "%";
			query = "SELECT rdfsLabel FROM new_topic where rdfsLabel like ?";		
		}
		else
		{
			term += "'"+term+"'";
			query = "SELECT rdfsLabel FROM new_topic where MATCH(rdfsLabel) \n" + 
						" AGAINST(? IN NATURAL LANGUAGE MODE)";	
		}
    	 int rows = 0;
         ResultSet rs = null;
	     try {
	    	 PreparedStatement stmt = con.prepareStatement(query);
	    	 stmt.setString(1, term);
	    	 rs = stmt.executeQuery();
	    	 rs.last();
	    	 rows = rs.getRow();
	     } catch (SQLException ex) {
	    	 System.out.println("Label: " + term + " Error:" + ex.getMessage());
	     }
	     
	     return rows;
    }

	public ArrayList<String> getRdfsLabelFromFile() {
		ArrayList<String> rdfsLabels = new ArrayList<>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("rdfsLabel.txt"));
			String line = reader.readLine();
			while (line != null) {
				if(!line.equals(""))
					rdfsLabels.add(line);
				
				//System.out.println(line);
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rdfsLabels;
	}
}

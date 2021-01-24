import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.apache.lucene.queryparser.classic.ParseException;

public class GenerateComperisonFile {

	static ArrayList<Integer> uniqueRandomList = new ArrayList<Integer>();
	
	public static void main(String[] args) throws IOException, ParseException, SQLException {
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
		FileWriter csvWriter = new FileWriter("comperisonFile_"+dateFormat.format(date)+".csv");
		csvWriter.write("Sr.,term,relevant,retrieved_our,retrieved_lucene, time_our, time_lucene");
		SearchIndexedDocuments lucene = new SearchIndexedDocuments();
		NanopubRetrievalFramework ourFramework = new NanopubRetrievalFramework();
		
		//ourFramework.getAllRdfsLabelsWriteIntoFile();
		
		ArrayList<String> rdfsLabel = ourFramework.getAllRdfsLabels();
		
		long size = 1000;//rdfsLabel.size();
		long startTime;// = System.currentTimeMillis();
		long endTime;// = System.currentTimeMillis();
		long time_our;// = endTime - startTime;
		long time_lucene;
		
		//int showMessageCount = 10000;
		System.out.println("Writing on csv file...");
		int i=0;
		while (i < size) 
		{ 
			int randomIndex = getRandomNumberInRange(0, rdfsLabel.size());
			String term = rdfsLabel.get(randomIndex);
		    int relevantCount = ourFramework.getTotalRecordsByExactSearchTerm(term);
		    
		    ////////////////////////////////////////////////////
		    startTime = System.currentTimeMillis();
		    int ourRetrivedCount = ourFramework.getTotalRecordsBySearchTerm(term);
		    endTime = System.currentTimeMillis();
		    time_our = endTime - startTime;
		    //////////////////////////////////////////////////////
		    startTime = System.currentTimeMillis();
		    long luceneRetrivedCount = lucene.getTotalRecordsBySearchTerm("label:\""+term+"*\"", "label");
		    endTime = System.currentTimeMillis();
		    time_lucene = endTime - startTime;
		    //////////////////////////////////////////////////////
		    // Write to the file
		    //System.out.println((i+1) + "," + term + "," + relevantCount + "," + ourRetrivedCount + "," + luceneRetrivedCount + "," + time_our + "," + time_lucene);
		    
		    if(luceneRetrivedCount > ourRetrivedCount)
		    {
		    	csvWriter.write("\n"+(i+1) + ",\"" + term + "\"," + relevantCount + "," + ourRetrivedCount + "," + luceneRetrivedCount + "," + time_our + "," + time_lucene);
		    	i++;
		    }
		
		    //if(i % showMessageCount == 0)
		    	//System.out.println(i + " records have been processed");
		}
		
		csvWriter.flush();
		csvWriter.close();
		System.out.println("Done...");
	}
	
	private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        int randNumber = r.nextInt((max - min) + 1) + min;
        while(uniqueRandomList.contains(randNumber))
        {
        	randNumber = r.nextInt((max - min) + 1) + min;
        }
        
        uniqueRandomList.add(randNumber);
        
        return randNumber; 
    }
}

import java.io.IOException;
import java.util.Scanner;

import org.apache.lucene.queryparser.classic.ParseException;

public class LuceneMain {

	public static void main(String[] args) throws IOException, ParseException {
		//CreateIndexedDocuments indexer = new CreateIndexedDocuments();
		//indexer.indexNanopubs();
		
		Search();
	}

	private static void Search() throws IOException, ParseException {
		Scanner in = new Scanner(System.in);
		String choice = "1";
		String query = "";
		SearchIndexedDocuments searcher = new SearchIndexedDocuments();
		
		while(!choice.equals("3"))
		{
			query = "";
			System.out.println("Search in Indexed Lucene");
			System.out.println("--------------------------");
			System.out.println("1. Search by IRI");
			System.out.println("2. Search by Label");
			System.out.println("3. Exit");
			System.out.println("--------------------------");
			System.out.print("Select option: ");
			choice = in.nextLine().trim();
			//Serotonin
			switch(choice)
			{
				case "1":
					System.out.print("Enter IRI: ");
					query = in.nextLine().trim();
					if(!query.equals("")) {
						searcher.performSearch(query, "iri");
					}
					break;
				case "2":
					System.out.print("Enter rdfsLabel: ");
					query = in.nextLine().trim();
					if(!query.equals("")) {
						query += "*";
						
						System.out.println("The query is: label:\""+query+"\"");
						searcher.performSearch("label:\""+query+"\"", "label");
						
						//System.out.println("The query is: "+query);
						//searcher.performSearch(query, "label");
					}
					break;
				default:
					System.out.println("Thanks. Bye!");
			}
		}
	}
}

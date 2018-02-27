package lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import models.CollectionDocModel;
import models.RelevanceModel;

/**
 * This is the project's parser.
 * It is used to parse the documents of the Cranfield
 * collection, the testing queries and the given relevancies.
 * 
 * @author Steve Galgouranas
 */
public class Parser {

	/**
	 * Parses the documents of the Cranfield test collection.
	 * The file data must have the following structure:
	 * 	.I X
	 *  .T
	 *  <TITLE TEXT>
	 *  .A
	 *  <AUTHOR TEXT> (NOT MANDATORY)
	 *  .B
	 *  <AFFILIATION TEXT> (NOT MANDATORY)
	 *  .W
	 *  <ABSTRACT TEXT>
	 *  ... (loop)
	 *  
	 * @return -> the ArrayList containing the documents.
	 */
	public ArrayList<CollectionDocModel> readDocs() {
		ArrayList<CollectionDocModel> docs = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(new File("data/collection/cran.all.1400")))) {
			String line;
			int counter = 1; //used to determine on which field the parser is at
			int flag = 0; //used to determine whether a '\n' should be appended
			String newLine; //this string takes the value "" or "\n"
			
			docs.add(new CollectionDocModel());
			docs.get(0).setId(1);
			while ((line = br.readLine()) != null) {
				if (line.charAt(0) == '.') { //if the line starts with a dot (it's a category)
					flag++;
					switch (counter) {
					case 0: //enters on the first field category (.I X)
						if (docs.get(docs.size() - 1).getText().length() > 0) {
							docs.add(new CollectionDocModel());
							docs.get(docs.size() - 1).setId(Integer.parseInt(line.substring(3)));
							counter = 1;
							flag = 0;
						}
						break;
					case 1: //enters on the second field category (.T)
						if (docs.get(docs.size() - 1).getTitle().length() > 0)
							counter++;
						break;
					case 2: //enters on the third field category (.A)
						if (flag > 1)
							counter++;
						if (docs.get(docs.size() - 1).getAuthor().length() > 0)
							counter++;
						break;
					case 3: //enters on the fourth field category (.B)
						if (docs.get(docs.size() - 1).getSource().length() > 0 || flag > 1)
							counter = 0;
						break;
					}
				} else { //line should be appended to the current category's text
					newLine = flag > 0 ? "" : "\n"; //check if "\n" should be appended
					
					switch (counter) {
					case 0:
						docs.get(docs.size() - 1).setText(docs.get(docs.size() - 1).getText() + newLine + line);
						break;
					case 1:
						docs.get(docs.size() - 1).setTitle(docs.get(docs.size() - 1).getTitle() + newLine + line);
						break;
					case 2:
						docs.get(docs.size() - 1).setAuthor(docs.get(docs.size() - 1).getAuthor() + newLine + line);
						break;
					case 3:
						docs.get(docs.size() - 1).setSource(docs.get(docs.size() - 1).getSource() + newLine + line);
						break;
					}
					flag = 0;
				}
			}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/* remove the title from the beginning of the document's text */
		//for(CollectionDocModel doc: docs) doc.setText(doc.getText().substring(doc.getTitle().length()+1, doc.getText().length()));
		return docs;
	}

	/**
	 * Parses the queries of the Cranfield test collection.
	 * The file data must have the following structure:
	 * 	.I X
	 *  .W
	 *  <QUERY TEXT>
	 *  ... (loop)
	 *  
	 * @return -> the ArrayList containing the queries.
	 */
	public ArrayList<CollectionDocModel> readQueries() {
		ArrayList<CollectionDocModel> queries = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(new File("data/collection/cran.qry")))) {
			String line;
			int flag = 0; //used to determine whether a '\n' should be appended
			String newLine; //this string takes the value "" or "\n"
			
			queries.add(new CollectionDocModel());
			queries.get(0).setId(1);
			while ((line = br.readLine()) != null) {
				if (line.charAt(0) == '.') { //if the line starts with a dot (it's a category)
					flag++;
					
					if (queries.get(queries.size() - 1).getText().length() > 0) { //this if is used to skip the .W line
						queries.add(new CollectionDocModel());
						queries.get(queries.size() - 1).setId(Integer.parseInt(line.substring(3)));
						flag = 0;
					}
				} else { //line should be appended to the query text
					newLine = flag > 0 ? "" : "\n"; //check if "\n" should be appended
					
					queries.get(queries.size() - 1).setText(queries.get(queries.size() - 1).getText()
							+ newLine + line); //append the line
					flag = 0;
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return queries;
	}

	/**
	 * Parses the relevancies of the Cranfield test collection.
	 * @return -> the ArrayList containing the relevancies.
	 */
	public ArrayList<RelevanceModel> readRelevs() {
		ArrayList<RelevanceModel> relevs = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(new File("data/collection/cranqrel")))) {
			String line;
			String[] tempTable;
			int flag = 0; //used to determine when a new query is examined
			
			while ((line = br.readLine()) != null) {
				tempTable = line.split(" "); //split line at whitespace
				
				if (flag != Integer.parseInt(tempTable[0])) { //if new query
					relevs.add(new RelevanceModel()); //add query
					relevs.get(relevs.size() - 1).setQueryID(Integer.parseInt(tempTable[0])); //add query id
				}
				flag = Integer.parseInt(tempTable[0]); //set the flag equal to the query number
				relevs.get(relevs.size() - 1).getDocsIDs().add(Integer.parseInt(tempTable[1])); //set the relevant document id
				relevs.get(relevs.size() - 1).getRelevance().add(Integer.parseInt(tempTable[2])); //set the scale of the relevance
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return relevs;
	}
}

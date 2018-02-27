package models;

import java.util.ArrayList;

/**
 * The model of the relevance of the Cranfield documents.
 * 
 * @author Steve Galgouranas
 */
public class RelevanceModel {

	private int queryID; //the ID of the query
	private ArrayList<Integer> docsIDs; // the IDs of the relevant documents
	private ArrayList<Integer> queryRelevanceToDocs; //the scale of relevancy to every relevant document
	
	public RelevanceModel() {
		docsIDs = new ArrayList<Integer>();
		queryRelevanceToDocs = new ArrayList<Integer>();
	}

	public int getQueryID() {
		return queryID;
	}

	public void setQueryID(int queryID) {
		this.queryID = queryID;
	}

	public ArrayList<Integer> getDocsIDs() {
		return docsIDs;
	}

	public void setDocsIDs(ArrayList<Integer> docID) {
		this.docsIDs = docID;
	}

	public ArrayList<Integer> getRelevance() {
		return queryRelevanceToDocs;
	}

	public void setRelevance(ArrayList<Integer> relevance) {
		this.queryRelevanceToDocs = relevance;
	}	
	
}

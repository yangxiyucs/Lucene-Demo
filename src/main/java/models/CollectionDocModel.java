package models;

/**
 * The model of documents of the given Cranfield collection.
 * 
 * @author Steve Galgouranas
 */
public class CollectionDocModel {
	
	private int id; 		//.I
	private String title;   //.T
	private String author;	//.A
	private String source;	//.B
	private String text;	//.W
	
	public CollectionDocModel() {
		id = 0;
		title = "";
		author = "";
		source = "";
		text = "";
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}

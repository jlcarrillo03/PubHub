package examples.pubhub.model;

public class BookTag {

	private String isbn13;
	private String tag;
	
	public BookTag() {
		this.isbn13 = null;
		this.tag = null;
	}
	
	public BookTag(String isbn13, String tag) {
		this.isbn13 = isbn13;
		this.tag = tag;
	}
	
	public String getIsbn13() {
		return isbn13;
	}
	
	public String getTag() {
		return tag;
	}
	
	public void setIsbn13(String isbn13) {
		this.isbn13 = isbn13;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}
}

package examples.pubhub.dao;

import java.util.List;

import examples.pubhub.model.Book;
import examples.pubhub.model.BookTag;

public interface BookTagDAO {
	
	public boolean addTag(String tag, BookTag bookTag);
	public boolean removeTag(String isbn, String tag);
	public List<BookTag> getAllTagsOfBook(String isbn);
	public List<Book> getAllBooksWithTag(String tag);
		
	
}

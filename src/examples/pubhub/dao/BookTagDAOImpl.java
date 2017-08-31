package examples.pubhub.dao;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import examples.pubhub.model.Book;
import examples.pubhub.model.BookTag;
import examples.pubhub.utilities.DAOUtilities;

public class BookTagDAOImpl implements BookTagDAO{

	Connection connection = null;	// Our connection to the database
	PreparedStatement stmt = null;	// We use prepared statements to help protect against SQL injection
	
	/*------------------------------------------------------------------------------------------------*/
	
	@Override
	public boolean addTag(String tag, BookTag bookTag) {
		try {
			connection = DAOUtilities.getConnection();
			String sql = "INSERT INTO book_tags(isbn_13, tag_name)"
					+ "SELECT isbn_13, ? FROM books WHERE isbn_13= ?";
			stmt = connection.prepareStatement(sql);
			
			//Sending them
			stmt.setString(1, tag);
			stmt.setString(2, bookTag.getIsbn13());
			
			// If we were able to add our book to the DB, we want to return true. 
			// This if statement both executes our query, and looks at the return 
			// value to determine how many rows were changed
			if (stmt.executeUpdate() != 0)
				return true;
			else
				return false;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeResources();
		}
		
	}

	@Override
	public boolean removeTag(String isbn, String tag ) {
		
		try {
			connection = DAOUtilities.getConnection();
			String sql = "DELETE FROM book_tags WHERE isbn_13= ? AND tag_name= ?";
			stmt = connection.prepareStatement(sql);
			
			stmt.setString(1, isbn);
			stmt.setString(2, tag);
			
			if (stmt.executeUpdate() != 0)
				return true;
			else
				return false;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			closeResources();
		}
		
	}

	@Override
	public List<BookTag> getAllTagsOfBook(String isbn) {
		List<BookTag> tags = new ArrayList<>();
		
		try {
			connection = DAOUtilities.getConnection();
			String sql = "SELECT tag_name FROM book_tags WHERE isbn_13= ?";
			
			stmt.setString(1, isbn);
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				BookTag tag = new BookTag();
				
				tag.setTag(rs.getString("tag_name"));
				
				tags.add(tag);
			}
			
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
		//Return the list		
		return tags;
	}

	@Override
	public List<Book> getAllBooksWithTag(String aTag) {
		List<Book> books = new ArrayList<>();
		
		try {
			connection = DAOUtilities.getConnection();
			String sql = "SELECT a.isbn_13, a.title, a.author, a.publish_date, a.price, a.content,"
					+ "b.isbn_13, b.tag_name FROM books a, book_tags b WHERE b.tag_name= ?";
			stmt = connection.prepareStatement(sql);
			
			stmt.setString(1, aTag);
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Book book = new Book();
				
				book.setIsbn13(rs.getString("isbn"));
				book.setAuthor(rs.getString("author"));
				book.setTitle(rs.getString("title"));
				book.setPublishDate(rs.getDate("publish_date").toLocalDate());
				book.setPrice(rs.getDouble("price"));
				book.setContent(rs.getBytes("content"));
				
				books.add(book);
				
			}
			
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
		
		return books;
	}
	
	/*------------------------------------------------------------------------------------------------*/

	// Closing all resources is important, to prevent memory leaks. 
	// Ideally, you really want to close them in the reverse-order you open them
	private void closeResources() {
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException e) {
			System.out.println("Could not close statement!");
			e.printStackTrace();
		}
		
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			System.out.println("Could not close connection!");
			e.printStackTrace();
		}
	}
	
}




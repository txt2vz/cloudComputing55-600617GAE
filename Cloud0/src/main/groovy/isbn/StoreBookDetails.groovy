package isbn

import javax.servlet.ServletConfig
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import groovy.json.JsonSlurper
import groovy.servlet.GroovyServlet

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

class StoreBookDetails extends GroovyServlet{

	void init(ServletConfig config) {
		System.out.println " UrlToJSON Servlet initialized"	
	}

	void service(HttpServletRequest request, HttpServletResponse response) {
		
		// Obtain an object for accessing the Datastore.
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		// Set the Kind. This is where the book data (as entities) will be stored.
		// A Kind is the equivalent of a Table in a relational database.
		String strKind = "Book"
		
		String usersISBN = request.getParameter("isbn");
		String googleBooksURL = 'https://www.googleapis.com/books/v1/volumes?q=isbn:'+usersISBN+'&country=UK' 
		def url = googleBooksURL.toURL()
		def json = url.getText()
		// println "json: " + json  //for debugging
		
		def jsonSlurper = new JsonSlurper()
		def object = jsonSlurper.parseText(json)
		def title = object.items[0].volumeInfo.title	
		
		// Create a new entity for the retrieved book.
		// The Kind is "Book" because the value of strKind was set to "Book" near the start of this code.
		// The ID of the entity is the ISBN number of the book.
		Entity book = new Entity(strKind, usersISBN);
		
		// Assign the new book entity property values obtained from the XML data returned by the ISBNdb web service.
		book.setProperty("Title", title)
		book.setProperty("ISBN", usersISBN);
		
		// Save the new book entity in the Datastore.
		datastore.put(book);

		response.getWriter().println(title)
		
		// Output the text for the start of the books list.
		response.getWriter().println("<br />Books list:<br />");
		
		// Create query to return all entities in the Kind "Book" from the Datastore.
		// The value of strKind was set to "Book" near the start of this code.
		Query q = new Query(strKind);
		PreparedQuery pq = datastore.prepare(q);
	//	
		// Iterate over all entities returned from the Datastore and
		// put properties in the response (output) string to be displayed on the web page.
		for (Entity result : pq.asIterable()) {
			response.getWriter().println("<br />Title : " + (String)result.getProperty("Title"));
			response.getWriter().println("<br />ISBN : " + (String)result.getProperty("ISBN"));
			response.getWriter().println("<br />-------------------------------------------------------------------------------");
		}
	}
}
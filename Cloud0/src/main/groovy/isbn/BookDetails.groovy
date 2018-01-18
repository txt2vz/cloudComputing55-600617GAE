package isbn

import javax.servlet.ServletConfig
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import groovy.json.JsonSlurper
import groovy.servlet.GroovyServlet


class BookDetails extends GroovyServlet{

	void init(ServletConfig config) {
		System.out.println " UrlToJSON Servlet initialized"
	}

	void service(HttpServletRequest request, HttpServletResponse response) {
		
		String usersISBN = request.getParameter("isbn");
		String googleBooksURL = 'https://www.googleapis.com/books/v1/volumes?q=isbn:'+usersISBN 
		def url = googleBooksURL.toURL()
		def json = url.getText()
		println "json: " + json
		def jsonSlurper = new JsonSlurper()
		def object = jsonSlurper.parseText(json)
		def title = object.items[0].volumeInfo.title	

		response.getWriter().println(title)
	}
}
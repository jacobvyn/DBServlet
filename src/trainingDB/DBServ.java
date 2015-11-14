package trainingDB;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;

//import com.google.gson.JsonArray;

@WebServlet(urlPatterns = { "/dbUpdate", "/dbChange", "/dbDelete" })
public class DBServ extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		response.setContentType("text/html");
		MyDBDriver driver = new MyDBDriver();
		// JsonArray jSonArray = driver.getJsonResultSet();
		JsonArray jrs = driver.getJsonResultSet();
		response.getWriter().print(jrs);

		driver.releaseResources();



	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}

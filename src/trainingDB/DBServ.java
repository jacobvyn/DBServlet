package trainingDB;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.google.gson.JsonArray;

@WebServlet(urlPatterns = { "/dbAdd", "/dbChange", "/dbDelete", "/dbGetData" })
public class DBServ extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf8");
		response.setCharacterEncoding("utf8");

		String url = request.getRequestURI();
		// if add url
		if (url.equalsIgnoreCase("/DBServlet/dbAdd")) {
			System.out.println("here");

			try {
				String jString = readRequest(request);
				JSONObject jObject = new JSONObject(jString);

				// show in console
				System.out.println(jObject);

				MyDBDriver md = new MyDBDriver();
				md.addRecord(jObject);
				md.releaseResources();

			} catch (JSONException e) {
				e.printStackTrace();
			}

			// if change url
		} else if (url.equalsIgnoreCase("/DBServlet/dbChange")) {
			response.getWriter().print("hello from servlet (query - delete) ");
			// if delete url
		} else if (url.equalsIgnoreCase("/DBServlet/dbDelete")) {
			response.getWriter().print("Next query was performed: " + request.getRequestURI());
			// by uploading of application
		} else if (url.equalsIgnoreCase("/DBServlet/dbGetData")) {

			response.setContentType("application/json");
			MyDBDriver driver = new MyDBDriver();
			JSONArray jrs = driver.getJSONResultSet();

			response.getWriter().print(jrs);

			driver.releaseResources();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	private String readRequest(HttpServletRequest request) {

		String jString = "";

		try {
			BufferedReader br = new BufferedReader(request.getReader());
			String c;
			while ((c = br.readLine()) != null) {
				jString += c;
			}
			br.close();
		} catch (IOException e) {
			System.out.println("smth wroooooooooooong");
			e.printStackTrace();
		}
		return jString;
	}

}

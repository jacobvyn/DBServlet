package trainingDB;

import java.io.BufferedWriter;

import java.io.IOException;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



@WebServlet(urlPatterns = { "/dbAdd", "/dbChange", "/dbDelete", "/dbGetData" })
public class DBServ extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String ADD = "/DBServlet/dbAdd";
	private static final String CHANGE = "/DBServlet/dbChange";
	private static final String DELETE = "/DBServlet/dbDelete";
	private static final String GET_DATA = "/DBServlet/dbGetData";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		JSONObject jObject = new JSONObject();
		Enumeration<String> paramNames = request.getParameterNames();

		if (!(paramNames == null)) {
			try {
				while (paramNames.hasMoreElements()) {
					String key = (String) paramNames.nextElement();
					String value = request.getParameter(key);
					jObject.put(key, value);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		String url = request.getRequestURI();

		// if add url
		if (url.equalsIgnoreCase(DBServ.ADD)) {

			System.out.println("SERVLET : received object from parametres : " + jObject);

			// add object to db if it is not a null
			if (!(jObject.length() == 0)) {
				addToDB(jObject);
			} else {
				System.out.println("You want to add an empty record....");
			}
		}

		// if change url
		else if (url.equalsIgnoreCase(DBServ.CHANGE)) {

			if (!(jObject.length() == 0)) {
				changeInDB(jObject);
			}

		}

		// if delete url
		else if (url.equalsIgnoreCase(DBServ.DELETE)) {

			if (!(jObject.length() == 0)) {
				deleteInDB(jObject);
			}
		}

		// by uploading of application
		else if (url.equalsIgnoreCase(DBServ.GET_DATA)) {
			
			response.setContentType("application/json");

			MyDBDriver driver = new MyDBDriver();
			JSONArray jrs = driver.getJSONResultSet();

			BufferedWriter out = new BufferedWriter(response.getWriter());
			out.write(jrs.toString());

			out.close();
			driver.releaseResources();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doPost(request, response);

	}

	private void addToDB(JSONObject jObject) {
		MyDBDriver driver = new MyDBDriver();
		driver.addRecord(jObject);
		driver.releaseResources();

	}

	private void deleteInDB(JSONObject jObject) {
		MyDBDriver driver = new MyDBDriver();
		driver.deleteRecord(jObject);
		driver.releaseResources();

	}
	
	//finished
	private void changeInDB(JSONObject jObject) {
		MyDBDriver driver = new MyDBDriver();
		driver.updateRecord(jObject);
		driver.releaseResources();

	}


}

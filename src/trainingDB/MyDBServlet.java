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

@WebServlet(urlPatterns = { "/dbAdd", "/dbChange", "/dbDelete", "/dbGetData", "/dbGetDataAndPrint" })
public class MyDBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String ADD = "/DBServlet/dbAdd";
	private static final String CHANGE = "/DBServlet/dbChange";
	private static final String DELETE = "/DBServlet/dbDelete";
	private static final String GET_DATA = "/DBServlet/dbGetData";

	private HiberDAO hiberDAO = null;

	@Override
	public void init() throws ServletException {

		hiberDAO = new HiberDAO();

	}

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
		if (url.equalsIgnoreCase(MyDBServlet.ADD)) {

			System.out.println();
			System.out.println("[MyDBServlet] : received object from parameters : " + jObject);

			// add object to db if it is not a null
			if (!(jObject.length() == 0)) {
				hiberDAO.addPersonToDB(jObject);
			} else {
				System.out.println("You want to add an empty record....");
			}
		}

		// if change url
		else if (url.equalsIgnoreCase(MyDBServlet.CHANGE)) {

			if (!(jObject.length() == 0)) {
				hiberDAO.updateInDB(jObject);
			}

		}

		// if delete url
		else if (url.equalsIgnoreCase(MyDBServlet.DELETE)) {
			hiberDAO.deleteFromDB(jObject);
		}

		// by uploading of application
		else if (url.equalsIgnoreCase(MyDBServlet.GET_DATA)) {

			response.setContentType("application/json");

			JSONArray persons = hiberDAO.listPersonsFromDB();
			BufferedWriter out = new BufferedWriter(response.getWriter());
			out.write(persons.toString());
			out.close();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doPost(request, response);

	}

}

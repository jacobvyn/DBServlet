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

	private PersonDAO dao = null;

	@Override
	public void init() throws ServletException {
		dao = new PersonDAOImpl();
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

		switch (url) {
		case MyDBServlet.ADD:

			if (!(jObject.length() == 0)) {
				dao.create(jObject);
			} else {
				System.out.println("You want to add an empty record....");
			}
			break;

		case MyDBServlet.CHANGE:

			if (!(jObject.length() == 0)) {
				dao.update(jObject);
			}
			break;

		case MyDBServlet.DELETE:

			dao.delete(jObject);
			break;

		case MyDBServlet.GET_DATA:

			response.setContentType("application/json");
			JSONArray persons = dao.jsonArrList();

			BufferedWriter out = new BufferedWriter(response.getWriter());
			out.write(persons.toString());
			out.close();
			break;

		default:
			break;
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doPost(request, response);

	}

}

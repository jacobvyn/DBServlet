package trainingDB;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;

@WebServlet(urlPatterns = { "/dbChange", "/dbGetData" })
public class MyDBServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private PersonDAO dao = null;

	@Override
	public void init() throws ServletException {
		dao = new PersonDAOImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		setCharacterEncoding("utf-8", request, response);

		response.setContentType("application/json");
		List<Person> list = dao.list();
		JSONArray persons = toJsonArr(list);

		BufferedWriter out = new BufferedWriter(response.getWriter());
		out.write(persons.toString());
		out.close();

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		setCharacterEncoding("utf-8", request, response);
		String message = readMessage(request);
		Person person = toPerson(message);
		System.out.println(message);
		dao.update(person);

	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		setCharacterEncoding("utf-8", request, response);
		String message = readMessage(request);
		Person person = toPerson(message);
		System.out.println(message);
		dao.create(person);

	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		setCharacterEncoding("utf-8", request, response);

		String message = readMessage(request);
		Person person = toPerson(message);
		dao.delete(person.getId());

	}

	private Person toPerson(String message) {
		Gson gson = new Gson();
		Person person = gson.fromJson(message, Person.class);
		return person;
	}

	private String readMessage(HttpServletRequest request) {
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();
		try {
			reader = request.getReader();

			String line = reader.readLine();
			while (line != null) {
				builder.append(line);
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return builder.toString();
	}

	private JSONArray toJsonArr(List<Person> list) {
		JSONArray array = new JSONArray();
		Gson gson = new Gson();
		for (Person person : list) {
			String jsonStr = gson.toJson(person);
			try {
				JSONObject obj = new JSONObject(jsonStr);
				array.put(obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return array;
	}

	private void setCharacterEncoding(String string, HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding(string);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.setCharacterEncoding(string);

	}
}

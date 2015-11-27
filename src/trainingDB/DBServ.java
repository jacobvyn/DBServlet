package trainingDB;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		String url = request.getRequestURI();

		// if add url
		if (url.equalsIgnoreCase("/DBServlet/dbAdd")) {
			response.getWriter().print(
					"Next query was performed from Servlet: " + request.getRequestURI() + "\n When: " + new Date());
		 
			Enumeration<String> paramNames = request.getParameterNames();
			JSONObject jObject = new JSONObject();
			
			try {
				while (paramNames.hasMoreElements()) {
					String key = (String) paramNames.nextElement();
					String value = request.getParameter(key);
					jObject.put(key, value);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			System.out.println("SERVLET : object from parametres : " +jObject);
			response.getWriter().print("SERVLET : object from parametres 111: " +jObject);
			
			//add object to db if it is not a null
			 if (!(jObject.length()==0)) {
				 sendToDB(jObject);
			 }
<<<<<<< HEAD
=======
			
>>>>>>> origin/master
			 /*
			 try ( InputStream in = request.getInputStream()){
				 StringBuilder ob = new StringBuilder();
				 int c;
			
				 while ((c=in.read())!=0) {
					 ob.append((char) c);
				 }
				 System.out.println("object from stream : " +ob.toString());
			 }	*/
				
		}

		// if change url
		else if (url.equalsIgnoreCase("/DBServlet/dbChange")) {
			response.getWriter().print(
					"Next query was performed from Servlet: " + request.getRequestURI() + "\n When: " + new Date());
		}

		// if delete url
		else if (url.equalsIgnoreCase("/DBServlet/dbDelete")) {
			response.getWriter().print(
					"Next query was performed from Servlet: " + request.getRequestURI() + "\n When: " + new Date());
		}

		// by uploading of application
		else if (url.equalsIgnoreCase("/DBServlet/dbGetData")) {
			response.setContentType("application/json");
			
			MyDBDriver driver = new MyDBDriver();
			JSONArray jrs = driver.getJSONResultSet();
			
			BufferedWriter out = new BufferedWriter(response.getWriter()) ;
			out.write(jrs.toString());
			
			out.close();
			driver.releaseResources();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doPost(request, response);

	}

	private void sendToDB(JSONObject jObject) {
		MyDBDriver driver = new MyDBDriver();
		driver.addRecord(jObject);
		driver.releaseResources();

	}
	
	/*
	 * private String readRequest(HttpServletRequest request) {
	 * 
	 * String jString = "";
	 * 
	 * try {
	 * 
	 * // InputStream in = request.getInputStream(); BufferedReader in =
	 * request.getReader();
	 * 
	 * int c;
	 * 
	 * while ((c = in.read()) != -1) { jString += (char) c; }
	 * 
	 * return jString;
	 * 
	 * } catch (IOException e) { System.out.println("smth wroooooooooooong");
	 * e.printStackTrace(); } return "EMPTY (("; }
	 */

}

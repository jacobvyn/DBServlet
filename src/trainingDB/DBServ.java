package trainingDB;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

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
			
			
			
			String msg = readRequest(request);	
			if (msg.length()==0)msg=" received nothing ";
				response.getWriter().print("method doGET  :" +msg);	
	
		}
		
		
		// if change url
		else if (url.equalsIgnoreCase("/DBServlet/dbChange")) {
			response.getWriter().print("Next query was performed from Servlet: " + request.getRequestURI() +"\n When: " + new Date());	
		}
		
		// if delete url
		else if (url.equalsIgnoreCase("/DBServlet/dbDelete")) {
			response.getWriter().print("Next query was performed from Servlet: " + request.getRequestURI() +"\n When: " + new Date());
		}
		
		// by uploading of application  
				//working!!!
		else if (url.equalsIgnoreCase("/DBServlet/dbGetData")) {
			
			System.out.println("Next query was performed from Servlet: " + request.getRequestURI() +"\n When: " + new Date());
			
			response.setContentType("application/json");
			MyDBDriver driver = new MyDBDriver();
			JSONArray jrs = driver.getJSONResultSet();

			response.getWriter().print(jrs);

			driver.releaseResources();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String msg = readRequest(request);	
		if (msg.length()==0)msg=" received nothing ";
			response.getWriter().print("method doPOST  :" +msg);	
		
		
	}

	private String readRequest(HttpServletRequest request) {

		String jString = "";

		try {
			
			InputStream in = request.getInputStream();
			
			int c;
			
			while ((c=in.read())!=-1) {
				jString +=(char) c;
			}
			
			return jString;
			
		} catch (IOException e) {
			System.out.println("smth wroooooooooooong");
			e.printStackTrace();
		}
		return "EMPTY ((";
	}

}

package trainingDB;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class MyProperties {
	private String login;
	private String password;
	
	public MyProperties() throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(new File("D:\\Education\\Java eclipse\\DBServlet\\info.txt")));
		
		login = prop.getProperty("login");
		password = prop.getProperty("pass");
	}
	
	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}
	
	/*
	public static void main(String[] args) {
		try {
			MyProperties p = new MyProperties();
			
			System.out.println(p.getLogin() +" " +p.getPassword());
			System.out.println(new File(".").getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/

}

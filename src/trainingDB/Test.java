package trainingDB;

import org.json.JSONArray;

public class Test {
	
	public static void main(String[] args) {
		MyDBDriver driver = new MyDBDriver();
		JSONArray jrs = driver.getJSONResultSet();
		System.out.println(jrs);
		driver.releaseResources();
		
	}
	

	

}

package trainingDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.locks.ReadWriteLock;

public class Test {

	
	public static void main(String[] args) {
		URL serverUrl;
		try {
			serverUrl = new URL("http://weather.i.ua");
			
		
		HttpURLConnection connection = (HttpURLConnection) serverUrl.openConnection();
		
		connection.setDoInput(true);
		
		InputStreamReader isr= new InputStreamReader(connection.getInputStream());
		
		BufferedReader br = new BufferedReader(isr);
		
		StringBuilder jString = new StringBuilder();
		String c;

		
		while ((c = br.readLine()) != null) {
			System.out.println(c);;
		}
		br.close();
		
		//System.out.println(jString.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
		/*
		HiberDAO dao= new HiberDAO();
	//	dao.print();
		for (String s : dao.getColumnNamesAsStrings()){
			System.out.println(s);*/
		}
		
		/*
		MyDBDriver driver = new MyDBDriver();
		JSONArray jrs = driver.getJSONResultSet();
		System.out.println(jrs);
		driver.releaseResources();
		*/
	
	/*
	public void fillTheTable111() {
		try {
			if (jArray != null) {

				for (int i = 0; i < jArray.length(); i++) {

					JSONObject record = jArray.getJSONObject(i);
					ArrayList<String> row = new ArrayList<>();
					String cellsContent;

					for (int j = 1; j <= columnsNames.length(); j++) {
						cellsContent = columnsNames.getString(String.valueOf(j));
						row.add(record.getString(cellsContent));
					}
					addDate(row.toArray(new String[row.size()]));
				}
			} else {
				System.out.println("Something wrong with creating of JsonArray (MyTableModel.fillTheTable)");
			}
		} catch (JSONException e) {
			System.out.println("Exception by creating JsonObject");
			e.printStackTrace();
		}

	}
	
*/
	



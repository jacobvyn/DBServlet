package trainingDB;



import org.json.JSONArray;


public class Test {

	
	public static void main(String[] args) {
		MyDBDriver driver = new MyDBDriver();
		JSONArray jrs = driver.getJSONResultSet();
		System.out.println(jrs);
		driver.releaseResources();
		
	}
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
	

}

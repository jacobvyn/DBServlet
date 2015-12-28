package trainingDB;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyDBDriver {

	private final static String dbURL = "jdbc:postgresql://localhost:5432/myfirstdb";
	private static final String tableName = "fdbtest";
	private Connection connect = null;// ***************
	private MyProperties propertie;

	public MyDBDriver() {
		getConnectionToDB();
	}

	private void getConnectionToDB() {

		if (connect == null) {
			try {
				propertie = new MyProperties();
				Class.forName("org.postgresql.Driver");

				connect = DriverManager.getConnection(dbURL, propertie.getLogin(), propertie.getPassword());

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void releaseResources() {
		if (connect != null) {
			try {
				connect.close();
			} catch (SQLException e) {

				System.out.println("Exception by closing connect!");
				e.printStackTrace();
			}
		}

	}
	
	private JSONObject getColumnsNames(ResultSetMetaData rsMetaData) {
		JSONObject columnsName = new JSONObject();
		try {
			for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
				columnsName.put(String.valueOf(i), rsMetaData.getColumnName(i));
			}
		} catch (SQLException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return columnsName;
		
	}

	public JSONArray getJSONResultSet() {
		if (connect != null) {
			JSONArray jArray = null;
			try (Statement statement = connect.createStatement()) {

				ResultSet rs = statement.executeQuery("SELECT * FROM " + tableName);
				ResultSetMetaData rsMetaData = rs.getMetaData();

				//make object with columns name
				JSONObject columnsName =getColumnsNames(rsMetaData);
				
				jArray = new JSONArray();
				while (rs.next()) {
					JSONObject jDB = new JSONObject();
					for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {

						String key = columnsName.getString(String.valueOf(i));
						String value;
						if (key.equals("birth_day")) {
							value = rs.getDate(i).toString();
						} else
							value = String.valueOf(rs.getString(i));
						jDB.put(key, value);
					}
					jArray.put(jDB);
				}
				rs.close();
				// add columns' names to jasonArray as last
				jArray.put(columnsName);

			} catch (SQLException e) {
				System.out.println("Exception from method getJson (sql...)");
				e.printStackTrace();
			} catch (JSONException e) {
				System.out.println("Exception from method getJson");
				e.printStackTrace();
			}
			return jArray;
		} else {
			System.out.println("Connection  is null");
		}
		return null;
	}

	// ----------------------------------------------------------------------------

	public void updateRecord(JSONObject jObject) {
		try (Statement statement = connect.createStatement()) {

			// get user id that has to be changed and remove this record from
			// jObject
			String user_id = jObject.getString("user_id");
			jObject.remove("user_id");

			String updSQL = makeUpdateSQL(jObject, user_id);

			System.out.println(" request to change is : " + updSQL);
			statement.execute(updSQL);

		} catch (SQLException e) {
			System.out.println("Exception from method MyDriver.update");
			e.printStackTrace();
		} catch (JSONException e) {
			System.out.println("Excpetion in metod MyDBDriver.updateRecord(JSONObject jObject)");
			e.printStackTrace();
		}
	}

	public void deleteRecord(JSONObject recordToDel) {
		String deleteSQL = makeDeleteSQL(recordToDel);

		try (Statement statement = connect.createStatement()) {
			statement.execute(deleteSQL);
		} catch (SQLException e) {
			System.out.println("Exception from method delete");
			e.printStackTrace();
		}
	}

	public void addRecord(JSONObject jObject) {
		String[] keys = JSONObject.getNames(jObject);
		ArrayList<String> values = getValuesFromJSON(jObject);

		String addSQL = makeAddSQL(keys, values);
		System.out.println("sql =  " + addSQL);
		try (Statement statement = connect.createStatement()) {
			statement.execute(addSQL);
		} catch (SQLException e) {
			System.out.println("Exception from method add");
			e.printStackTrace();
		}
	}

	// ----------------------------------------------------------------------------
	private String makeDeleteSQL(JSONObject o) {
		StringBuilder deleteSQL = new StringBuilder();

		try {
			deleteSQL.append("DELETE FROM ");
			deleteSQL.append(tableName);
			deleteSQL.append(" WHERE user_id=");
			deleteSQL.append(o.getString("toDelete"));
		} catch (JSONException e) {
			System.out.println("Exception from makeDeleteSQL");
			e.printStackTrace();
		}

		return deleteSQL.toString();

	}

	private String makeUpdateSQL(JSONObject jObject, String user_id) {
		String[] fieldsNames = JSONObject.getNames(jObject);
		ArrayList<String> valuesList = getValuesFromJSON(jObject);

		String fields = makeColumnsForSQL(fieldsNames);
		String vaules = makeValuesForSQL(valuesList);
		
		StringBuilder updSQL = new StringBuilder();
		updSQL.append("UPDATE ");
		updSQL.append(tableName);
		updSQL.append(" SET ");
		updSQL.append(fields);
		updSQL.append(" = ");
		updSQL.append(vaules);
		updSQL.append(" WHERE user_id = ");
		updSQL.append(user_id);

		return updSQL.toString();

	}

	private String makeAddSQL(String[] keys, ArrayList<String> val) {
		String columns = makeColumnsForSQL(keys);
		String values = makeValuesForSQL(val);

		StringBuilder addSQL = new StringBuilder();

		addSQL.append("INSERT INTO ");
		addSQL.append(tableName);
		addSQL.append(" ");
		addSQL.append(columns);
		addSQL.append(" VALUES ");
		addSQL.append(values);

		return addSQL.toString();

	}

	// ----------------------------------------------------------------------------

	private String makeColumnsForSQL(String[] keys) {
		StringBuilder keysStr = new StringBuilder();
		keysStr.append("(");
		for (String s : keys) {
			keysStr.append(s);
			keysStr.append(", ");
		}
		keysStr.deleteCharAt(keysStr.length() - 1);
		keysStr.deleteCharAt(keysStr.length() - 1);
		keysStr.append(")");
		return keysStr.toString();

	}

	private String makeValuesForSQL(ArrayList<String> values) {
		StringBuilder valuesStr = new StringBuilder();
		valuesStr.append("('");
		for (String s : values) {
			valuesStr.append(s);
			valuesStr.append("', '");
		}
		valuesStr.deleteCharAt(valuesStr.length() - 1);
		valuesStr.deleteCharAt(valuesStr.length() - 1);
		valuesStr.deleteCharAt(valuesStr.length() - 1);
		valuesStr.append(")");
		return valuesStr.toString();

	}

	private ArrayList<String> getValuesFromJSON(JSONObject jObject) {
		ArrayList<String> values = new ArrayList<>();
		for (String s : JSONObject.getNames(jObject)) {
			try {
				values.add(jObject.getString(s));
			} catch (JSONException e) {
				System.out.println("Exception from getValuesFromJSON");
				e.printStackTrace();
			}
		}
		return values;

	}

	// ----------------------------------------------------------------------------
	@SuppressWarnings("unused")
	private void createTable() {
		try (Statement statement = connect.createStatement()) {
			String createTableSQL = "CREATE TABLE fdbtest (" + "USER_ID 			SERIAL,"
					+ "FIRSTNAME 		VARCHAR(10)	 	 NOT NULL," + "LASTNAME 		VARCHAR(10) 	 NULL,"
					+ "BIRTH_DAY 		DATE 			 NULL," + "JOB 				VARCHAR(10)  	 NULL,"
					+ "COMMENT 			VARCHAR(20) 	 NULL," + "PRIMARY KEY  	(USER_ID))";

			System.out.println("Request is: " + createTableSQL);
			statement.execute(createTableSQL);
			System.out.println("Table FDBtest is created!");
		} catch (SQLException e) {
			System.out.println("Exception from method create");
			e.printStackTrace();
		}
	}

	/*
	 * public static void main(String[] args) { MyDBDriver my = new
	 * MyDBDriver();
	 * 
	 * my.addRecord("Vasya", "Pupkin", "1990-12-21", "player", "bad guy");
	 * my.addRecord("Alesha", "Popovich", "1018-12-22", "wariaor", "speedy");
	 * my.addRecord("ILiya", "Muromec", "1012-08-03", "wariaor", "strong");
	 * my.addRecord("Jacob", "Vin", "1986-09-06", "Engineer", "yet");
	 * my.addRecord("Vasya", "Pupkin", "1990-12-21", "player", "bad guy");
	 * my.addRecord("Jacob", "Vin", "1986-09-06", "Engineer", "yet");
	 * my.addRecord("Vasya", "Pupkin", "1990-12-21", "player", "bad guy");
	 * my.addRecord("Jacob", "Vin", "1986-09-06", "Engineer", "yet");
	 * my.addRecord("Vasya", "Pupkin", "1990-12-21", "player", "bad guy");
	 * my.addRecord("ILiya", "Muromec", "1012-08-03", "wariaor", "strong");
	 * my.releaseResources(); }
	 */
}

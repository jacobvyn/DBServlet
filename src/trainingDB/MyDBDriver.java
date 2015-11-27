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

	public JSONArray getJSONResultSet() {
		if (connect != null) {
			JSONArray jArray = null;
			try (Statement statement = connect.createStatement()) {

				ResultSet rs = statement.executeQuery("SELECT * FROM " + tableName);
				ResultSetMetaData rsmd = rs.getMetaData();

				JSONObject columnsName = new JSONObject();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					columnsName.put("" + i, rsmd.getColumnName(i));

				}

				jArray = new JSONArray();
				while (rs.next()) {

					JSONObject jDB = new JSONObject();

					for (int i = 1; i <= rsmd.getColumnCount(); i++) {

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

	public void updateRecord(String[] fields, String[] values, int user_id) {
		try (Statement statement = connect.createStatement()) {

			String updSQL = "";

			for (int i = 0; i < fields.length; i++) {
				updSQL = "UPDATE " + tableName + " SET " + fields[i] + " = '" + values[i] + "' WHERE user_id = "
						+ user_id;
				System.out.println("--------------------");
				System.out.println("UPDATE REQUEST : " + updSQL);
				System.out.println("--------------------");
				statement.execute(updSQL);
			}
		} catch (SQLException e) {
			System.out.println("Exception from method MyDriver.update");
			e.printStackTrace();
		}
	}

	public void updateRecord(ArrayList<String> fields, ArrayList<String> values, int user_id) {
		try (Statement statement = connect.createStatement()) {

			String updSQL = "";

			for (int i = 0; i < fields.size(); i++) {
				updSQL = "UPDATE " + tableName + " SET " + fields.get(i) + " = '" + values.get(i) + "' WHERE user_id = "
						+ user_id;
				System.out.println("--------------------");
				System.out.println("UPDATE REQUEST : " + updSQL);
				System.out.println("--------------------");
				statement.execute(updSQL);
			}
		} catch (SQLException e) {
			System.out.println("Exception from method MyDriver.update");
			e.printStackTrace();
		}
	}

	public void deleteRecord(int personToDel) {
		try (Statement statement = connect.createStatement()) {
			String deleteSQL = "DELETE FROM " + tableName + " WHERE user_id=" + personToDel;
			statement.execute(deleteSQL);

		} catch (SQLException e) {
			System.out.println("Exception from method delete");
			e.printStackTrace();
		}
	}

	public void addRecord(JSONObject jObject) {
		try {
			String firstName = jObject.getString("firstName");
			String lastName = jObject.getString("lastName");
			String birthDay = jObject.getString("birthDay");
			String job = jObject.getString("job");
			String comment = jObject.getString("comment");

			if (birthDay.isEmpty()) {
				addRecord(firstName, lastName, job, comment);
			} else {
				addRecord(firstName, lastName, birthDay, job, comment);
			}

		} catch (JSONException e) {
			System.out.println("Exception from metod MyDriver.addrecord (JSONObject o)");
			e.printStackTrace();
		}

	}

	public void addRecord(String firstName, String lastName, String birthDay, String job, String comment) {
		try (Statement statement = connect.createStatement()) {
			String addSQL = "INSERT INTO " + tableName + " (FIRSTNAME, LASTNAME, BIRTH_DAY, JOB, COMMENT)" + "VALUES('"
					+ firstName + "','" + lastName + "','" + birthDay + "','" + job + "','" + comment + "')";
			statement.execute(addSQL);
		} catch (SQLException e) {
			System.out.println("Exception from method add");
			e.printStackTrace();
		}

	}

	public void addRecord(String firstName, String lastName, String job, String comment) {
		try (Statement statement = connect.createStatement()) {
			String addSQL = "INSERT INTO " + tableName + " (FIRSTNAME, LASTNAME, JOB, COMMENT)" + "VALUES('" + firstName
					+ "','" + lastName + "','" + job + "','" + comment + "')";
			statement.execute(addSQL);
		} catch (SQLException e) {
			System.out.println("Exception from method add");
			e.printStackTrace();
		}
	}

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
	 * public static void main(String[] args) { MyDBDriver m = new MyDBDriver();
	 * JSONArray a= m.getJSONResultSet(); m.releaseResources(); }
	 * 
	 */

	/*
	 * to fill DB public static void main(String[] args) { MyDBDriver my = new
	 * MyDBDriver();
	 * 
	 * my.addRecord("Vasya", "Pupkin", "1990-12-21", "player", "bad guy");
	 * my.addRecord("Alesha", "Popovich", "1018-12-22", "wariaor", "speedy");
	 * my.addRecord("ILiya", "Muromec", "1012-08-03", "wariaor", "strong");
	 * my.addRecord("Jacob", "Vin", "1986-06-06", "Engineer", "yet");
	 * my.addRecord("Vasya", "Pupkin", "1990-12-21", "player", "bad guy");
	 * my.addRecord("Jacob", "Vin", "1986-06-06", "Engineer", "yet");
	 * my.addRecord("Vasya", "Pupkin", "1990-12-21", "player", "bad guy");
	 * my.addRecord("Jacob", "Vin", "1986-06-06", "Engineer", "yet");
	 * my.addRecord("Vasya", "Pupkin", "1990-12-21", "player", "bad guy");
	 * mt.addRecord("ILiya", "Muromec", "1012-08-03", "wariaor", "strong");
	 * my.releaseResources(); }
	 */

}

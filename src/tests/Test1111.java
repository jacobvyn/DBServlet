package tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import trainingDB.Person;
import trainingDB.PersonDAO;
import trainingDB.PersonDAOImpl;

public class Test1111 {
	public static void main(String[] args) {
		
		PersonDAO dao = new PersonDAOImpl();

		List<Person> list = dao.list();


		System.out.println("==========From Database just as list===============");
		for (Person person : list) {
			System.out.println(person.toString());
		}
		System.out.println("======================================================");
		System.out.println();
		System.out.println();

		JSONArray arr = fromListToJson(list);
		System.out.println("==========JSON arra from list===============");
		System.out.println(arr);
		System.out.println("======================================================");
		
				
		System.out.println("==========New List from json array===============");
		List<Person> newList = fromJsonToList(arr);

		for (Person person : newList) {
			System.out.println(person.toString());
		}
		System.out.println("======================================================");
		
	

	}

	public static List<Person> fromJsonToList(JSONArray jArray) {
		List<Person> list = new ArrayList<>();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd").create();

		if (jArray == null) {
			System.out.println("jArray is null");
			return null;
		} else {

			for (int i = 0; i < jArray.length(); i++) {
				JSONObject record;
				try {
					record = jArray.getJSONObject(i);
					Person pers = gson.fromJson(record.toString(), Person.class);
					list.add(pers);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
		return list;
	}

	public static JSONArray fromListToJson(List<Person> persons) {
		JSONArray jArray = new JSONArray();

		for (Person person : persons) {
			jArray.put(new JSONObject(person));
		}
		return jArray;
	}

}

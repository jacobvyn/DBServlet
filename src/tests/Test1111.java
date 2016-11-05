package tests;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import trainingDB.Person;

public class Test1111 {

	public static Collection<Person> fromJsonToList(JSONArray jArray) {
		Collection<Person> list = new ArrayList<>();
		Gson gson = new GsonBuilder().create();

		if (jArray == null) {
			System.out.println("jArray is null");
			return null;
		} else {
			try {
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject record = jArray.getJSONObject(i);
					Person pers = gson.fromJson(record.toString(), Person.class);
					list.add(pers);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

}

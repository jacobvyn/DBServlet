package trainingDB;

import org.json.JSONArray;
import org.json.JSONObject;

public interface PersonDAO {
	
	public void create(JSONObject jObject);

	public void delete(JSONObject jObject);

	public void update(JSONObject jObject);

	public JSONArray list();

}

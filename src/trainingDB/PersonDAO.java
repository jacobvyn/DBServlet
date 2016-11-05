package trainingDB;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public interface PersonDAO {

	public void create(JSONObject jObject);

	public void delete(JSONObject jObject);

	public void update(JSONObject jObject);

	public List<Person> list();

	public JSONArray jsonArrList();

}

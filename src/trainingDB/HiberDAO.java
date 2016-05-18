package trainingDB;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.metadata.ClassMetadata;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;

public class HiberDAO {
	private static SessionFactory factory = null;

	public HiberDAO() {
		initilize();
	}

	private void initilize() {
		try {
			factory = new Configuration().configure().buildSessionFactory();

		} catch (Exception e) {
			System.out.println("Exception by creating session factory!");
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public JSONArray listPersonsFromDB() {
		List<Person> persons = null;

		Session session = factory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			persons = session.createQuery("From Person").list();

			tx.commit();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return listToJsonArray(persons);

	}

	public void deleteFromDB(JSONObject jObject) {
		Integer id = null;
		try {
			id = jObject.getInt("toDelete");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Person person = (Person) session.get(Person.class, id);

			session.delete(person);
			tx.commit();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	public JSONObject getColumnNamesAsJSON() {
		ClassMetadata classMetaData = factory.getClassMetadata(Person.class);

		JSONObject names = new JSONObject();
		String[] columnsNames = classMetaData.getPropertyNames();
		try {
			names.put("0", classMetaData.getIdentifierPropertyName());

			for (int i = 0; i < columnsNames.length; i++) {
				names.put(String.valueOf(i + 1), columnsNames[i]);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return names;
	}

	public String[] getColumnNamesAsStrings() {
		ClassMetadata classMetaData = factory.getClassMetadata(Person.class);

		return classMetaData.getPropertyNames();
	}

	public JSONArray listToJsonArray(List<Person> persons) {
		JSONArray jArray = new JSONArray();
		try {
			// String[] metaData = hiberDAO.getColumnNamesAsStrings();
			for (Person pers : persons) {
				JSONObject jObject = new JSONObject();

				jObject.put("id", String.valueOf(pers.getId()));
				jObject.put("firstName", pers.getFirstName());
				jObject.put("lastName", pers.getLastName());
				jObject.put("birthDay", pers.getBirthDay().toString());
				jObject.put("job", pers.getJob());
				jObject.put("comment", pers.getComment());

				jArray.put(jObject);
			}
			jArray.put(getColumnNamesAsJSON());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jArray;
	}


	public void addPersonToDB(JSONObject jObject) {
		System.out.println(jObject);
		Person pers = null;
		try {
			pers = new Person(jObject.getString("FIRSTNAME"),
					jObject.getString("LASTNAME"),
					////////////////////////////////////////////////////////////////
					new Date(),
					jObject.getString("JOB"),
					jObject.getString("COMMENT"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(pers);
			tx.commit();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();

		} finally {
			session.close();
		}

	}
}

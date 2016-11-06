package trainingDB;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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


public class PersonDAOImpl implements PersonDAO {
	private static SessionFactory factory = null;

	public PersonDAOImpl() {
		try {
			factory = new Configuration().configure().buildSessionFactory();
		} catch (HibernateException e) {
			System.out.println("Exception by creating session factory!");
			e.printStackTrace();
		}
	}

	@Override
	public JSONArray jsonArrList() {
		List<Person> persons = list();
		JSONArray array =listToJsonArray(persons);
		return array;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Person> list() {
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
		return persons;
	}

	@Override
	public void delete(JSONObject jObject) {
		Integer id = null;
		try {
			id = jObject.getInt("toDelete");
		} catch (JSONException e) {
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

	@Override
	public void create(JSONObject jObject) {

		Person pers = null;
		try {
			pers = new Person(jObject.getString("FIRSTNAME"), jObject.getString("LASTNAME"),
					makeDateFromString(jObject.getString("BIRTHDAY")), jObject.getString("JOB"),
					jObject.getString("COMMENT"));
		} catch (JSONException e) {
			System.out.println("Exception by creating Person");
			e.printStackTrace();
		}
		if (pers == null)
			System.out.println("Person is  null");
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

	@Override
	public void update(JSONObject jObject) {

		String str_id = null;
		try {
			str_id = jObject.getString("user_id");

			jObject.remove("user_id");
		} catch (JSONException e) {

			e.printStackTrace();
		}
		System.out.println("[HiberDAO] " + jObject);
		if (jObject.length() != 0) {

			Integer id = Integer.valueOf(str_id);
			Session session = factory.openSession();
			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				Person person = session.get(Person.class, id);

				ArrayList<Method> methods = leaveOnlySetters(person.getClass().getDeclaredMethods());

				for (String name : JSONObject.getNames(jObject)) {
					String value = jObject.getString(name);
					Method met = methods.get(getIndexOfOppropriateMethod(methods, name));
					met.setAccessible(true);

					if (name.equalsIgnoreCase("BIRTHDAY")) {
						Date birthDay = makeDateFromString(value);
						met.invoke(person, birthDay);
					} else {
						met.invoke(person, value);
					}
				}
				session.update(person);
				tx.commit();
			} catch (HibernateException e) {
				if (tx != null)
					tx.rollback();
				System.out.println("HibernateException");
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("IllegalAccessException");
				e.printStackTrace();
			} finally {
				session.close();
			}

		}
	}

	private int getIndexOfOppropriateMethod(ArrayList<Method> methods, String name) throws Exception {

		for (int i = 0; i < methods.size(); i++) {
			String methodName = methods.get(i).getName();

			if (methodName.toLowerCase().contains(name.toLowerCase())) {
				return i;
			}
		}
		throw new Exception("Appropriate method is not found");
	}

	private static ArrayList<Method> leaveOnlySetters(Method[] mts) {

		ArrayList<Method> methods = new ArrayList<>();

		for (int i = 0; i < mts.length; i++) {
			Method method = mts[i];
			if (method.getName().contains("set")) {
				methods.add(method);
			}
		}
		return methods;
	}

	private static Date makeDateFromString(String value) {

		String format = "yyyy-mm-dd";

		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(value);

		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		return date;
	}

	private JSONObject getColumnNamesAsJSON() {
		ClassMetadata classMetaData = factory.getClassMetadata(Person.class);

		JSONObject names = new JSONObject();
		String[] columnsNames = classMetaData.getPropertyNames();
		try {
			names.put("0", classMetaData.getIdentifierPropertyName());

			for (int i = 0; i < columnsNames.length; i++) {
				names.put(String.valueOf(i + 1), columnsNames[i]);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return names;
	}

	private JSONArray listToJsonArray(List<Person> persons) {
		JSONArray jArray = new JSONArray();

		try {
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
		} catch (JSONException e) {
			e.printStackTrace();
		}
		jArray.put(getColumnNamesAsJSON());
		return jArray;
	}

	private JSONArray listToJsonArrayNew(List<Person> persons) {
		JSONArray jArray = new JSONArray();
		for (Person pers : persons) {
			jArray.put(new JSONObject(pers));
		}
		jArray.put(getColumnNamesAsJSON());
		return jArray;
	}

}

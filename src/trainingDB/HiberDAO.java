package trainingDB;

import java.lang.reflect.InvocationTargetException;
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

		Person pers = null;
		try {
			pers = new Person(jObject.getString("FIRSTNAME"), jObject.getString("LASTNAME"),
					makeDateFromString("BIRTHDAY"), jObject.getString("JOB"), jObject.getString("COMMENT"));
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

	public void updateInDB(JSONObject jObject) {

		String str_id = null;
		try {
			str_id = jObject.getString("user_id");

			jObject.remove("user_id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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
				/*
				 * for (Method method : methods) {
				 * System.out.println(method.getName()); System.out.println(
				 * "Parameter class :    "); for (Parameter param :
				 * method.getParameters()) { System.out.println("   "
				 * +param.getParameterizedType().getTypeName());
				 * 
				 * } System.out.println("---------"); }
				 */

				for (String name : JSONObject.getNames(jObject)) {
					String value = jObject.getString(name);
					Method met = methods.get(getIndexOfOppropriateMethod(methods, name));
					met.setAccessible(true);
					/*
					 * System.out.println("filed : " + name);
					 * System.out.println("value : " + value);
					 * System.out.println("Method name : " + met.getName());
					 */
					if (name.equalsIgnoreCase("BIRTHDAY")) {
						Date birthDay = makeDateFromString(value);
						met.invoke(person, birthDay);
					} else {
						met.invoke(person, value);
					}

				}

				////////////////////
				session.update(person);
				/////////////////////

				tx.commit();
			} catch (HibernateException e) {
				if (tx != null)
					tx.rollback();
				System.out.println("HibernateException");
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				System.out.println("IllegalAccessException");

				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				System.out.println("IllegalArgumentException");

				e.printStackTrace();
			} catch (InvocationTargetException e) {
				System.out.println("InvocationTargetException");

				e.printStackTrace();
			} catch (JSONException e) {
				System.out.println("JSONException");

				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("Appropriate method is not found");
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

	public static ArrayList<Method> leaveOnlySetters(Method[] mts) {

		ArrayList<Method> methods = new ArrayList<>();

		for (int i = 0; i < mts.length; i++) {
			Method method = mts[i];
			if (method.getName().contains("set")) {
				methods.add(method);
			}
		}
		return methods;
	}

	public static Date makeDateFromString(String value) {

		String format = "yyyy-mm-dd";

		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(value);

		} catch (ParseException ex) {
			// ex.printStackTrace();
		}
		return date;
	}
}

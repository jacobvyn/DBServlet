package tests;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Test {

	
	public static void main(String[] args) {
		/*
		String s = "abs";
		System.out.println(s.contains("ab"));
	Person pers = new Person("Oleg", "Pikulenko", "2000-12-02", "trader", "good fellow");
	
	try {
		Method m = pers.getClass().getMethod("setComment", java.lang.String.class);
		m.setAccessible(true);
		m.invoke(pers, "yraaaa");
	
		
		System.out.println(pers.toString());
	} catch (NoSuchMethodException | SecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvocationTargetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	Method [] methods = pers.getClass().getDeclaredMethods();

	ArrayList<Method> mets= HiberDAO.leaveOnlySetters(methods);
	
	for (Method method : mets) {
		System.out.println(method);
	}
	*/
	/*
	for (Method method : methods) {
		System.out.println(method.getName());
	}
	*/
System.out.println();

Date date = new Date();
DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
System.out.println(df.format(date));
	
	
	/*
	Field [] fields = pers.getClass().getDeclaredFields();

	for (Field field : fields) {
		System.out.println(field.getName());
	}
		*/
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
	
		
		/*
		HiberDAO dao= new HiberDAO();
	//	dao.print();
		for (String s : dao.getColumnNamesAsStrings()){
			System.out.println(s);*/
		}
		
		/*
		MyDBDriver driver = new MyDBDriver();
		JSONArray jrs = driver.getJSONResultSet();
		System.out.println(jrs);
		driver.releaseResources();
		*/
	
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
	



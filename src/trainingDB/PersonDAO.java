package trainingDB;

import java.util.List;

public interface PersonDAO {

	public List<Person> list();

	public void create(Person person);

	public void update(Person person);

	public void delete(Integer id);

}

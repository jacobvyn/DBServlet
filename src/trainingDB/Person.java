package trainingDB;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * Created by Jacob on 13.05.2016.
 */
public class Person implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "pers_id_seq")
	@SequenceGenerator(name = "pers_id_seq", sequenceName = "pers_id_seq", allocationSize = 1)
	@Column(name = "user_id")
	private int id;
	private String firstName;
	private String lastName;
	private Date birthDay;
	private String job;
	private String comment;

	public Person() {
	}

	public Person(String firstName, String lastName, Date birthDay, String job, String comment) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDay = birthDay;
		this.job = job;
		this.comment = comment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String toString() {

		return "[" + getFirstName() + ", " + getLastName() + ", " + getBirthDay() + ", " + getJob() + ", "
				+ getComment() + "]";

	}

}

package trainingDB;

import java.util.Date;

/**
 * Created by Jacob on 13.05.2016.
 */
public class Person {

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



}

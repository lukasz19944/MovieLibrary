package library.model;

import library.dao.ActorDao;
import library.dao.MovieActorDao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class Actor {
    private int id;
    private String firstName;
    private String lastName;
    private String gender;
    private String nationality;
    private Date dateOfBirth;

    public Actor() {

    }

    public Actor(String firstName, String lastName, String gender, String nationality, Date dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public Integer calculateAge() {
        Date currentDate = new Date();
        if (dateOfBirth != null) {
            LocalDate dob = Instant.ofEpochMilli(dateOfBirth.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate now = Instant.ofEpochMilli(currentDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
            return Period.between(dob, now).getYears();
        } else {
            return 0;
        }
    }

    public Float getAverageRate() {
        ActorDao dao = new ActorDao();

        return dao.averageActorRate(this);
    }

    public void updateActorRate(int movieId, Float rate) {
        MovieActorDao dao = new MovieActorDao();

        dao.updateActorRate(movieId, this.getId(), rate);
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", nationality='" + nationality + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return id == actor.id &&
                Objects.equals(firstName, actor.firstName) &&
                Objects.equals(lastName, actor.lastName) &&
                Objects.equals(gender, actor.gender) &&
                Objects.equals(nationality, actor.nationality) &&
                Objects.equals(dateOfBirth, actor.dateOfBirth);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstName, lastName, gender, nationality, dateOfBirth);
    }
}

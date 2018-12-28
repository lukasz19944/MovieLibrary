package library.model;

import library.dao.DirectorDao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

public class Director {
    private int id;
    private String firstName;
    private String lastName;
    private String gender;
    private String nationality;
    private Date dateOfBirth;
    private Date dateOfDeath;

    public Director() {

    }

    public Director(String firstName, String lastName, String gender, String nationality, Date dateOfBirth, Date dateOfDeath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.dateOfDeath = dateOfDeath;
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

    public Date getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(Date dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public Float getAverageRate() {
        DirectorDao dao = new DirectorDao();

        return dao.averageDirectorRate(this);
    }

    public Integer calculateAge() {
        Date currentDate = new Date();
        if (dateOfBirth != null) {
            if (dateOfDeath == null) {
                LocalDate dob = Instant.ofEpochMilli(dateOfBirth.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate now = Instant.ofEpochMilli(currentDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                return Period.between(dob, now).getYears();
            } else {
                LocalDate dob = Instant.ofEpochMilli(dateOfBirth.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate dod = Instant.ofEpochMilli(dateOfDeath.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
                return Period.between(dob, dod).getYears();
            }
        } else {
            return 0;
        }
    }

    public String isAlive() {
        return dateOfDeath == null ? "Yes" : "No";
    }

    public String deathSign() {
        return isAlive().equals("Yes") ? "" : " [*]";
    }

    @Override
    public String toString() {
        return "Director{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", nationality='" + nationality + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", dateOfDeath=" + dateOfDeath +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Director director = (Director) o;
        return id == director.id &&
                Objects.equals(firstName, director.firstName) &&
                Objects.equals(lastName, director.lastName) &&
                Objects.equals(gender, director.gender) &&
                Objects.equals(nationality, director.nationality) &&
                Objects.equals(dateOfBirth, director.dateOfBirth) &&
                Objects.equals(dateOfDeath, director.dateOfDeath);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstName, lastName, gender, nationality, dateOfBirth, dateOfDeath);
    }
}

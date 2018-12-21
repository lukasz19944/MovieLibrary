package library.model;

import library.dao.DirectorDao;

import java.util.Objects;

public class Director {
    private int id;
    private String firstName;
    private String lastName;
    private String gender;
    private String nationality;

    public Director() {

    }

    public Director(String firstName, String lastName, String gender, String nationality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.nationality = nationality;
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

    public String getName() {
        return firstName + " " + lastName;
    }

    public Float getAverageRate() {
        DirectorDao dao = new DirectorDao();

        return dao.averageDirectorRate(this);
    }

    @Override
    public String toString() {
        return "Director{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", nationality='" + nationality + '\'' +
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
                Objects.equals(nationality, director.nationality);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstName, lastName, gender, nationality);
    }
}

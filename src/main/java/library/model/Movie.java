package library.model;

public class Movie {
    private int id;
    private String title;
    private Director director;
    private int releaseDate;
    private float rate;

    public Movie() {

    }

    public Movie(String title, Director director, int releaseDate, float rate) {
        this.title = title;
        this.director = director;
        this.releaseDate = releaseDate;
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public int getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(int releaseDate) {
        this.releaseDate = releaseDate;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }


    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", releaseDate=" + releaseDate +
                ", rate=" + rate +
                '}';
    }
}

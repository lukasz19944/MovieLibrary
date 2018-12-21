package library.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "movie_actor")
public class MovieActor implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name="movie_id")
    private Movie movie;

    @Id
    @ManyToOne
    @JoinColumn(name = "actor_id")
    private Actor actor;

    @JoinColumn(name = "rate")
    private float rate;

    public MovieActor() {

    }

    public MovieActor(Movie movie, Actor actor) {
        this.movie = movie;
        this.actor = actor;
    }

    public MovieActor(Movie movie, Actor actor, float rate) {
        this.movie = movie;
        this.actor = actor;
        this.rate = rate;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieActor that = (MovieActor) o;
        return Objects.equals(movie, that.movie) &&
                Objects.equals(actor, that.actor);
    }

    @Override
    public int hashCode() {

        return Objects.hash(movie, actor);
    }
}

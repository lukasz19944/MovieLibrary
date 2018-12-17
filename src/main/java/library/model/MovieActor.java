package library.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "movie_actor")
public class MovieActor implements Serializable {
    private Movie movie;
    private Actor actor;
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

    @Id
    @ManyToOne
    @JoinColumn(name="movie_id")
    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "actor_id")
    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    @JoinColumn(name = "rate")
    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}

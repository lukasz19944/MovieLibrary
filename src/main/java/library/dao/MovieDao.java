package library.dao;

import library.model.Movie;
import library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class MovieDao {

    public void addMovie(Movie movie) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            session.save(movie);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void deleteMovie(int movieId) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            Movie movie = (Movie) session.load(Movie.class, new Integer(movieId));
            session.delete(movie);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void updateMovie(Movie movie) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            session.update(movie);
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<Movie>();

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            movies = session.createQuery("from Movie").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return movies;
    }

    public Movie getMovieById(int movieId) {
        Movie movie = null;

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            String hql = "from Movie where id = :id";
            Query query = session.createQuery(hql);
            query.setInteger("id", movieId);
            movie = (Movie) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return movie;
    }

    public boolean movieExists(Movie movie) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            String hql = "from Movie where title = :title and director_id = :director " +
                         "and release_date = :rDate and rate = :rate";
            Query query = session.createQuery(hql);
            query.setString("title", movie.getTitle());
            query.setInteger("director", movie.getDirector().getId());
            query.setInteger("rDate", movie.getReleaseDate());
            query.setFloat("rate", movie.getRate());
            movie = (Movie) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        if (movie == null) {
            return false;
        }

        return true;
    }

    public void deleteAllMovies() {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            session.createQuery("delete from Movie").executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public String getAllGenres() {
        List<String> genres = null;

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("select genre from Movie");
            genres = query.getResultList();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return String.join(", ", genres);
    }

    public String getAllCountries() {
        List<String> countries = null;

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            Query query = session.createQuery("select distinct(country) from Movie");
            countries = query.getResultList();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return String.join(", ", countries);
    }
}

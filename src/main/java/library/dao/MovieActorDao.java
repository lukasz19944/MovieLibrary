package library.dao;

import library.model.Actor;
import library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.Set;

public class MovieActorDao {

    public Set<Actor> getActorsByMovie(int movieId) {
        Set<Actor> actors = null;

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            String hql = "select a from Actor a, Movie m, MovieActor ma " +
                    "where ma.movie = m.id and ma.actor = a.id and ma.movie = :id";
            Query query = session.createQuery(hql);
            query.setInteger("id", movieId);
            actors = new HashSet<>(query.list());
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return actors;
    }
}
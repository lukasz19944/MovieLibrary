package library.dao;

import library.model.Actor;
import library.model.MovieActor;
import library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.List;
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

    public void deleteActorFromMovie(int movieId, int actorId) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();

            String hql = "delete from MovieActor ma " +
                         " where ma.movie = :m and ma.actor = :a";
            Query query = session.createQuery(hql);
            query.setInteger("m", movieId);
            query.setInteger("a", actorId);
            query.executeUpdate();

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

    public void addActorToMovie(MovieActor movieActorRelation) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            session.save(movieActorRelation);
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

    public Float getActorRate(int movieId, int actorId) {
        Float rate = null;

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();

            String hql = "select rate from MovieActor ma " +
                    " where ma.movie = :m and ma.actor = :a";
            Query query = session.createQuery(hql);
            query.setInteger("m", movieId);
            query.setInteger("a", actorId);

            rate = (Float) query.getSingleResult();

            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return rate;
    }

    public void updateActorRate(int movieId, int actorId, float rate) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();

            String hql = "update from MovieActor ma " +
                    " set rate = :r " +
                    " where ma.movie = :m and ma.actor = :a";
            Query query = session.createQuery(hql);
            query.setInteger("m", movieId);
            query.setInteger("a", actorId);
            query.setFloat("r", rate);
            query.executeUpdate();

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

    public boolean relationExists(int movieId, int actorId) {
        MovieActor relation = null;

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            String hql = "from MovieActor where movie = :m and actor = :a";
            Query query = session.createQuery(hql);
            query.setInteger("m", movieId);
            query.setInteger("a", actorId);

            relation = (MovieActor) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        if (relation == null) {
            return false;
        }
        return true;
    }

    public void deleteAllActorsFromMovie(int movieId) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            String hql = "delete from MovieActor where movie = :m";
            Query query = session.createQuery(hql);
            query.setInteger("m", movieId);
            query.executeUpdate();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void deleteAllActorMovieRelation() {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            String hql = "delete from MovieActor";
            Query query = session.createQuery(hql);
            query.executeUpdate();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public Integer maxActorRateCount() {
        Long max = null;

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();

            String hql = "select count(rate) from MovieActor " +
                    "group by actor order by count(rate) desc";
            Query query = session.createQuery(hql);
            max = (Long) query.getResultList().get(0);

            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return max.intValue();
    }

    public List<MovieActor> getBestRoles(String gender) {
        List<MovieActor> maleRoles = null;

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        //

        try {
            transaction = session.beginTransaction();

            String hql = "from MovieActor m " +
                    "where m.actor.gender = :g " +
                    "order by m.rate desc";
            Query query = session.createQuery(hql, MovieActor.class);
            query.setString("g", gender);

            maleRoles = query.getResultList();

            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return maleRoles;
    }

}
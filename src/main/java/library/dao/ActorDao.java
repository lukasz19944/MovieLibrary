package library.dao;

import library.model.Actor;
import library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class ActorDao {

    public void addActor(Actor actor) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            session.save(actor);
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

    public void deleteActor(int actorId) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            Actor actor = (Actor) session.load(Actor.class, new Integer(actorId));
            session.delete(actor);
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

    public void updateActor(Actor actor) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            session.update(actor);
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

    public List<Actor> getAllActors() {
        List<Actor> actors = new ArrayList<>();

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            actors = session.createQuery("from Actor").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return actors;
    }

    public Actor getActorById(int actorId) {
        Actor actor = null;

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            String hql = "from Actor where id = :id";
            Query query = session.createQuery(hql);
            query.setInteger("id", actorId);
            actor = (Actor) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return actor;
    }

    public Actor getActorByName(String actorName) {
        Actor actor = null;

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            String hql = "from Actor where concat(first_name, ' ', last_name) = :name";
            Query query = session.createQuery(hql);
            query.setString("name", actorName);
            actor = (Actor) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return actor;
    }

    public boolean actorExists(Actor actor) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            String hql = "from Actor where first_name = :fName and last_name = :lName " +
                    "and gender = :gender and nationality = :nationality and date_of_birth = :dob";
            Query query = session.createQuery(hql);
            query.setString("fName", actor.getFirstName());
            query.setString("lName", actor.getLastName());
            query.setString("gender", actor.getGender());
            query.setString("nationality", actor.getNationality());
            query.setDate("dob", actor.getDateOfBirth());
            actor = (Actor) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        if (actor == null) {
            return false;
        }

        return true;
    }

    public void deleteAllActors() {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            session.createQuery("delete from Actor").executeUpdate();
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
}
package library.dao;

import library.model.Director;
import library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class DirectorDao {

    public void addDirector(Director director) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            session.save(director);
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

    public void deleteDirector(int directorId) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            Director director = (Director) session.load(Director.class, new Integer(directorId));
            session.delete(director);
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

    public void updateDirector(Director director) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            session.update(director);
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

    public List<Director> getAllDirectors() {
        List<Director> directors = new ArrayList<>();

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            directors = session.createQuery("from Director").list();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return directors;
    }

    public Director getDirectorById(int directorId) {
        Director director = null;

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            String hql = "from Director where id = :id";
            Query query = session.createQuery(hql);
            query.setInteger("id", directorId);
            director = (Director) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return director;
    }

    public Director getDirectorByName(String directorName) {
        Director director = null;

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            String hql = "from Director where concat(first_name, ' ', last_name) = :name";
            Query query = session.createQuery(hql);
            query.setString("name", directorName);
            director = (Director) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return director;
    }

    public boolean directorExists(Director director) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            String hql = "from Director where first_name = :fName and last_name = :lName " +
                         "and gender = :gender and nationality = :nationality";
            Query query = session.createQuery(hql);
            query.setString("fName", director.getFirstName());
            query.setString("lName", director.getLastName());
            query.setString("gender", director.getGender());
            query.setString("nationality", director.getNationality());
            director = (Director) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        if (director == null) {
            return false;
        }

        return true;
    }

    public void deleteAllDirectors() {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            session.createQuery("delete from Director").executeUpdate();
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

    public Float averageDirectorRate(Director director) {
        Double avgRate = null;

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            String hql = "select avg(rate) from Movie where director = :dir";
            Query query = session.createQuery(hql);
            query.setEntity("dir", director);
            avgRate = (Double) query.uniqueResult();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        if (avgRate != null) {
            return avgRate.floatValue();
        } else {
            return 0f;
        }
    }

    public String getAllNationalities() {
        List<String> nationalities = null;

        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();
            String hql = "select distinct(nationality) from Director";
            Query query = session.createQuery(hql);
            nationalities = query.getResultList();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return String.join(", ", nationalities);
    }
}

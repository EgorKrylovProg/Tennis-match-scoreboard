package Repository.Impl;

import Entity.Match;
import Repository.Interface.Dao;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;
import java.util.Optional;

@Log4j2
public class MatchDao implements Dao<Integer, Match> {

    private final SessionFactory sessionFactory;

    public MatchDao() {
        try {
            Context context = new InitialContext();

            sessionFactory = (SessionFactory) context.lookup("sessionFactory");
        } catch (NamingException e) {
            log.error("Ошибка при работе с JNDI контекстом: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Match> getAll() {
        List<Match> matches;
        try (Session session = sessionFactory.openSession()) {
           session.beginTransaction();

           matches = session.createSelectionQuery("FROM Matches", Match.class).list();

           session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Ошибка при работе с базой данных: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return matches;
    }

    @Override
    public Optional<Match> getById(Integer id) {
        Optional<Match> matchOptional;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            matchOptional = Optional.ofNullable(session.get(Match.class, id));

            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Ошибка при работе с базой данных: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return matchOptional;
    }

    @Override
    public void save(Match match) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.merge(match);

            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Ошибка при работе с базой данных: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.createMutationQuery("DELETE FROM Match WHERE id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Ошибка при работе с базой данных: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}

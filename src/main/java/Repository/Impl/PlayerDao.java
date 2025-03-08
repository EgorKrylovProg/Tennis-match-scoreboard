package Repository.Impl;

import Entity.Player;
import Repository.Interface.NamedEntityDao;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;
import java.util.Optional;

@Log4j2
public class PlayerDao implements NamedEntityDao<Integer, Player> {

    private final SessionFactory sessionFactory;

    public PlayerDao() {
        try {
            Context context = new InitialContext();

            sessionFactory = (SessionFactory) context.lookup("sessionFactory");
        } catch (NamingException e) {
            log.error("Ошибка при работе с JNDI контекстом: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Player> getAll() {
        List<Player> players;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            players = session.createSelectionQuery("FROM Player p", Player.class).list();

            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Ошибка при работе c базой данных: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return players;
    }

    @Override
    public Optional<Player> getById(Integer id) {
        Optional<Player> optionalPlayer;

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            optionalPlayer = Optional.ofNullable(session.get(Player.class, id));

            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Ошибка при работе c базой данных: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return optionalPlayer;
    }

    @Override
    public Optional<Player> getByName(String name) {
        Optional<Player> optionalPlayer;

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            optionalPlayer = Optional.ofNullable(
                    session.createSelectionQuery("FROM Player WHERE name= :name", Player.class)
                            .setParameter("name", name)
                            .uniqueResult()
            );

            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Ошибка при работе c базой данных: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return optionalPlayer;
    }

    @Override
    public void save(Player player) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.merge(player);

            session.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            log.error("Попытка сохранить в базу данных пользователя с уже существующим именем - {}", player.getName());
        } catch (Exception e) {
            log.error("Ошибка при работе c базой данных: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Integer id) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.createMutationQuery("delete from Player where id = :id")
                    .setParameter("id", id)
                    .executeUpdate();

            session.getTransaction().commit();
        } catch (Exception e) {
            log.error("Ошибка при работе c базой данных: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }

}

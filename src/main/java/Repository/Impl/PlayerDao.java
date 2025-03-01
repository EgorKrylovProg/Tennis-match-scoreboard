package Repository.Impl;

import Entity.Player;
import Repository.Interface.DAO;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;
import java.util.Optional;

@Log4j2
public class PlayerDao implements DAO<Integer, Player> {

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
    public void save(Player player) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Player existingPlayer = session.createQuery("FROM Player WHERE name = :name", Player.class)
                            .setParameter("name", player.getName())
                            .uniqueResult();

            if (existingPlayer != null) {
                return;
            }

            session.merge(player);

            session.getTransaction().commit();
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

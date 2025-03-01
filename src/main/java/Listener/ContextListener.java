package Listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.log4j.Log4j2;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

@WebListener
@Log4j2
public class ContextListener implements ServletContextListener {

    private Context context;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        try {
            context = new InitialContext();
            context.bind("sessionFactory", getSessionFactory());

            log.info("Инициализирован JNDI контекст.");
        } catch (NamingException e) {
            log.error("Ошибка при создании JNDI контекста: {}", e.getMessage());
            throw new RuntimeException(e);
        }


    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            SessionFactory sessionFactory = (SessionFactory) context.lookup("sessionFactory");
            if (sessionFactory != null) {
                sessionFactory.close();
                log.info("Освобождены ресурсы Hibernate SessionFactory.");
            }
            if (context != null) {
                context.close();
                log.info("Освобождены ресурсы JNDI контекста.");
            }
        } catch (NamingException e) {
            log.error("Ошибка при освобождении ресурсов JNDI контекста: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration().configure();
        Properties properties = configuration.getProperties();

        SessionFactory sessionFactory = configuration.buildSessionFactory();

        log.info("Инициализирован Hibernate SessionFactory. URL базы данных: {}",
                properties.getProperty("hibernate.connection.url"));

        return sessionFactory;
    }
}

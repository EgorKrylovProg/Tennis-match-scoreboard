package Repository.Interface;

import java.util.List;
import java.util.Optional;

public interface DAO <T, R> {

    List<R> getAll();
    Optional<R> getById(T t);
    void save(R r);
    void delete(T t);

}

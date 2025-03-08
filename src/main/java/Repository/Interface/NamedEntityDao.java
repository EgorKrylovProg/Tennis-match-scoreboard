package Repository.Interface;

import java.util.Optional;

public interface NamedEntityDao<T, R> extends Dao<T, R> {

    Optional<R> getByName(String name);

}

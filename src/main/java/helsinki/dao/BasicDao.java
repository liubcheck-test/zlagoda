package helsinki.dao;

import java.util.List;
import java.util.Optional;

public interface BasicDao<T, Id> {
    T save(T t);

    Optional<T> get(Id id);

    List<T> getAll();

    T update(T t);

    boolean delete(Id id);
}

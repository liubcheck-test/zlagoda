package helsinki.dao;

import java.util.List;
import java.util.Optional;

public interface BasicDao<T, I> {
    T save(T t);

    Optional<T> get(I id);

    List<T> getAll();

    T update(T t);

    boolean delete(I id);
}

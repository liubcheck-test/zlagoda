package helsinki.service;

import java.util.List;

public interface BasicService<T, I> {
    T create(T t);

    T get(I id);

    List<T> getAll();

    T update(T t);

    boolean delete(I id);
}

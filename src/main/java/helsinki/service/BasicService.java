package helsinki.service;

import java.util.List;

public interface BasicService<T, Id> {
    T create(T t);

    T get(Id id);

    List<T> getAll();

    T update(T t);

    boolean delete(Id id);
}

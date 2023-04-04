package helsinki.dao;

import helsinki.model.Check;
import java.util.List;
import java.util.Optional;

public interface CheckDao {
    Check save(Check check);

    Optional<Check> get(String number);

    List<Check> getAll();

    Check update(Check check);

    boolean delete(String number);
}

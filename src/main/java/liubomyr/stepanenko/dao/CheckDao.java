package liubomyr.stepanenko.dao;

import java.util.List;
import java.util.Optional;
import liubomyr.stepanenko.model.Check;

public interface CheckDao {
    Check save(Check check);

    Optional<Check> get(String number);

    List<Check> getAll();

    Check update(Check check);

    boolean delete(String number);
}

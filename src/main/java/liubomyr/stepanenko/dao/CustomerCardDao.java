package liubomyr.stepanenko.dao;

import java.util.List;
import java.util.Optional;
import liubomyr.stepanenko.model.CustomerCard;

public interface CustomerCardDao {
    CustomerCard save(CustomerCard card);

    Optional<CustomerCard> get(String number);

    List<CustomerCard> getAll();

    CustomerCard update(CustomerCard card);

    boolean delete(String number);
}

package helsinki.dao;

import helsinki.model.CustomerCard;
import java.util.List;
import java.util.Optional;

public interface CustomerCardDao {
    CustomerCard save(CustomerCard card);

    Optional<CustomerCard> get(String number);

    List<CustomerCard> getAll();

    CustomerCard update(CustomerCard card);

    boolean delete(String number);
}

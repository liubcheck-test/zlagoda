package helsinki.dao;

import helsinki.model.CustomerCard;
import java.math.BigDecimal;
import java.util.List;

public interface CustomerCardDao extends BasicDao<CustomerCard, String> {
    List<CustomerCard> getAllSortedBySurname();

    List<CustomerCard> getAllByPercentSortedBySurname(Integer percent);

    List<CustomerCard> getAllByLastName(String lastName);

    List<CustomerCard> requestCustomer(BigDecimal inputSum);
}

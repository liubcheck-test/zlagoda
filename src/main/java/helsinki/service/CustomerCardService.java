package helsinki.service;

import helsinki.model.CustomerCard;
import java.math.BigDecimal;
import java.util.List;

public interface CustomerCardService extends BasicService<CustomerCard, String> {
    List<CustomerCard> getAllSortedBySurname();

    List<CustomerCard> getAllByPercentSortedBySurname(Integer percent);

    List<CustomerCard> getAllByLastName(String lastName);

    List<CustomerCard> requestCustomer(BigDecimal inputSum);
}

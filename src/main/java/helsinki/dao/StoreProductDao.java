package helsinki.dao;

import helsinki.model.StoreProduct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface StoreProductDao extends BasicDao<StoreProduct, String> {
    List<StoreProduct> getAllSortedByAmount();

    Map<String, Object> getStoreProductDataByUpc(String upc);

    List<StoreProduct> getAllNonPromotionalSortedByAmountAndName();
}

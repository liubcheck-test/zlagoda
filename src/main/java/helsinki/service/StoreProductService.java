package helsinki.service;

import helsinki.model.StoreProduct;

import java.util.List;
import java.util.Map;

public interface StoreProductService extends BasicService<StoreProduct, String> {

    List<StoreProduct> getAllSortedByAmount();

    Map<String, Object> getStoreProductDataByUpc(String upc);

    Map<String, Object> getStoreProductPriceAndAmountByUpc(String upc);

    List<StoreProduct> getAllPromotionalSortedByAmountAndName();

    List<StoreProduct> getAllNonPromotionalSortedByAmountAndName();
}

package helsinki.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
    private StoreProduct storeProduct;
    private Check check;
    private Integer productNumber;
    private BigDecimal sellingPrice;
}

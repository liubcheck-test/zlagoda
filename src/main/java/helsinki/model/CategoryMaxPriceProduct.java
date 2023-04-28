package helsinki.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMaxPriceProduct {
    private int categoryNumber;
    private String categoryName;
    private long idProduct;
    private String productName;
    private BigDecimal sellingPrice;
}
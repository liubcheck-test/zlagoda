package helsinki.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStat {
    private Long idProduct;
    private String productName;
    private String characteristics;
    private Integer totalNumber;
    private BigDecimal totalSum;
}
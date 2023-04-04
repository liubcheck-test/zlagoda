package liubomyr.stepanenko.model;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreProduct {
    @Pattern(regexp = "\\d{12}")
    private String upc;
    private String upcProm;
    private Product product;
    @Positive
    private BigDecimal price;
    @Positive
    private Integer amount;
    @NotNull
    private Boolean isPromotional;
}

package helsinki.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Check {
    @Pattern(regexp = "\\d{9}")
    private String checkNumber;
    private Employee employee;
    private CustomerCard customerCard;
    @PastOrPresent
    private LocalDateTime printDate;
    @Positive
    private BigDecimal sumTotal;
    @Positive
    private BigDecimal vat;

    public Check(String number, Employee employee, CustomerCard customerCard,
                 LocalDateTime printDate, BigDecimal sumTotal) {
        this.checkNumber = number;
        this.employee = employee;
        this.customerCard = customerCard;
        this.printDate = printDate;
        this.sumTotal = sumTotal;
        this.vat = sumTotal.multiply(BigDecimal.valueOf(0.2)).setScale(2, RoundingMode.HALF_UP);
    }
}

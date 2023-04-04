package liubomyr.stepanenko.model;

import javax.validation.constraints.Pattern;
import liubomyr.stepanenko.model.composite.Address;
import liubomyr.stepanenko.model.composite.FullName;
import liubomyr.stepanenko.validation.IsFullName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCard {
    private String cardNumber;
    @IsFullName
    private FullName customerFullName;
    @Pattern(regexp = "^\\+380\\s(39|50|63|66|67|68|73|"
            + "91|92|93|94|95|96|97|98|99)\\s\\d{3}\\s\\d{2}\\s\\d{2}$\n")
    private String phoneNumber;
    private Address customerAddress;
    private Integer percent;
}

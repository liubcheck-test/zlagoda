package helsinki.model.composite;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @NotEmpty
    private String city;
    @NotEmpty
    private String street;
    @Pattern(regexp = "\\d{5}(-\\d{4})?")
    private String zipCode;
}

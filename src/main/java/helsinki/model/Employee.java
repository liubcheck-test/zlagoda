package helsinki.model;

import helsinki.model.composite.Address;
import helsinki.model.composite.FullName;
import helsinki.validation.IsFullName;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private String id;
    @IsFullName
    private FullName employeeFullName;
    private Role employeeRole;
    @Positive
    private BigDecimal salary;
    private LocalDate dateOfBirth;
    private LocalDate dateOfStart;
    @Pattern(regexp = "^\\+380\\s(39|50|63|66|67|68|73|"
            + "91|92|93|94|95|96|97|98|99)\\s\\d{3}\\s\\d{2}\\s\\d{2}$\n")
    private String phoneNumber;
    private Address address;
    private String email;
    private String password;

    public enum Role {
        MANAGER("Manager"),
        CASHIER("Cashier");

        private final String name;

        Role(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}

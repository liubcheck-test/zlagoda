package helsinki.model;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long id;
    private Category category;
    @NotEmpty
    private String name;
    @NotEmpty
    private String characteristics;

    public String toString() {
        return name;
    }
}

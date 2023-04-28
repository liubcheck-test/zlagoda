package helsinki.model;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Integer categoryNumber;
    @NotEmpty
    private String categoryName;

    public String toString() {
        return categoryNumber + " " + categoryName;
    }
}

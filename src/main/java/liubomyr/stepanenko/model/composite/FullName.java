package liubomyr.stepanenko.model.composite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FullName {
    private String surname;
    private String name;
    private String patronymic;
}

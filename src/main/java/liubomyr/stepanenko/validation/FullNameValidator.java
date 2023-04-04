package liubomyr.stepanenko.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import liubomyr.stepanenko.model.composite.FullName;

public class FullNameValidator implements ConstraintValidator<IsFullName, FullName> {
    private static final String SURNAME_PATTERN = "[\\p{Lu}\\p{Ll}]{2,}[\\s\\p{Pd}]";
    private static final String NAME_PATTERN = "[\\p{Lu}\\p{Ll}]+";
    private static final String PATRONYMIC_PATTERN
            = "(\\s[\\p{Lu}\\p{Ll}]+)?(\\s[\\p{Lu}\\p{Ll}]+)?$";

    @Override
    public boolean isValid(FullName fullName,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (fullName == null) {
            return false;
        }
        Pattern surnamePattern = Pattern.compile(SURNAME_PATTERN);
        Matcher surnameMatcher = surnamePattern.matcher(fullName.getSurname());
        Pattern namePattern = Pattern.compile(NAME_PATTERN);
        Matcher nameMatcher = namePattern.matcher(fullName.getName());
        if (fullName.getPatronymic() != null) {
            Pattern patronymicPattern = Pattern.compile(PATRONYMIC_PATTERN);
            Matcher patronymicMatcher = patronymicPattern.matcher(fullName.getPatronymic());
            return surnameMatcher.matches()
                    && nameMatcher.matches()
                    && patronymicMatcher.matches();
        }
        return surnameMatcher.matches() && nameMatcher.matches();
    }
}

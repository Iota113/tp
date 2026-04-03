package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should contain only English characters, with words separated by a single space or '/', "
                    + "e.g. 'Tan Ah Kow' or 'Raj S/O Kumar'. "
                    + "Names must not start or end with a space or '/', "
                    + "and must not contain consecutive spaces or '/' characters";

    /*
     * 1. Must start with a letter: [a-zA-Z]
     * 2. Can be followed by sequences of a single space/slash and a letter: ([ /][a-zA-Z])*
     * 3. This ensures no consecutive symbols and no trailing symbols.
     */
    public static final String VALIDATION_REGEX = "^[a-zA-Z]+(([ /][a-zA-Z])?[a-zA-Z]*)*$";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        name = name.trim();
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    /**
     * Returns true if two names are the same (case-insensitive)
     */
    public boolean isSameName(Name otherName) {
        if (otherName == this) {
            return true;
        }

        return this.fullName.equalsIgnoreCase(otherName.fullName);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}

package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Arrays;

/**
 * Represents a Day in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDay(String)}
 * value of day is stored internally in upper case.
 */
public class Day {

    public static final String MESSAGE_CONSTRAINTS =
            "Lesson Day should only be a day of the week.";

    public final String value;

    /**
     * Constructs a {@code Day}.
     * Convets String day to lowercase.
     *
     * @param day A valid tuition day.
     */
    public Day(String day) {
        requireNonNull(day);
        checkArgument(isValidDay(day), MESSAGE_CONSTRAINTS);
        value = day.toUpperCase();
    }

    /**
     * Returns true if a given string is a valid tuition rate.
     */
    public static boolean isValidDay(String test) {
        return DayOfWeek.isValid(test);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof seedu.address.model.person.Day)) {
            return false;
        }

        seedu.address.model.person.Day otherDay = (seedu.address.model.person.Day) other;
        return value.equals(otherDay.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Represents the valid days of the week.
     */
    public enum DayOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

        /**
         * Returns true if a given string matches any of the enum names (case-insensitive).
         */
        public static boolean isValid(String test) {
            return Arrays.stream(Day.DayOfWeek.values())
                    .anyMatch(day -> day.name().equalsIgnoreCase(test));
        }
    }


}

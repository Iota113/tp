package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's lesson time in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTime(String)}
 */
public class Time {

    public static final String MESSAGE_CONSTRAINTS =
            "Lesson Time should be in 24-hour (HH:mm) format e.g. 18:00. "
            + "End time must be after start time.";
    public static final String VALIDATION_REGEX = "^([0-1][0-9]|2[0-3]):[0-5][0-9]$";
    public final String value;

    /**
     * Constructs a {@code LessonTime}.
     *
     * @param lessonTime A valid LessonTime number.
     */
    public Time(String lessonTime) {
        requireNonNull(lessonTime);
        checkArgument(isValidTime(lessonTime), MESSAGE_CONSTRAINTS);
        value = lessonTime;
    }

    /**
     * Returns true if a given string is a valid LessonTime number.
     */
    public static boolean isValidTime(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if this time occurs after a given time.
     * @param other the other {@code Time} to compare against
     * @return {@code true} if this time is strictly after {@code other}, {@code false} otherwise
     */
    public boolean isAfter(Time other) {
        String[] thisParts = value.split(":");
        String [] otherParts = other.value.split(":");
        int thisHour = Integer.parseInt(thisParts[0]);
        int thisMin = Integer.parseInt(thisParts[1]);
        int otherHour = Integer.parseInt(otherParts[0]);
        int otherMin = Integer.parseInt(otherParts[1]);

        return thisHour > otherHour || (thisHour == otherHour && thisMin > otherMin);
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
        if (!(other instanceof Time)) {
            return false;
        }

        Time otherTime = (Time) other;
        return value.equals(otherTime.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

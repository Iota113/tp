package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class DayTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Day(null));
    }

    @Test
    public void constructor_invalidDay_throwsIllegalArgumentException() {
        String invalidDay = "Not a Day";
        assertThrows(IllegalArgumentException.class, () -> new Day(invalidDay));
    }

    @Test
    public void isValidDay() {
        // invalid Day numbers
        assertFalse(Day.isValidDay("")); // empty string
        assertFalse(Day.isValidDay(" ")); // spaces only
        assertFalse(Day.isValidDay("91")); // numeric
        assertFalse(Day.isValidDay("Day")); // non-numeric
        assertFalse(Day.isValidDay("9011p041")); // alphabets within digits
        assertFalse(Day.isValidDay("Wedne sday")); // spaces within characters

        // valid Days
        assertTrue(Day.isValidDay("Monday"));
        assertTrue(Day.isValidDay("Tuesday"));
        assertTrue(Day.isValidDay("Wednesday"));
        assertTrue(Day.isValidDay("Thursday"));
        assertTrue(Day.isValidDay("Friday"));
        assertTrue(Day.isValidDay("Saturday"));
        assertTrue(Day.isValidDay("Sunday"));

        // case-insensitve tests
        assertTrue(Day.isValidDay("sunday"));
        assertTrue(Day.isValidDay("SUNDAY"));
        assertTrue(Day.isValidDay("SuNdAy"));
    }

    @Test
    public void equals() {
        Day day = new Day("Friday");

        // same values -> returns true
        assertTrue(day.equals(new Day("Friday")));

        // case-insensitive test
        assertTrue(day.equals(new Day("friday")));

        // same object -> returns true
        assertTrue(day.equals(day));

        // null -> returns false
        assertFalse(day.equals(null));

        // different types -> returns false
        assertFalse(day.equals(5.0f));

        // different values -> returns false
        assertFalse(day.equals(new Day("Monday")));
    }
}

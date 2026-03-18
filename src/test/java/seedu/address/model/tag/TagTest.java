package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));
    }

    @Test
    public void constructor_mixedCase_normalizesToLowerCase() {
        Tag mixedCaseTag = new Tag("FrIeNdS");

        assertEquals("friends", mixedCaseTag.tagName);
    }

    @Test
    public void equals_sameTagNameDifferentCase_returnsTrue() {
        Tag lowerCaseTag = new Tag("friends");
        Tag upperCaseTag = new Tag("FRIENDS");

        assertEquals(lowerCaseTag, upperCaseTag);
        assertEquals(lowerCaseTag.hashCode(), upperCaseTag.hashCode());
    }

    @Test
    public void equals_differentTagNames_returnsFalse() {
        Tag firstTag = new Tag("friends");
        Tag secondTag = new Tag("teammate");

        assertNotEquals(firstTag, secondTag);
    }

}

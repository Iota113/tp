package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

import java.util.HashSet;

class TagsContainKeywordsPredicateTest {
    @Test
    void test_singleKeywordMatch() {
        TagsContainKeywordsPredicate predicate = new TagsContainKeywordsPredicate(Collections.singletonList("friends"));
        Person person = new PersonBuilder().withTags("friends").build();
        assertTrue(predicate.test(person));
    }

    @Test
    void test_singleKeywordNoMatch() {
        TagsContainKeywordsPredicate predicate = new TagsContainKeywordsPredicate(Collections.singletonList("colleagues"));
        Person person = new PersonBuilder().withTags("friends").build();
        assertFalse(predicate.test(person));
    }

    @Test
    void test_multipleKeywordsAllMatch() {
        TagsContainKeywordsPredicate predicate = new TagsContainKeywordsPredicate(Arrays.asList("friends", "colleagues"));
        Person person = new PersonBuilder().withTags("friends", "colleagues").build();
        assertTrue(predicate.test(person));
    }

    @Test
    void test_multipleKeywordsPartialMatch() {
        TagsContainKeywordsPredicate predicate = new TagsContainKeywordsPredicate(Arrays.asList("friends", "colleagues"));
        Person person = new PersonBuilder().withTags("friends").build();
        assertFalse(predicate.test(person));
    }
}

package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DAY_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DAY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_END_TIME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_END_TIME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_RATE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_RATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_START_TIME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_START_TIME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("94351253").withDay("Sunday")
            .withStartTime("09:00").withEndTime("11:00").withRate("40")
            .withTags("friends").build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withEmail("johnd@example.com").withPhone("98765432").withDay("Friday")
            .withStartTime("20:00").withEndTime("22:00").withRate("50")
            .withTags("owesMoney", "friends").build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com").withAddress("wall street")
            .withDay("Monday").withStartTime("10:00").withEndTime("12:00").withRate("40")
            .build();

    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com").withAddress("10th street").withTags("friends")
            .withDay("Tuesday").withStartTime("10:00").withEndTime("12:00").withRate("40")
            .build();

    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer").withPhone("94822245")
            .withEmail("werner@example.com").withAddress("michegan ave")
            .withDay("Wednesday").withStartTime("10:00").withEndTime("12:00").withRate("40")
            .build();

    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz").withPhone("94824275")
            .withEmail("lydia@example.com").withAddress("little tokyo")
            .withDay("Thursday").withStartTime("10:00").withEndTime("12:00").withRate("40")
            .build();

    public static final Person GEORGE = new PersonBuilder().withName("George Best").withPhone("94824422")
            .withEmail("anna@example.com").withAddress("4th street")
            .withDay("Saturday").withStartTime("10:00").withEndTime("12:00").withRate("40")
            .build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withPhone("84824245")
            .withEmail("stefan@example.com").withAddress("little india")
            .withDay("Monday").withStartTime("14:00").withEndTime("16:00").withRate("45")
            .build();

    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withPhone("84821315")
            .withEmail("hans@example.com").withAddress("chicago ave")
            .withDay("Tuesday").withStartTime("14:00").withEndTime("16:00").withRate("45")
            .build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY).withTags(VALID_TAG_FRIEND)
            .withDay(VALID_DAY_AMY).withStartTime(VALID_START_TIME_AMY).withEndTime(VALID_END_TIME_AMY)
            .withRate(VALID_RATE_AMY).build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .withDay(VALID_DAY_BOB).withStartTime(VALID_START_TIME_BOB).withEndTime(VALID_END_TIME_BOB)
            .withRate(VALID_RATE_BOB).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}

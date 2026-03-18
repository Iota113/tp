package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Day day;
    private final Time startTime;
    private final Time endTime;
    private final Rate rate;
    private final boolean isPaid;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null. Accepts an explicit {@code isPaid} status.
     * Used when constructing a Person with a known payment state (e.g. Mark/Unmark commands).
     */
    public Person(Name name, Phone phone, Email email, Address address, Day day,
                  Time startTime, Time endTime, Rate rate, boolean isPaid, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, day, startTime, endTime, rate, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.rate = rate;
        this.isPaid = isPaid;
        this.tags.addAll(tags);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Day getDay() {
        return day;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public Rate getRate() {
        return rate;
    }

    public boolean isPaid() {
        return isPaid;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().isSameName(getName())
                && otherPerson.getPhone().equals(getPhone());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && day.equals(otherPerson.day)
                && startTime.equals(otherPerson.startTime)
                && endTime.equals(otherPerson.endTime)
                && rate.equals(otherPerson.rate)
                && isPaid == otherPerson.isPaid
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, day, startTime, endTime,
                rate, isPaid, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("day", day)
                .add("startTime", startTime)
                .add("endTime", endTime)
                .add("rate", rate)
                .add("isPaid", isPaid)
                .add("tags", tags)
                .toString();
    }

}

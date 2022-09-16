package com.bobocode.se;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * A generic comparator that is comparing a random field of the given class. The field is either primitive or
 * {@link Comparable}. It is chosen during comparator instance creation and is used for all comparisons.
 * <p>
 * If no field is available to compare, the constructor throws {@link IllegalArgumentException}
 *
 * @param <T> the type of the objects that may be compared by this comparator
 *<p><p>
 *  <strong>TODO: to get the most out of your learning, <a href="https://www.bobocode.com/learn">visit our website</a></strong>
 *  <p>
 *
 * @author Stanislav Zabramnyi
 */
public class RandomFieldComparator<T> implements Comparator<T> {

    private final Field field;
    private final Class<T> clazz;

    public RandomFieldComparator(Class<T> targetType) {
        Objects.requireNonNull(targetType);
        this.clazz = targetType;
        this.field = Arrays.stream(targetType.getDeclaredFields())
            .filter(this::isComparable)
            .findAny()
            .orElseThrow(IllegalArgumentException::new);
    }

    private boolean isComparable(Field f) {
        return f.getClass().isPrimitive() || Arrays.asList(f.getType().getInterfaces()).contains(Comparable.class);
    }

    /**
     * Compares two objects of the class T by the value of the field that was randomly chosen. It allows null values
     * for the fields, and it treats null value greater than a non-null value.
     *
     * @param o1
     * @param o2
     * @return positive int in case of first parameter {@param o1} is greater than second one {@param o2},
     *         zero if objects are equals,
     *         negative int in case of first parameter {@param o1} is less than second one {@param o2}.
     */
    @Override
    public int compare(T o1, T o2) {
        Objects.requireNonNull(o1);
        Objects.requireNonNull(o2);
        return compareValues(o1, o2);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private <U extends Comparable<? super U>> int compareValues(T o1, T o2) {
        field.setAccessible(true);
        var value1 = (U) field.get(o1);
        var value2 = (U) field.get(o2);
        Comparator<U> comparator = Comparator.nullsLast(Comparator.naturalOrder());
        return comparator.compare(value1, value2);
    }

    /**
     * Returns the name of the randomly-chosen comparing field.
     */
    public String getComparingFieldName() {
        return field.getName();
    }

    /**
     * Returns a statement "Random field comparator of class '%s' is comparing '%s'" where the first param is the name
     * of the type T, and the second parameter is the comparing field name.
     *
     * @return a predefined statement
     */
    @Override
    public String toString() {
        return "Random field comparator of class '" + clazz.getSimpleName() + "' is comparing '" + field.getName() + "'";
    }
}

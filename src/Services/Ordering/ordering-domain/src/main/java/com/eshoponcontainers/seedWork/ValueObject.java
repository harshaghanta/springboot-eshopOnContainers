package com.eshoponcontainers.seedWork;

import java.util.Collection;
import java.util.Objects;

public abstract class ValueObject {

    protected static boolean equalOperator(ValueObject left, ValueObject right) {

        if ((left == null && right == null) || (left != null && right != null && left.equals(right)))
            return true;

        return false;
    }

    protected static boolean notEqualOperator(ValueObject left, ValueObject right) {
        return !(equalOperator(left, right));
    }

    protected abstract Collection<Object> getEqualityComponents();

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass())
            return false;

        ValueObject other = (ValueObject) obj;
        return Objects.equals(this.getEqualityComponents(), other.getEqualityComponents());
    }

    @Override
    public int hashCode() {
        return getEqualityComponents().stream().map(x -> x != null ? x.hashCode() : 0).reduce(0, (x, y) -> x ^ y);
    }

    public ValueObject getCopy() {
        try {
            return (ValueObject) clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}

package org.example.com.family.accounting.utils;

public enum Gender {
    M(MessageSourceHelper.getMessage("gender.male")),
    F(MessageSourceHelper.getMessage("gender.female"));

    private final String stringValue;

    Gender(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    public static Gender fromString(String stringValue) {
        for (Gender s: Gender.values()) {
            if (s.toString().equals(stringValue)) {
                return s;
            }
        }

        throw new IllegalArgumentException(stringValue);
    }
}

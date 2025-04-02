package ca.ulaval.glo2003.customer.logic;

public class Email {
    private final String value;

    public Email(String value) {
        if (!value.matches(".+@.+\\...+")) {
            throw new IllegalArgumentException("Invalid value format");
        }

        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

package ca.ulaval.glo2003.customer.logic;

public enum EmailType {
    WORK,
    PERSONAL;

    public static EmailType parse(String value) {
        return EmailType.valueOf(value.toUpperCase());
    }
}

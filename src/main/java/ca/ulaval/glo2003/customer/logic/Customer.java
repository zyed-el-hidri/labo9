package ca.ulaval.glo2003.customer.logic;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record Customer(
        String id,
        String name,
        LocalDate birthDate,
        Map<EmailType, Set<Email>> emails
) {
    public int age() {
        return (int)ChronoUnit.YEARS.between(birthDate, LocalDate.now());
    }

    public Set<Email> emailsFor(EmailType type) {
        return emails.getOrDefault(type, Collections.emptySet());
    }
}

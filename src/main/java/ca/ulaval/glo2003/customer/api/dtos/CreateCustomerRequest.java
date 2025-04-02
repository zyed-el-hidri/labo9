package ca.ulaval.glo2003.customer.api.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DTO pour créer un nouveau client
 */
public class CreateCustomerRequest {
    public String name;
    public LocalDate birthDate;
    public Map<String, List<String>> emails;
}
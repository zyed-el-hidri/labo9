package ca.ulaval.glo2003.customer.api.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DTO pour mettre à jour un client existant
 */
public class UpdateCustomerRequest {
    public String name;
    public LocalDate birthDate;
    public Map<String, List<String>> emails;
}
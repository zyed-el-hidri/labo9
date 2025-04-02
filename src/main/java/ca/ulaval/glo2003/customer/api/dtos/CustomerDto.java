package ca.ulaval.glo2003.customer.api.dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * DTO pour les données client renvoyées par l'API
 */
public class CustomerDto {
    public String id;
    public String name;
    public LocalDate birthDate;
    public int age;
    public Map<String, List<String>> emails;
}
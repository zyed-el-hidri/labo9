package ca.ulaval.glo2003.customer.logic;

import java.util.List;

public interface CustomerPersistence {
    List<Customer> listAll();
    void save(Customer customer);
}

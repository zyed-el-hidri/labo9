package ca.ulaval.glo2003.customer.infra;

import ca.ulaval.glo2003.customer.logic.Customer;
import ca.ulaval.glo2003.customer.logic.CustomerPersistence;
import ca.ulaval.glo2003.customer.logic.Email;
import ca.ulaval.glo2003.customer.logic.EmailType;
import dev.morphia.Datastore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomerPersistenceMongo implements CustomerPersistence {
    private final Datastore datastore;

    public CustomerPersistenceMongo(Datastore datastore) {
        this.datastore = datastore; // Initialisation de datastore
    }

@Override
public List<Customer> listAll() {
    List<CustomerMongo> mongoCustomers = datastore.find(CustomerMongo.class).iterator().toList();
    return mongoCustomers.stream()
        .map(this::convertToCustomer)
        .collect(Collectors.toList());
}

@Override
public void save(Customer customer) {
    CustomerMongo mongoCustomer = convertToMongoCustomer(customer);
    datastore.save(mongoCustomer);
}

private CustomerMongo convertToMongoCustomer(Customer customer) {
    CustomerMongo mongoCustomer = new CustomerMongo(
        customer.id(),
        customer.name(),
        customer.birthDate().toString()
    );
    
    // Convertir tous les emails en une liste simple
    for (Map.Entry<EmailType, Set<Email>> entry : customer.emails().entrySet()) {
        for (Email email : entry.getValue()) {
            mongoCustomer.emails.add(email.toString());
        }
    }
    
    return mongoCustomer;
}

private Customer convertToCustomer(CustomerMongo mongoCustomer) {
    String id = mongoCustomer.id;
    String name = mongoCustomer.name;
    LocalDate birthDate = LocalDate.parse(mongoCustomer.birthDate);
    
    // Dans cette implémentation simplifiée, nous ne reconstruisons pas la structure des emails
    Map<EmailType, Set<Email>> emails = new HashMap<>();
    
    return new Customer(id, name, birthDate, emails);
}
}
package ca.ulaval.glo2003.customer.infra;

import ca.ulaval.glo2003.customer.logic.Customer;
import ca.ulaval.glo2003.customer.logic.CustomerPersistence;
import ca.ulaval.glo2003.customer.logic.Email;
import ca.ulaval.glo2003.customer.logic.EmailType;
import dev.morphia.Datastore;

import java.time.LocalDate;
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
        // Récupère tous les documents CustomerMongo de la base de données
        List<CustomerMongo> mongoCustomers = datastore.find(CustomerMongo.class).iterator().toList();
        
        // Convertit chaque CustomerMongo en Customer et retourne la liste
        return mongoCustomers.stream()
            .map(this::convertToCustomer)
            .collect(Collectors.toList());
    }

    @Override
    public void save(Customer customer) {
        System.out.println("Sauvegarde dans MongoDB: " + customer.id());
        
        // Convertit l'objet Customer en CustomerMongo pour le stockage
        CustomerMongo mongoCustomer = convertToMongoCustomer(customer);
        
        // Sauvegarde l'objet dans MongoDB
        datastore.save(mongoCustomer);
    }

    /**
     * Convertit un objet Customer du domaine en objet CustomerMongo pour la persistance.
     * Cette méthode préserve les types d'emails en les stockant dans une structure Map.
     */
    private CustomerMongo convertToMongoCustomer(Customer customer) {
        // Crée un nouvel objet CustomerMongo avec les propriétés de base
        CustomerMongo mongoCustomer = new CustomerMongo(
            customer.id(),
            customer.name(),
            customer.birthDate().toString()
        );
        
        // Pour chaque type d'email et ensemble d'emails associés
        for (Map.Entry<EmailType, Set<Email>> entry : customer.emails().entrySet()) {
            // Obtient le type d'email sous forme de chaîne (ex: "WORK", "PERSONAL")
            String emailType = entry.getKey().toString();
            
            // Pour chaque email de ce type
            for (Email email : entry.getValue()) {
                // Ajoute l'email à la map des emails du CustomerMongo, classé par type
                mongoCustomer.addEmail(emailType, email.toString());
            }
        }
        
        return mongoCustomer;
    }

    /**
     * Convertit un objet CustomerMongo de la base de données en objet Customer du domaine.
     * Cette méthode reconstruit la structure des emails avec leurs types appropriés.
     */
    private Customer convertToCustomer(CustomerMongo mongoCustomer) {
        // Récupère les propriétés de base
        String id = mongoCustomer.id;
        String name = mongoCustomer.name;
        LocalDate birthDate = LocalDate.parse(mongoCustomer.birthDate);
        
        // Crée un map pour stocker les emails par type
        Map<EmailType, Set<Email>> emails = new HashMap<>();
        
        // Si la map des emails dans mongoCustomer n'est pas vide
        if (mongoCustomer.emails != null && !mongoCustomer.emails.isEmpty()) {
            // Pour chaque type d'email et liste d'emails associés
            for (Map.Entry<String, List<String>> entry : mongoCustomer.emails.entrySet()) {
                try {
                    // Convertit la chaîne de type en objet EmailType
                    EmailType emailType = EmailType.parse(entry.getKey());
                    
                    // Crée un ensemble pour stocker les emails de ce type
                    Set<Email> emailSet = new HashSet<>();
                    
                    // Pour chaque adresse email de ce type
                    for (String emailStr : entry.getValue()) {
                        try {
                            // Crée un nouvel objet Email et l'ajoute à l'ensemble
                            emailSet.add(new Email(emailStr));
                        } catch (IllegalArgumentException e) {
                            // Ignorer les emails invalides
                            System.err.println("Email invalide ignoré: " + emailStr);
                        }
                    }
                    
                    // Si des emails ont été ajoutés à l'ensemble, ajoute cet ensemble au map
                    if (!emailSet.isEmpty()) {
                        emails.put(emailType, emailSet);
                    }
                } catch (IllegalArgumentException e) {
                    // Ignorer les types d'email invalides
                    System.err.println("Type d'email invalide ignoré: " + entry.getKey());
                }
            }
        }
        
        // Crée et retourne un nouvel objet Customer avec toutes les propriétés
        return new Customer(id, name, birthDate, emails);
    }
}
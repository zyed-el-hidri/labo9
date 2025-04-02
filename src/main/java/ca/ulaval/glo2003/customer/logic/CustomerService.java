package ca.ulaval.glo2003.customer.logic;

import java.time.LocalDate;
import java.util.*;

/**
 * Service qui gère la logique métier liée aux entités Customer.
 * Cette classe suit le principe de responsabilité unique en se concentrant uniquement sur les opérations client.
 */
public class CustomerService {
    private final CustomerPersistence persistence;
    
    public CustomerService(CustomerPersistence persistence) {
        this.persistence = persistence;
    }
    
    /**
     * Récupère tous les clients.
     */
    public List<Customer> getAllCustomers() {
        return persistence.listAll();
    }
    
    /**
     * Trouve un client spécifique par ID.
     * @throws IllegalArgumentException si le client n'est pas trouvé
     */
    public Customer getCustomerById(String id) {
        return persistence.listAll().stream()
            .filter(customer -> customer.id().equals(id))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID: " + id));
    }
    
    /**
     * Crée un nouveau client.
     */
    public Customer createCustomer(String name, LocalDate birthDate, Map<EmailType, Set<Email>> emails) {
        // Génère un UUID aléatoire pour le nouveau client
        String id = UUID.randomUUID().toString();
        
        Customer customer = new Customer(id, name, birthDate, emails);
        persistence.save(customer);
        
        return customer;
    }
    
    /**
     * Met à jour un client existant.
     * @throws IllegalArgumentException si le client n'est pas trouvé
     */
    public Customer updateCustomer(String id, String name, LocalDate birthDate, Map<EmailType, Set<Email>> emails) {
        // Vérifie si le client existe
        getCustomerById(id);
        
        // Crée le client mis à jour
        Customer updatedCustomer = new Customer(id, name, birthDate, emails);
        persistence.save(updatedCustomer);
        
        return updatedCustomer;
    }
    
    /**
     * Ajoute un nouvel email à un client.
     * @throws IllegalArgumentException si le client n'est pas trouvé ou l'email est invalide
     */
    public Customer addEmailToCustomer(String customerId, EmailType type, String emailValue) {
        Customer customer = getCustomerById(customerId);
        
        // Crée le nouvel email
        Email email = new Email(emailValue);
        
        // Crée une nouvelle map avec les emails existants
        Map<EmailType, Set<Email>> updatedEmails = new HashMap<>(customer.emails());
        
        // Obtient les emails existants pour ce type ou crée un nouveau set
        Set<Email> emailsOfType = new HashSet<>(customer.emailsFor(type));
        emailsOfType.add(email);
        
        // Met à jour la map
        updatedEmails.put(type, emailsOfType);
        
        // Crée le client mis à jour
        Customer updatedCustomer = new Customer(
            customer.id(),
            customer.name(),
            customer.birthDate(),
            updatedEmails
        );
        
        persistence.save(updatedCustomer);
        
        return updatedCustomer;
    }
}
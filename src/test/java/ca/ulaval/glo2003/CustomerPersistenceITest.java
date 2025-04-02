package ca.ulaval.glo2003;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


import ca.ulaval.glo2003.customer.logic.CustomerPersistence;

import static org.junit.Assert.assertEquals;
// Removed to resolve ambiguity
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Set; // Import the Set interface
import java.util.HashSet; // Import the HashSet class
import java.util.Map; // Import the Map interface
import java.util.HashMap; // Import the HashMap class
import ca.ulaval.glo2003.customer.logic.Customer; // Import the Customer class
import ca.ulaval.glo2003.customer.logic.Email; // Import the Email class
import ca.ulaval.glo2003.customer.logic.EmailType; // Import the EmailType class or enum

/*public abstract class CustomerPersistenceITest {

    protected CustomerPersistence persistence;//c'est l'implementation qui va etre tester
    protected abstract CustomerPersistence createPersistence(); // ne pas etre implementee dans cette classe, car elle depend 
    //de l'implementation specifique a tester. protected abstract pour que les sous-classes puissent l'utiliser. pas par le code exeterne
    //c'est le modele de conception "Factory Method" : chaque sous-classe fournit sa propre facon de crreer l'objet a tester

    @BeforeEach
    public void setUp() {
        persistence = createPersistence(); // pour garantir que chaque test commence avec une nouvelle propre instance de persistence
    }

    @Test
    public void givenEmtyPersistence_whenListAll_thenReturnEmtyList(){
        
        List<Customer> customers = persistence.listAll(); // on appelle la methode listAll de l'implementation de persistence
        assertTrue(customers.isEmpty());
    }
}*/

public abstract class CustomerPersistenceITest {
    protected CustomerPersistence persistence;
    protected abstract CustomerPersistence createPersistence();
    private Customer createSampleCustomer(){
        String id ="1";
        String name = "John Doe";
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        Set<Email> workEmails = new HashSet<>();
        workEmails.add(new Email("john@work.com"));
        Set<Email> personalEmails = new HashSet<>();
        personalEmails.add(new Email("john@personal.com"));

        Map<EmailType, Set<Email>> emails = new HashMap<>();
        emails.put(EmailType.WORK, workEmails);
        emails.put(EmailType.PERSONAL, personalEmails);
        return new Customer(id, name, birthDate, emails);
    }

    private Customer createSampleCustomerWithSameIdButDifferentName(String id) {
        String name = "Jane Doe";
        LocalDate birthDate = LocalDate.of(1990, 2, 2);
        Map<EmailType, Set<Email>> emails = new HashMap<>();
        return new Customer(id, name, birthDate, emails);
    }
    @BeforeEach
    public void setUp(){
        persistence = createPersistence();
    }
    
    

    @Test
    public void givenEmtyPersistence_whenListAll_thenReturnEmtyList(){
        List<Customer> customers = persistence.listAll();
        assertTrue(customers.isEmpty());
    }

    @Test
    public void givenCustomerSaved_whenListAll_thenReturnsListWithCustomers() {
        Customer customer = createSampleCustomer();
        persistence.save(customer);
    
        List<Customer> customers = persistence.listAll();
        assertEquals(1, customers.size());
        assertEquals(customer.name(), customers.get(0).name());
    }

    
}
package ca.ulaval.glo2003;

import ca.ulaval.glo2003.customer.infra.CustomerPersistenceInMemory; // Ensure this import matches the actual package of the class
import ca.ulaval.glo2003.customer.logic.CustomerPersistence; // Add the correct import for CustomerPersistence

public class CustomerPersistenceInMemoryITest extends CustomerPersistenceITest {
    
    @Override
    protected CustomerPersistence createPersistence() {
        return new CustomerPersistenceInMemory(); // On retourne une instance de la classe CustomerPersistenceInMemory
    }
    
}

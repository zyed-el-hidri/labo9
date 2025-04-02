package ca.ulaval.glo2003;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import dev.morphia.Datastore;
import dev.morphia.Morphia;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import ca.ulaval.glo2003.customer.infra.CustomerMongo;
import ca.ulaval.glo2003.customer.logic.CustomerPersistence;
import ca.ulaval.glo2003.customer.infra.CustomerPersistenceMongo;

/**
* Cette classe teste l'implémentation MongoDB de l'interface CustomerPersistence.
* Elle étend la classe abstraite CustomerPersistenceITest qui contient tous les tests génériques
* qui doivent fonctionner pour n'importe quelle implémentation de CustomerPersistence.
*/
public class CustomerPersistenceMongoITest extends CustomerPersistenceITest {
   
   // Déclaration d'un conteneur MongoDB pour les tests
   // "static final" signifie que cette variable est partagée entre toutes les instances de cette classe
   // et ne change pas pendant l'exécution du programme
   private static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:latest"));
   
   // La variable datastore qui sera utilisée pour interagir avec MongoDB
   // Elle n'est pas static car chaque test a besoin de sa propre instance
   private Datastore datastore;
   
   // Bloc statique d'initialisation - s'exécute une seule fois lors du chargement de la classe
   // avant l'exécution de n'importe quel test
   static {
       // Démarrer le conteneur MongoDB
       // Nous le faisons dans un bloc statique car démarrer un conteneur Docker est coûteux
       // et nous voulons le faire une seule fois pour tous les tests
       mongoDBContainer.start();
   }
   
   /**
    * Méthode exécutée avant chaque test individuel.
    * Nous remplaçons (@Override) la méthode setUp() de la classe parente pour contrôler l'ordre d'initialisation.
    * Cela garantit que datastore est initialisé avant que createPersistence() ne soit appelé.
    */
   @BeforeEach
   @Override
   public void setUp() {
       // 1. Obtenir la chaîne de connexion au conteneur MongoDB
       // Elle ressemble à "mongodb://localhost:12345" où le port est généré aléatoirement
       String connectionString = mongoDBContainer.getConnectionString();
       
       // 2. Créer un client MongoDB et configurer Morphia (ODM - Object Document Mapper)
       // Morphia permet de convertir facilement entre objets Java et documents MongoDB
       // "test-db" est le nom de la base de données qui sera utilisée pour les tests
       datastore = Morphia.createDatastore(
           MongoClients.create(connectionString), 
           "test-db"
       );
       
       // 3. Supprimer la collection "customers" pour garantir que chaque test commence avec une base de données vide
       // Cela assure l'isolation des tests - chaque test s'exécute dans un environnement propre
       datastore.getDatabase().getCollection("customers").drop();
       
       // 4. Informer Morphia de la classe CustomerMongo pour qu'il puisse mapper les objets correctement
       // Cela permet à Morphia de reconnaître les annotations comme @Entity et @Id dans CustomerMongo
       datastore.getMapper().map(CustomerMongo.class);
       
       // 5. IMPORTANT: Appeler la méthode setUp() de la classe parente APRÈS avoir initialisé datastore
       // La méthode parente appelle createPersistence() qui a besoin de datastore
       // Sans cet ordre spécifique, nous obtiendrions une NullPointerException
       super.setUp();
   }
   
   /**
    * Cette méthode crée et retourne l'implémentation spécifique de CustomerPersistence à tester.
    * Elle est appelée par la méthode setUp() de la classe parente.
    * 
    * C'est le "Factory Method" pattern - chaque sous-classe concrète fournit sa propre façon
    * de créer l'objet à tester.
    */
   @Override
   protected CustomerPersistence createPersistence() {
       // Créer et retourner une instance de CustomerPersistenceMongo en lui passant datastore
       // datastore doit être initialisé avant cet appel, c'est pourquoi l'ordre dans setUp() est crucial
       return new CustomerPersistenceMongo(datastore);
   }
}
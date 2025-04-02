package ca.ulaval.glo2003;

import ca.ulaval.glo2003.customer.api.CustomerResource;
import ca.ulaval.glo2003.customer.api.exceptionmappers.GenericExceptionMapper;
import ca.ulaval.glo2003.customer.api.exceptionmappers.IllegalArgumentExceptionMapper;
import ca.ulaval.glo2003.customer.infra.CustomerPersistenceMongo;
import ca.ulaval.glo2003.customer.logic.CustomerService;
import ca.ulaval.glo2003.shared.infra.DatastoreProvider;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * Point d'entrée de l'application qui configure Jersey et démarre le serveur.
 */
public class Main {
    private static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        // Créer la persistance MongoDB en utilisant le DatastoreProvider existant
        DatastoreProvider datastoreProvider = new DatastoreProvider();
        CustomerPersistenceMongo persistence = new CustomerPersistenceMongo(datastoreProvider.provide());
        
        // Créer le service métier
        CustomerService customerService = new CustomerService(persistence);
        
        // Configurer Jersey
        ResourceConfig config = new ResourceConfig();
        
        // Enregistrer les ressources
        config.register(new CustomerResource(customerService));
        
        // Enregistrer les exception mappers
        config.register(new GenericExceptionMapper());
        config.register(new IllegalArgumentExceptionMapper());
        
        // Créer et configurer le serveur
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));
        Server server = new Server(PORT);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");
        
        try {
            // Démarrer le serveur
            server.start();
            System.out.println("Serveur démarré sur le port " + PORT);
            System.out.println("API endpoints disponibles à http://localhost:" + PORT + "/api/customers");
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.destroy();
        }
    }
}
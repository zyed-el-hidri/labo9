package ca.ulaval.glo2003.customer.api;

import ca.ulaval.glo2003.customer.api.dtos.CustomerDto; // Update to the correct package path
import ca.ulaval.glo2003.customer.api.dtos.CreateCustomerRequest; // Update to the correct package path
import ca.ulaval.glo2003.customer.api.dtos.UpdateCustomerRequest; // Update to the correct package path
import ca.ulaval.glo2003.customer.api.dtos.AddEmailRequest; // Update to the correct package path
import ca.ulaval.glo2003.customer.logic.Customer;
import ca.ulaval.glo2003.customer.logic.CustomerService;
import ca.ulaval.glo2003.customer.logic.Email;
import ca.ulaval.glo2003.customer.logic.EmailType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Contrôleur REST qui expose les endpoints liés aux clients.
 */
@Path("/api/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    
    private final CustomerService customerService;
    
    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    /**
     * Récupérer tous les clients
     * GET /api/customers
     */
    @GET
    public Response getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerDto> customerDtos = customers.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        
        return Response.ok(customerDtos).build();
    }
    
    /**
     * Récupérer un client spécifique par ID
     * GET /api/customers/{id}
     */
    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") String id) {
        try {
            Customer customer = customerService.getCustomerById(id);
            return Response.ok(convertToDto(customer)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }
    
    /**
     * Créer un nouveau client
     * POST /api/customers
     */
    @POST
    public Response createCustomer(CreateCustomerRequest request) {
        // Convertir la map d'emails du DTO
        Map<EmailType, Set<Email>> emailsMap = new HashMap<>();
        
        if (request.emails != null) {
            request.emails.forEach((type, emails) -> {
                EmailType emailType = EmailType.parse(type);
                Set<Email> emailSet = emails.stream()
                    .map(Email::new)
                    .collect(Collectors.toSet());
                emailsMap.put(emailType, emailSet);
            });
        }
        
        Customer customer = customerService.createCustomer(
            request.name,
            request.birthDate,
            emailsMap
        );
        
        CustomerDto dto = convertToDto(customer);
        
        return Response.status(Response.Status.CREATED)
            .entity(dto)
            .build();
    }
    
    /**
     * Mettre à jour un client existant
     * PUT /api/customers/{id}
     */
    @PUT
    @Path("/{id}")
    public Response updateCustomer(
            @PathParam("id") String id,
            UpdateCustomerRequest request) {
        
        try {
            // Convertir la map d'emails du DTO
            Map<EmailType, Set<Email>> emailsMap = new HashMap<>();
            
            if (request.emails != null) {
                request.emails.forEach((type, emails) -> {
                    EmailType emailType = EmailType.parse(type);
                    Set<Email> emailSet = emails.stream()
                        .map(Email::new)
                        .collect(Collectors.toSet());
                    emailsMap.put(emailType, emailSet);
                });
            }
            
            Customer customer = customerService.updateCustomer(
                id,
                request.name,
                request.birthDate,
                emailsMap
            );
            
            return Response.ok(convertToDto(customer)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }
    
    /**
     * Ajouter un email à un client
     * POST /api/customers/{id}/emails
     */
    @POST
    @Path("/{id}/emails")
    public Response addEmail(
            @PathParam("id") String id,
            AddEmailRequest request) {
        
        try {
            EmailType emailType = EmailType.parse(request.type);
            
            Customer customer = customerService.addEmailToCustomer(
                id,
                emailType,
                request.email
            );
            
            return Response.ok(convertToDto(customer)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", e.getMessage()))
                .build();
        }
    }
    
    /**
     * Convertir un objet Customer du domaine en DTO pour la couche API
     */
    private CustomerDto convertToDto(Customer customer) {
        CustomerDto dto = new CustomerDto();
        dto.id = customer.id();
        dto.name = customer.name();
        dto.birthDate = customer.birthDate();
        dto.age = customer.age();
        
        // Convertir la map d'emails
        Map<String, List<String>> emailsMap = new HashMap<>();
        
        customer.emails().forEach((type, emails) -> {
            List<String> emailList = emails.stream()
                .map(Email::toString)
                .collect(Collectors.toList());
            emailsMap.put(type.toString().toLowerCase(), emailList);
        });
        
        dto.emails = emailsMap;
        
        return dto;
    }
}
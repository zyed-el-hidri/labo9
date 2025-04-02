package ca.ulaval.glo2003.customer.infra;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Représentation d'un client dans MongoDB.
 * Cette classe est annotée avec les annotations Morphia pour le mapping objet-document.
 * 
 * @Entity("customers") indique que cette classe sera mappée à la collection "customers" dans MongoDB.
 */
@Entity("customers")
public class CustomerMongo {
    /**
     * L'identifiant unique du client.
     * L'annotation @Id indique à Morphia que ce champ correspond à l'identifiant primaire du document.
     */
    @Id
    public String id;

    /**
     * Le nom du client.
     */
    public String name;
    
    /**
     * La date de naissance au format String (exemple: "1990-01-01").
     * Stockée comme chaîne pour simplifier la sérialisation/désérialisation.
     */
    public String birthDate;
    
    /**
     * Map qui stocke les emails par type.
     * La clé est le nom du type d'email (ex: "WORK", "PERSONAL").
     * La valeur est une liste d'emails de ce type.
     * 
     * Cette structure permet de préserver le type de chaque email,
     * ce qui est crucial pour reconstruire correctement l'objet Customer.
     */
    public Map<String, List<String>> emails = new HashMap<>();

    /**
     * Constructeur par défaut requis par Morphia.
     * Morphia utilise ce constructeur lors de la désérialisation des documents depuis MongoDB.
     */
    public CustomerMongo() {}

    /**
     * Constructeur avec les propriétés de base d'un client.
     * 
     * @param id Identifiant unique du client
     * @param name Nom du client
     * @param birthDate Date de naissance au format String
     */
    public CustomerMongo(String id, String name, String birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }
    
    /**
     * Ajoute un email à la map des emails, en le classant par type.
     * Si le type n'existe pas encore dans la map, une nouvelle liste est créée.
     * 
     * @param type Type d'email (ex: "WORK", "PERSONAL")
     * @param email Adresse email à ajouter
     */
    public void addEmail(String type, String email) {
        // Si le type n'existe pas encore dans la map, créer une nouvelle liste
        if (!emails.containsKey(type)) {
            emails.put(type, new ArrayList<>());
        }
        
        // Ajouter l'email à la liste correspondant à son type
        emails.get(type).add(email);
    }
}
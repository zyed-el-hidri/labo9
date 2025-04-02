package ca.ulaval.glo2003.customer.infra;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;

@Entity("customers")
public class CustomerMongo {
    @Id
    public String id;

    public String name;
    public String birthDate;
    public List<String> emails = new ArrayList<>();

    public CustomerMongo() {}

    public CustomerMongo(String id, String name, String birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }
}

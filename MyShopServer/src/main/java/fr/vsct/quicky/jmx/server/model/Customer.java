package fr.vsct.quicky.jmx.server.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.csv.CSVRecord;

public class Customer {
    public final int id;
    public final String firstName;
    public final String lastName;
    public final String email;
    public final String country;
    public final String ipAddress;
    public final String cardNumber;
    public final String cardType;

    @JsonCreator
    public Customer(@JsonProperty("id        ") int id,
                    @JsonProperty("firstName ") String firstName,
                    @JsonProperty("lastName  ") String lastName,
                    @JsonProperty("email     ") String email,
                    @JsonProperty("country   ") String country,
                    @JsonProperty("ipAddress ") String ipAddress,
                    @JsonProperty("cardNumber") String cardNumber,
                    @JsonProperty("cardType  ") String cardType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.country = country;
        this.ipAddress = ipAddress;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
    }

    public Customer(CSVRecord record) {
        // id,first_name,last_name,email,country,ip_address,card_number,card_type
        id = Integer.parseInt(record.get("id"));
        firstName = record.get("first_name");
        lastName = record.get("last_name");
        email = record.get("email");
        country = record.get("country");
        ipAddress = record.get("ip_address");
        cardNumber = record.get("card_number");
        cardType = record.get("card_type");
    }

    @Override
    public String toString() {
        return firstName + ' ' + lastName + " (" + email + ')';
    }
}

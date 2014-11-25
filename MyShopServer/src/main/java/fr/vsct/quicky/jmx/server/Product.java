package fr.vsct.quicky.jmx.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.csv.CSVRecord;
import org.joda.money.BigMoney;
import org.joda.money.Money;

import java.math.RoundingMode;

public class Product {
    private final int id;
    private final String name;
    private final String description;
    private final Money amount;

    @JsonCreator
    public Product(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("description") String description, @JsonProperty("amount") Money amount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.amount = amount;
    }

    public Product(CSVRecord record) {
        //id,name,description,currency,amount
        this.id = Integer.parseInt(record.get("id"));
        this.name = record.get("name");
        this.description = record.get("description");
        this.amount = BigMoney.parse("EUR " + record.get("amount")).toMoney(RoundingMode.HALF_DOWN);
    }

    public Money getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}

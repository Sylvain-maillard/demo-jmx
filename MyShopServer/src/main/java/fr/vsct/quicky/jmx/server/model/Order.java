package fr.vsct.quicky.jmx.server.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.List;

import static org.joda.money.CurrencyUnit.EUR;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

    public final long id;

    public final List<LigneCommande> productList;

    public final Customer owner;

    @JsonCreator
    public Order(@JsonProperty("id") long id,
                 @JsonProperty("productList") List<LigneCommande> productList,
                 @JsonProperty("owner") Customer owner) {
        this.id = id;
        this.productList = productList;
        this.owner = owner;
    }

    public Money getTotalAmount() {
        return productList.stream().map(LigneCommande::getAmount).reduce(Money.zero(EUR), (sum, amount) -> sum = sum.plus(amount));
    }

    public int getTotalArticles() {
        return productList.stream().mapToInt(LigneCommande::getQuantite).reduce(0,(sum,qte) -> sum +=qte);
    }
}

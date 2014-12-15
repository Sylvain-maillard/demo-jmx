package fr.vsct.quicky.jmx.server.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.money.Money;

/**
 * Created by Sylvain on 10/12/2014.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LigneCommande {

    private final Product product;
    private int quantite = 1;

    @JsonCreator
    public LigneCommande(@JsonProperty("product") Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public Money getAmount() {
        return this.product.getAmount().multipliedBy(quantite);
    }

    public void incrementeQte() {
        quantite++;
    }
}

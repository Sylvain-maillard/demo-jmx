package fr.vsct.quicky.jmx.server.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import org.joda.money.Money;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.joda.money.CurrencyUnit.EUR;
import static org.joda.money.Money.zero;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Basket {

    private final Integer customerId;

    private final Map<Product, LigneCommande> productIndex = Maps.newConcurrentMap();

    @JsonCreator
    public Basket(@JsonProperty("customerId") Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public List<LigneCommande> getLigneCommandes() {
        List<LigneCommande> list = newArrayList(productIndex.values());
        list.sort((c1, c2) -> c1.getProduct().getId() - c2.getProduct().getId());
        return list;
    }

    public Money getTotalAmount() {
        final Money[] total = {zero(EUR)};
        productIndex.values().forEach(ligne -> total[0] = total[0].plus(ligne.getAmount()));
        return total[0];
    }

    public void addToLigneCommandes(Product product) {
        LigneCommande ligneCommande = productIndex.get(product);
        if (ligneCommande == null) {
            ligneCommande = new LigneCommande(product);
            productIndex.put(product, ligneCommande);
        } else {
            ligneCommande.incrementeQte();
        }
    }
}

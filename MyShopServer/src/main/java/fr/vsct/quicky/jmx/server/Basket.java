package fr.vsct.quicky.jmx.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Basket {

    private final Integer customerId;

    private final List<Product> productList = Lists.newArrayList();

    @JsonCreator
    public Basket(@JsonProperty("customerId") Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public Money getTotalAmount() {
        final Money[] total = {Money.zero(CurrencyUnit.EUR)};
        productList.forEach(product -> {
            total[0] = total[0].plus(product.getAmount());
        });
        return total[0];
    }
}

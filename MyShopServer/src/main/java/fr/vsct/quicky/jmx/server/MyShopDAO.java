package fr.vsct.quicky.jmx.server;

import com.google.common.collect.Maps;
import fr.vsct.quicky.jmx.server.model.Basket;
import fr.vsct.quicky.jmx.server.model.Customer;
import fr.vsct.quicky.jmx.server.model.Order;
import fr.vsct.quicky.jmx.server.model.Product;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;


@Component
public class MyShopDAO {

    Map<Integer, Customer> customerMap = Maps.newConcurrentMap();

    Map<Integer, Product> productMap = Maps.newConcurrentMap();

    AtomicLong orderSeq = new AtomicLong();

    Map<Long, Order> orderMap = Maps.newConcurrentMap();

    Map<Integer, Basket> basketMap = Maps.newConcurrentMap();

    @PostConstruct
    public void loadData() throws IOException {
        loadDataFromCsv("customers.csv", record -> {
            customerMap.put(Integer.valueOf(record.get("id")), new Customer(record));
        });
        loadDataFromCsv("products.csv", record -> {
            productMap.put(Integer.valueOf(record.get("id")), new Product(record));
        });
    }

    private void loadDataFromCsv(String resourceName, Consumer<? super CSVRecord> rowAction) throws IOException {
        Reader in = new InputStreamReader(new ClassPathResource(resourceName).getInputStream());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(in);
        records.forEach(rowAction);
    }

    public Map<Integer, Product> findProductBetween(int minPrice, int maxPrice) {

        Map<Integer, Product> result = Maps.newHashMap();

        productMap.values().stream().filter(product -> product.getAmount().getAmountMajorInt() > minPrice && product.getAmount().getAmountMajorInt() < maxPrice).forEach(product -> {
            result.put(product.getId(), product);
        });

        return result;
    }

    public Customer getCustomer(int id) {
        return customerMap.get(id);
    }

    public Product getProduct(int id) {
        return productMap.get(id);
    }

    // just turn basket to order
    public Order saveOrder(Integer customerId) {
        Basket basket = getBasket(customerId);
        long id = orderSeq.incrementAndGet();
        Order order = new Order(id, basket.getLigneCommandes(), getCustomer(basket.getCustomerId()));
        orderMap.put(id, order);
        basketMap.remove(customerId);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return order;
    }

    public Basket getBasket(Integer customerId) {
        Basket basket = basketMap.get(customerId);
        if (basket == null) {
            basket = new Basket(customerId);
            basketMap.put(customerId, basket);
        }
        return basket;
    }

    public Basket addToBasket(int customerId, int productId) {
        Basket basket = getBasket(customerId);
        basket.addToLigneCommandes(getProduct(productId));
        return basket;
    }

    public Order getOrder(long orderId) {
        return orderMap.get(orderId);
    }

    public Collection<Order> allOrders() {
        return orderMap.values();
    }

    public Customer getCustomerByEmail(String email) {
        return customerMap.values().stream().filter((customer) -> customer.email.equals(email)).findFirst().get();
    }

    public Collection<Customer> getAllCustomers() {
        return customerMap.values();
    }

    public Collection<Product> getAllProducts() {
        return productMap.values();
    }
}

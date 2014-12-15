package fr.vsct.quicky.jmx.client;

import com.google.common.collect.ImmutableList;
import fr.vsct.quicky.jmx.server.model.Basket;
import fr.vsct.quicky.jmx.server.model.Customer;
import fr.vsct.quicky.jmx.server.model.Order;
import fr.vsct.quicky.jmx.server.model.Product;
import fr.vsct.quicky.jmx.server.utils.MoneyJacksonModule;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static org.apache.commons.lang3.RandomUtils.nextInt;

/**
 * Created by Sylvain on 25/11/2014.
 */
public class Client {

    static {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.getObjectMapper().registerModule(new MoneyJacksonModule());
        restTemplate = new RestTemplate(ImmutableList.of(converter));
    }

    private static RestTemplate restTemplate;
    private static String baseUrl = "http://localhost:8080";

    private static <T> T query(String url, Class<T> classOfT, Object... parameters) {
        return restTemplate.getForObject(baseUrl + url, classOfT, parameters);
    }

    /**
     * just run one scenario.
     *
     * @param args
     */
    public static void main(String[] args) {
        Client client = new Client();
        client.runScenario();
    }

    /**
     * this is a user scenario.
     */
    public void runScenario() {
        // login (generate a user id between 1..1000)
        int userId = nextInt(1, 1000);
        Customer currentUser = query("/login/{userId}", Customer.class, userId);
        System.out.println(currentUser.firstName + " " + currentUser.lastName + " logged in.");

        // browse some products:
        int basePrice = nextInt(1, 50);
        Product[] products = query("/products/prices/{min}/{max}", Product[].class, basePrice, basePrice + nextInt(5, 10));
        System.out.println("got " + products.length + " products");

        // browse some products more:
        basePrice = nextInt(1, 50);
        products = query("/products/prices/{min}/{max}", Product[].class, basePrice, basePrice + nextInt(5, 10));
        System.out.println("got " + products.length + " products");

        // select some products from the last selection:
        Basket basket;
        int productCountToSelect = nextInt(1, 5);
        for (int i = 0; i < productCountToSelect; i++) {
            // add the product to the user basket:
            basket = query("/basket/{userId}/add/{productId}", Basket.class, userId, products[i].getId());
        }

        // query the basket:
        basket = query("/basket/{userId}", Basket.class, userId);

        // order the basket:
        Order order = query("/basket/{userId}/order", Order.class, userId);
        System.out.println("created order: " + order.id);
    }
}

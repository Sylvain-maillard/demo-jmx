package fr.vsct.quicky.jmx.client;

import com.google.common.collect.ImmutableList;
import fr.vsct.quicky.jmx.server.model.Basket;
import fr.vsct.quicky.jmx.server.model.Customer;
import fr.vsct.quicky.jmx.server.model.Order;
import fr.vsct.quicky.jmx.server.model.Product;
import fr.vsct.quicky.jmx.server.utils.MoneyJacksonModule;
import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.apache.commons.lang3.RandomUtils.nextLong;

/**
 * Created by Sylvain on 25/11/2014.
 */
public class Client {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

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

    private void waitALittle() {
        try {
            Thread.sleep(nextLong(50,200));
        } catch (InterruptedException e) {
            // this _should_ not happen
        }
    }

    /**
     * this is a user scenario.
     */
    public void runScenario() {
        // login (generate a user id between 1..1000)
        int userId = nextInt(1, 1000);
        Customer currentUser = query("/login/{userId}", Customer.class, userId);
        MDC.put("user", currentUser);
        LOGGER.info("logged in.");

        waitALittle();

        // browse some products:
        int basePrice = nextInt(1, 50);
        Product[] products = query("/products/prices/{min}/{max}", Product[].class, basePrice, basePrice + nextInt(5, 10));
        LOGGER.info("selected {} products", products.length);

        waitALittle();

        // browse some products more:
        basePrice = nextInt(50, 60);
        products = query("/products/prices/{min}/{max}", Product[].class, basePrice, basePrice + nextInt(5, 10));
        LOGGER.info("selected {} other products", products.length);

        waitALittle();

        // select some products from the last selection:
        Basket basket;
        int productCountToSelect = nextInt(1, 5);
        int i = 0;
        for (; i < productCountToSelect; i++) {
            // add the product to the user basket:
            basket = query("/basket/{userId}/add/{productId}", Basket.class, userId, products[i].getId());
        }
        LOGGER.info("added {} products to basket", i);

        // query the basket:
        basket = query("/basket/{userId}", Basket.class, userId);

        waitALittle();

        // order the basket:
        Order order = query("/basket/{userId}/order", Order.class, userId);
        LOGGER.info("created order #{}, total price is {} for {} articles", order.id, order.getTotalAmount(), order.getTotalArticles());
        MDC.remove("user");
    }
}

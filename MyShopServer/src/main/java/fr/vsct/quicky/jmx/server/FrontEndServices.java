package fr.vsct.quicky.jmx.server;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import fr.vsct.quicky.jmx.server.model.Basket;
import fr.vsct.quicky.jmx.server.model.Customer;
import fr.vsct.quicky.jmx.server.model.Order;
import fr.vsct.quicky.jmx.server.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

/**
 * basic front controller
 */
@RestController
@ManagedResource
public class FrontEndServices {

    private final MyShopDAO dao;
    private final Counter orderCount;
    private final MetricRegistry metricRegistry;
    private final Timer orderTimer;

    @Autowired
    public FrontEndServices(MyShopDAO dao) {
        this.dao = dao;
        this.metricRegistry = new MetricRegistry();
        this.orderCount = metricRegistry.counter("app.Front.order.count");
        this.orderTimer = metricRegistry.timer("app.Front.order.timer");
    }

    /**
     * login to the system
     *
     * @param id user id.
     * @return the customer informations
     */
    @RequestMapping(value = "/login/{id}")
    public Customer login(@PathVariable("id") Integer id) {
        return dao.getCustomer(id);
    }

    /**
     * select products between min and max prices.
     *
     * @param min the min price
     * @param max the max price
     * @return a list of matching products.
     */
    @RequestMapping(value = "/products/prices/{min}/{max}")
    public Collection<Product> allProductsBetweenPrices(@PathVariable(value = "min") int min, @PathVariable("max") int max) {
        return dao.findProductBetween(min, max).values();
    }

    /**
     * Add items to the logged user
     *
     * @param customerId the user id
     * @param productId  the product to add
     * @return the current basket.
     */
    @RequestMapping(value = "/basket/{customerId}/add/{productId}")
    public Basket addToBasket(@PathVariable("customerId") int customerId, @PathVariable("productId") int productId) {
        return dao.addToBasket(customerId, productId);
    }

    /**
     * display the customer current basket.
     *
     * @param customerId the customer id.
     * @return the basket
     */
    @RequestMapping(value = "/basket/{customerId}")
    public Basket displayBasket(@PathVariable("customerId") int customerId) {
        return dao.getBasket(customerId);
    }

    /**
     * turn the customer basket into a order that could be paid.
     * the basket is cleared after this operation.
     *
     * @param customerId user id.
     * @return the new order.
     */
    @RequestMapping(value = "/basket/{customerId}/order")
    public Order basketOrder(@PathVariable("customerId") int customerId) {
        orderCount.inc();
        Timer.Context time = orderTimer.time();
        try {
            return dao.saveOrder(customerId);
        } finally {
            time.stop();
        }
    }

    /**
     * display the order
     *
     * @param orderId the order id.
     * @return the order.
     */
    @RequestMapping(value = "/orders/{orderId}")
    public Order displayOrder(@PathVariable("orderId") int orderId) {
        return dao.getOrder(orderId);
    }

    @ManagedAttribute
    public long getOrderCount() {
        return orderCount.getCount();
    }

    @ManagedAttribute(description = "mean response time in milliseconds.")
    public double getOrderMeanResponseTime() {
        return NANOSECONDS.toMillis((long) orderTimer.getSnapshot().getMean());
    }
}

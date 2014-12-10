package fr.vsct.quicky.jmx.server;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import fr.vsct.quicky.jmx.server.counting.Counting;
import fr.vsct.quicky.jmx.server.model.Basket;
import fr.vsct.quicky.jmx.server.model.Customer;
import fr.vsct.quicky.jmx.server.model.Order;
import fr.vsct.quicky.jmx.server.model.Product;
import fr.vsct.quicky.jmx.server.utils.LoginFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * basic front controller
 */
@RestController
@ManagedResource
public class FrontEndServices {

    private final MyShopDAO dao;
    private final AtomicInteger orderCallsCount = new AtomicInteger();
    private final Meter orderCount;

    @Autowired
    public FrontEndServices(MyShopDAO dao) {
        this.dao = dao;
        MetricRegistry metricRegistry = new MetricRegistry();
        orderCount = metricRegistry.meter("orderCount");
    }

    @Counting
    @RequestMapping(value = "/products/prices/{min}/{max}")
    public Collection<Product> allProductsBetweenPrices(@PathVariable(value = "min") int min, @PathVariable("max") int max) {
        return dao.findProductBetween(min, max).values();
    }

    @Counting
    @RequestMapping(value = "/login/{id}")
    public Customer login(@PathVariable("id") Integer id) {
        return dao.getCustomer(id);
    }

    @Counting
    @RequestMapping(value = "/basket/{customerId}/add/{productId}")
    public Basket addToBasket(@PathVariable("customerId") int customerId, @PathVariable("productId") int productId) {
        return dao.addToBasket(customerId, productId);
    }

    @Counting
    @RequestMapping(value = "/basket/{customerId}")
    public Basket displayBasket(@PathVariable("customerId") int customerId) {
        return dao.getBasket(customerId);
    }

    @Counting
    @RequestMapping(value = "/basket/{customerId}/order")
    public Order basketOrder(@PathVariable("customerId") int customerId) {
        orderCallsCount.incrementAndGet();
        orderCount.mark();
        return dao.saveOrder(customerId);
    }

    @Counting
    @RequestMapping(value = "/orders/{orderId}")
    public Order displayOrder(@PathVariable("orderId") int orderId) {
        return dao.getOrder(orderId);
    }

    @ManagedAttribute
    public int getOrderCallsCount() {
        return orderCallsCount.get();
    }

    @ManagedAttribute
    public double getOrderCallsCountOneMinute() {
        return orderCount.getOneMinuteRate();
    }
}

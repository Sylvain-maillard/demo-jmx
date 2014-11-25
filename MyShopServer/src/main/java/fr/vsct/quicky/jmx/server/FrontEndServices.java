package fr.vsct.quicky.jmx.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * basic front controller
 */
@RestController
public class FrontEndServices {

    private final MyShopDAO dao;

    @Autowired
    public FrontEndServices(MyShopDAO dao) {
        this.dao = dao;
    }

    @RequestMapping(value = "/products")
    public Collection<Product> allProducts() {
        return dao.productMap.values();
    }

    @RequestMapping(value = "/products/prices/{min}/{max}")
    public Collection<Product> allProductsBetweenPrices(@PathVariable(value = "min") int min, @PathVariable("max") int max) {
        return dao.findProductBetween(min, max).values();
    }

    @RequestMapping(value = "/customers")
    public Collection<Customer> allCustomers() {
        return dao.customerMap.values();
    }

    @RequestMapping(value = "/login/{id}")
    public Customer login(@PathVariable("id") Integer id) {
        return dao.getCustomer(id);
    }

    @RequestMapping(value = "/basket/{customerId}/add/{productId}")
    public Basket addToBasket(@PathVariable("customerId") int customerId, @PathVariable("productId") int productId) {
        return dao.addToBasket(customerId, productId);
    }

    @RequestMapping(value = "/basket/{customerId}")
    public Basket displayBasket(@PathVariable("customerId") int customerId) {
        return dao.getBasket(customerId);
    }

    @RequestMapping(value = "/basket/{customerId}/order")
    public Order basketOrder(@PathVariable("customerId") int customerId) {
        return dao.saveOrder(customerId);
    }

    @RequestMapping(value = "/orders/{orderId}")
    public Order displayOrder(@PathVariable("orderId") int orderId) {
        return dao.getOrder(orderId);
    }

    @RequestMapping(value = "/orders/")
    public Collection<Order> allOrders() {
        return dao.allOrders();
    }
}

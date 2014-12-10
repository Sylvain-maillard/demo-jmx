package fr.vsct.quicky.jmx.server.utils;

import fr.vsct.quicky.jmx.server.MyShopDAO;
import fr.vsct.quicky.jmx.server.model.Customer;
import fr.vsct.quicky.jmx.server.model.Order;
import fr.vsct.quicky.jmx.server.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Sylvain on 10/12/2014.
 */
@RestController
public class FrontTools {

    private final MyShopDAO dao;

    @Autowired
    public FrontTools(MyShopDAO dao) {
        this.dao = dao;
    }

    @RequestMapping(value = "/products")
    public Collection<Product> allProducts() {
        return dao.getAllProducts();
    }

    @RequestMapping(value = "/customers")
    public Collection<Customer> allCustomers() {
        return dao.getAllCustomers();
    }


    @RequestMapping(value = "/orders/")
    public Collection<Order> allOrders() {
        return dao.allOrders();
    }

    @RequestMapping(value = "/signIn", method = POST, consumes = "application/json")
    public Customer loginByEmail(@RequestBody String email) {
        Customer customerByEmail = dao.getCustomerByEmail(email);
        if (customerByEmail == null) {
            throw new LoginFailedException();
        }
        return customerByEmail;
    }


}

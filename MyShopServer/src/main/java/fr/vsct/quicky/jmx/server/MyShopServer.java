package fr.vsct.quicky.jmx.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyShop Server
 */
@ComponentScan
@EnableAutoConfiguration
@Configuration
public class MyShopServer {

    public static void main(String[] args) {
        SpringApplication.run(MyShopServer.class);
    }
}

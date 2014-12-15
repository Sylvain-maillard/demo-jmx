package fr.vsct.quicky.jmx.server;

import com.codahale.metrics.DefaultObjectNameFactory;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.management.ObjectName;
import java.util.concurrent.TimeUnit;

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

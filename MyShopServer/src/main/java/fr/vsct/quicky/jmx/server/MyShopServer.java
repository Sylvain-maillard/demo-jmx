package fr.vsct.quicky.jmx.server;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * MyShop Server
 */
@ComponentScan
@EnableAutoConfiguration
@Configuration
public class MyShopServer {

    @Bean
    public MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    @Bean (initMethod = "start", destroyMethod = "stop")
    public JmxReporter jmxReporter() {
        return JmxReporter.forRegistry(metricRegistry())
                .inDomain(MyShopServer.class.getPackage().getName() + ".counters")
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(MyShopServer.class);
    }
}

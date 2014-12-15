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

    @Autowired
    MetricRegistry registry;

    public static void main(String[] args) {
        SpringApplication.run(MyShopServer.class);
    }

    @Bean
    public JmxReporter jmxReporter() {
        final JmxReporter reporter = JmxReporter.forRegistry(registry)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .inDomain(MyShopServer.class.getPackage().getName() + ".metrics")
                .createsObjectNamesWith(new DefaultObjectNameFactory() {
                    @Override
                    public ObjectName createName(String type, String domain, String name) {
                        // distinguish between our counter and spring actuators ones.
                        if (name.startsWith("app.")) {
                            return super.createName(type, domain, name.substring(4));
                        } else {
                            return super.createName(type, domain + ".spring", name);
                        }
                    }
                })
                .build();
        reporter.start();
        return reporter;
    }
}

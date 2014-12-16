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
    MetricRegistry metricRegistry;

    @Bean (initMethod = "start", destroyMethod = "stop")
    public JmxReporter jmxReporter() {
        return JmxReporter.forRegistry(metricRegistry)
                .inDomain(MyShopServer.class.getPackage().getName() + ".counters")
                .createsObjectNamesWith(new DefaultObjectNameFactory() {
                    @Override
                    public ObjectName createName(String type, String domain, String name) {
                        if (name.startsWith("app.")) {
                            return super.createName(type, domain, name.substring(4));
                        } else {
                            return super.createName(type, domain + ".spring", name);
                        }
                    }
                })
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(MyShopServer.class);
    }
}

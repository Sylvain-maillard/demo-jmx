package fr.vsct.quicky.jmx.metrics;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;

/**
 * Created by Sylvain on 11/12/2014.
 */
public class HistogramTest {
    public static void main(String[] args) {
        MetricRegistry registry = new MetricRegistry();

        Histogram myHistogram = registry.histogram("myHistogram");
        myHistogram.update(100);

        System.out.println(myHistogram.getCount());
        System.out.println(myHistogram.getSnapshot().size());
        System.out.println(myHistogram.getSnapshot().getMax());
        System.out.println(myHistogram.getSnapshot().getMin());

        myHistogram.update(100);

        System.out.println(myHistogram.getCount());
        System.out.println(myHistogram.getSnapshot().size());
        System.out.println(myHistogram.getSnapshot().getMean());
        System.out.println(myHistogram.getSnapshot().getMedian());
        System.out.println(myHistogram.getSnapshot().getMax());
        System.out.println(myHistogram.getSnapshot().getMin());

        final JmxReporter reporter = JmxReporter.forRegistry(registry).build();
        reporter.start();

    }
}

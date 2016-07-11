package org.restcomm.commons.statistics.test;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import java.util.concurrent.TimeUnit;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restcomm.commons.statistics.reporter.RestcommStatsReporter;

/**
 * This class represents a unit test for Histogram events on RestComm Stats Module. 
 * @author Ricardo Limonta
 * @since 2016-05-29
 */
public class StatsHistogramTest {
    
    private static MetricRegistry metrics;

    @BeforeClass
    public static void setup() {
        metrics = new MetricRegistry();
    }
    
    @Test
    public void meterStats() throws Exception {
        //start reporter
        RestcommStatsReporter statsReporter = 
                             RestcommStatsReporter.forRegistry(metrics).build();
        
        //define periodicy
        statsReporter.start(1, TimeUnit.SECONDS);

        //define metric name
        Histogram histogram = metrics.histogram("sip-requests");

        //simulate metric sender
        for (int i = 0; i < 10; i++) {
            //increment request events
            histogram.update(i * 100);
            //simulate interval
            Thread.sleep(1000);
        }
    }
}
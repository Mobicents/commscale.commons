package org.restcomm.commons.statistics.test;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import java.util.concurrent.TimeUnit;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restcomm.commons.statistics.reporter.RestcommStatsReporter;

/**
 * This class represents a unit test for Meter events on RestComm Stats Module. 
 * @author Ricardo Limonta
 * @since 2016-05-29
 */
public class StatsMeterTest {
    
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
        Meter requests = metrics.meter("sip-invites");

        //simulate metric sender
        for (int i = 0; i < 10; i++) {
            //mark requests events
            requests.mark(i);
            //simulate interval
            Thread.sleep(1000);
        }  
    }
}
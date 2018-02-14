package org.restcomm.commons.statistics.test;

import org.junit.Test;
import org.mockito.Mockito;
import org.restcomm.commons.statistics.reporter.RestcommStatsReporter;
import org.restcomm.commons.statistics.reporter.RestcommStatsReporterExtension;

/**
 * @author oleg.agafonov@telestax.com (Oleg Agafonov)
 */
public class StatsReporterExtensionTest {

    @Test
    public void extendTest() {
        RestcommStatsReporterExtension extension = Mockito.mock(RestcommStatsReporterExtension.class);

        RestcommStatsReporter statsReporter = RestcommStatsReporter.getRestcommStatsReporter();
        statsReporter.getMetricRegistry().counter("counter");
        statsReporter.getMetricRegistry().histogram("histogram");
        statsReporter.getMetricRegistry().meter("meter");
        statsReporter.setRestcommStatsReporterExtension(extension);
        statsReporter.report();

        Mockito.verify(extension, Mockito.times(3)).extend(Mockito.anyMap());
    }
}

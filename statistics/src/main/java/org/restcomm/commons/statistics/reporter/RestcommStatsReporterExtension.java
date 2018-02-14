package org.restcomm.commons.statistics.reporter;

import java.util.Map;

/**
 * @author oleg.agafonov@telestax.com (Oleg Agafonov)
 */
public interface RestcommStatsReporterExtension {

    void extend(Map<String, Object> values);
}

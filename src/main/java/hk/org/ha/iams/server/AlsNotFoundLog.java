package hk.org.ha.iams.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * ALS stub. No ALS JAR in v1. Writes a 404-not-found line so the later ALS adapter can hook here.
 * Does not change the HTTP status returned to CMS.
 */
@Component
public class AlsNotFoundLog {

    private static final Logger LOG = LoggerFactory.getLogger(AlsNotFoundLog.class);

    public void warnHttp404(String detail) {
        LOG.warn("ALS_STUB HTTP 404 not-found | {}", detail);
    }
}

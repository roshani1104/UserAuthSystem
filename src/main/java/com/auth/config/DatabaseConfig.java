package com.auth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public final class DatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);
    private static final String JNDI_NAME = "java:comp/env/jdbc/authDB";

    private DatabaseConfig() {}

    private static final class Holder {
        private static final DataSource INSTANCE = resolve();
    }

    public static DataSource getDataSource() {
        return Holder.INSTANCE;
    }

    public static void shutdown() {
        log.info("DataSource shutdown — managed by Tomcat");
    }

    private static DataSource resolve() {
        try {
            DataSource ds = (DataSource) new InitialContext().lookup(JNDI_NAME);
            log.info("DataSource resolved via JNDI ({})", JNDI_NAME);
            return ds;
        } catch (NamingException e) {
            throw new IllegalStateException(
                "Could not resolve JNDI DataSource '" + JNDI_NAME + "'. " +
                "Make sure context.xml is configured in META-INF/", e);
        }
    }
}
